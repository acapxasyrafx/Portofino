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

package com.manydesigns.portofino.actions;

import com.manydesigns.elements.reflection.ClassAccessor;
import com.manydesigns.portofino.annotations.InjectDispatch;
import com.manydesigns.portofino.dispatcher.CrudNodeInstance;
import com.manydesigns.portofino.dispatcher.Dispatch;
import com.manydesigns.portofino.dispatcher.SiteNodeInstance;
import com.manydesigns.portofino.model.site.CrudNode;
import com.manydesigns.portofino.util.ShortNameUtils;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.controller.StripesConstants;

/*
* @author Paolo Predonzani     - paolo.predonzani@manydesigns.com
* @author Angelo Lupo          - angelo.lupo@manydesigns.com
* @author Giampiero Granatella - giampiero.granatella@manydesigns.com
* @author Alessio Stalla       - alessio.stalla@manydesigns.com
*/
public abstract class AbstractActionBean implements ActionBean {
    public static final String copyright =
            "Copyright (c) 2005-2011, ManyDesigns srl";

    public final static String INPUT = "input";
    public final static String SUCCESS = "success";

    protected ActionBeanContext context;

    @InjectDispatch
    public Dispatch dispatch;


    public String returnToParentTarget;

    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    public ActionBeanContext getContext() {
        return context;
    }

    public boolean isEmbedded() {
        return context.getRequest().getAttribute(StripesConstants.REQ_ATTR_INCLUDE_PATH) != null;
    }

    protected void setupReturnToParentTarget() {
        SiteNodeInstance[] siteNodeInstancePath =
                dispatch.getSiteNodeInstancePath();
        int previousPos = siteNodeInstancePath.length - 2;
        returnToParentTarget = null;
        if (previousPos >= 0) {
            SiteNodeInstance previousNode = siteNodeInstancePath[previousPos];
            if (previousNode instanceof CrudNodeInstance) {
                CrudNodeInstance crudNodeInstance =
                        (CrudNodeInstance) previousNode;
                if (CrudNode.MODE_SEARCH.equals(crudNodeInstance.getMode())) {
                    returnToParentTarget = crudNodeInstance.getCrud().getName();
                } else if (CrudNode.MODE_DETAIL.equals(crudNodeInstance.getMode())) {
                    Object previousNodeObject = crudNodeInstance.getObject();
                    ClassAccessor previousNodeClassAccessor =
                            crudNodeInstance.getClassAccessor();
                    returnToParentTarget = ShortNameUtils.getName(
                            previousNodeClassAccessor, previousNodeObject);
                }
            }
        }
    }

    public Dispatch getDispatch() {
        return dispatch;
    }

    public String getReturnToParentTarget() {
        return returnToParentTarget;
    }

}
