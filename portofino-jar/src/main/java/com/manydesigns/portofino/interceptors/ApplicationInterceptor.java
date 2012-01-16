/*
 * Copyright (C) 2005-2011 ManyDesigns srl.  All rights reserved.
 * http://www.manydesigns.com/
 *
 * Unless you have purchased a commercial license agreement from ManyDesigns srl,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
 *
 * There are special exceptions to the terms and conditions of the GPL
 * as it is applied to this software. View the full text of the
 * exception in file OPEN-SOURCE-LICENSE.txt in the directory of this
 * software distribution.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307  USA
 *
 */

package com.manydesigns.portofino.interceptors;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.blobs.BlobManager;
import com.manydesigns.portofino.actions.PortofinoAction;
import com.manydesigns.portofino.actions.RequestAttributes;
import com.manydesigns.portofino.application.AppProperties;
import com.manydesigns.portofino.application.Application;
import com.manydesigns.portofino.breadcrumbs.Breadcrumbs;
import com.manydesigns.portofino.dispatcher.Dispatch;
import com.manydesigns.portofino.dispatcher.PageInstance;
import com.manydesigns.portofino.logic.SecurityLogic;
import com.manydesigns.portofino.navigation.Navigation;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/*
* @author Paolo Predonzani     - paolo.predonzani@manydesigns.com
* @author Angelo Lupo          - angelo.lupo@manydesigns.com
* @author Giampiero Granatella - giampiero.granatella@manydesigns.com
* @author Alessio Stalla       - alessio.stalla@manydesigns.com
*/
@Intercepts(LifecycleStage.CustomValidation)
public class ApplicationInterceptor implements Interceptor {
    public static final String copyright =
            "Copyright (c) 2005-2011, ManyDesigns srl";

    public final static Logger logger =
            LoggerFactory.getLogger(ApplicationInterceptor.class);

    public Resolution intercept(ExecutionContext context) throws Exception {
        logger.debug("Retrieving Stripes objects");
        ActionBeanContext actionContext = context.getActionBeanContext();

        logger.debug("Retrieving Servlet API objects");
        HttpServletRequest request = actionContext.getRequest();

        logger.debug("Retrieving Portofino application");
        Application application =
                (Application) request.getAttribute(
                        RequestAttributes.APPLICATION);

        logger.debug("Starting page response timer");
        StopWatch stopWatch = new StopWatch();
        // There is no need to stop this timer.
        stopWatch.start();
        request.setAttribute(RequestAttributes.STOP_WATCH, stopWatch);

        logger.debug("Setting skin");
        if(request.getAttribute("skin") == null) {
            String skin = application.getAppConfiguration().getString(AppProperties.SKIN);
            request.setAttribute("skin", skin);
        }

        logger.debug("Setting blobs directory");
        BlobManager blobManager = ElementsThreadLocals.getBlobManager();
        blobManager.setBlobsDir(application.getAppBlobsDir());

        Dispatch dispatch =
                (Dispatch) request.getAttribute(RequestAttributes.DISPATCH);
        if (dispatch != null) {
            List<String> groups =
                    (List<String>) request.getAttribute(RequestAttributes.GROUPS);

            logger.debug("Creating navigation");
            boolean admin = SecurityLogic.isAdministrator(request);
            Navigation navigation =
                    new Navigation(application, dispatch, groups, admin);
            request.setAttribute(RequestAttributes.NAVIGATION, navigation);

            int i = 0;
            for(PageInstance page : dispatch.getPageInstancePath()) {
                i++;
                PortofinoAction actionBean = instantiateActionBean(page);
                configureActionBean(actionBean, page);
                Resolution resolution = actionBean.prepare(page, actionContext);
                if(resolution != null) {
                    logger.error("Page realization failed for {}", page);
                    return resolution;
                }
            }
            PageInstance pageInstance = dispatch.getLastPageInstance();
            request.setAttribute(RequestAttributes.PAGE_INSTANCE, pageInstance);

            logger.debug("Creating breadcrumbs");
            Breadcrumbs breadcrumbs = new Breadcrumbs(dispatch);
            request.setAttribute(RequestAttributes.BREADCRUMBS, breadcrumbs);
        }

        return context.proceed();
    }

    protected PortofinoAction instantiateActionBean(PageInstance page) throws IllegalAccessException, InstantiationException {
        PortofinoAction action = page.getActionClass().newInstance();
        page.setActionBean(action);
        return action;
    }

    protected void configureActionBean(PortofinoAction actionBean, PageInstance page) throws JAXBException, IOException {
        Class<?> configurationClass = actionBean.getConfigurationClass();
        String configurationPackage = configurationClass.getPackage().getName();

        //TODO!!!
        File pageFile = new File(page.getDirectory(), "configuration.xml");
        if(pageFile.exists()) {
            JAXBContext jaxbContext = JAXBContext.newInstance(configurationPackage);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            FileInputStream in = new FileInputStream(pageFile);
            try {
                Object configuration = unmarshaller.unmarshal(in);
                if(!configurationClass.isInstance(configuration)) {
                    logger.error("Invalid configuration: expected " + configurationClass + ", got " + configuration);
                    return;
                }
                page.setConfiguration(configuration);
            } finally {
                in.close();
            }
        }
    }

}
