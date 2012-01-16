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

package com.manydesigns.portofino.dispatcher;

/*
* @author Paolo Predonzani     - paolo.predonzani@manydesigns.com
* @author Angelo Lupo          - angelo.lupo@manydesigns.com
* @author Giampiero Granatella - giampiero.granatella@manydesigns.com
* @author Alessio Stalla - alessio.stalla@manydesigns.com
*/
/*public class CrudPageInstance extends PageInstance {

    protected final String pk;
    protected final CrudConfiguration crudConfiguration;

    protected final ClassAccessor classAccessor;
    protected final Table baseTable;
    protected final PkHelper pkHelper;

    protected Object object;

    public CrudPageInstance(Application application, CrudPage page, String mode, String param) {
        super(application, page, mode);
        this.pk = param;
        this.crudConfiguration = page.getCrud();

        ClassAccessor tmpClassAccessor = null;
        Table tmpBaseTable = null;
        PkHelper tmpPkHelper = null;
        if(crudConfiguration != null && crudConfiguration.getActualTable() != null) {
            try {
                tmpClassAccessor = application.getCrudAccessor(crudConfiguration);
                tmpBaseTable = crudConfiguration.getActualTable();
                tmpPkHelper = new PkHelper(tmpClassAccessor);
            } catch (Exception e) {
                logger.warn("Crud threw exception (probably not configured).", e);
                tmpClassAccessor = null;
                tmpBaseTable = null;
                tmpPkHelper = null;
            }
        }
        classAccessor = tmpClassAccessor;
        baseTable = tmpBaseTable;
        pkHelper = tmpPkHelper;
    }

    @Override
    public boolean realize() {
        if(CrudPage.MODE_DETAIL.equals(mode)) {
            OgnlContext ognlContext = ElementsThreadLocals.getOgnlContext();
            loadObject(pk);
            if(object != null) {
                ognlContext.put(crudConfiguration.getActualVariable(), object);
            } else {
                logger.info("Not in use case: " + crudConfiguration.getName());
                return false;
            }
        }
        return realized = true;
    }

    private void loadObject(String pk) {
        Serializable pkObject = pkHelper.parsePkString(pk);
        object = QueryUtils.getObjectByPk(
                application,
                baseTable, pkObject,
                crudConfiguration.getQuery(), null);
    }

    // Getter/setter

    public String getPk() {
        return pk;
    }

    @Override
    public CrudPage getPage() {
        return (CrudPage) super.getPage();
    }

    @Override
    public String formatUrlFragment(String baseFragment) {
        if (pk == null) {
            return baseFragment;
        } else {
            return String.format("%s/%s", baseFragment, pk);
        }
    }

    public CrudConfiguration getCrudConfiguration() {
        return crudConfiguration;
    }

    public ClassAccessor getClassAccessor() {
        return classAccessor;
    }

    public Table getBaseTable() {
        return baseTable;
    }

    public PkHelper getPkHelper() {
        return pkHelper;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public List<Page> getChildPages() {
        if (CrudPage.MODE_SEARCH.equals(mode)) {
            return page.getChildPages();
        } else if (CrudPage.MODE_DETAIL.equals(mode)) {
            return getPage().getDetailChildPages();
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public String getLayoutContainer() {
        if (CrudPage.MODE_SEARCH.equals(mode)) {
            return page.getLayoutContainer();
        } else if (CrudPage.MODE_DETAIL.equals(mode)) {
            return getPage().getDetailLayoutContainer();
        } else {
            throw new IllegalStateException("Unsupported mode: " + mode);
        }
    }

    @Override
    public void setLayoutContainer(String layoutContainer) {
        if (CrudPage.MODE_SEARCH.equals(mode)) {
            page.setLayoutContainer(layoutContainer);
        } else if (CrudPage.MODE_DETAIL.equals(mode)) {
            getPage().setDetailLayoutContainer(layoutContainer);
        } else {
            throw new IllegalStateException("Unsupported mode: " + mode);
        }
    }

    @Override
    public void setLayoutOrder(int order) {
        if (CrudPage.MODE_SEARCH.equals(mode)) {
            page.setLayoutOrder(Integer.toString(order));
        } else if (CrudPage.MODE_DETAIL.equals(mode)) {
            getPage().setDetailLayoutOrder(Integer.toString(order));
        } else {
            throw new IllegalStateException("Unsupported mode: " + mode);
        }
    }

    @Override
    public int getLayoutOrder() {
        if (CrudPage.MODE_SEARCH.equals(mode)) {
            return page.getActualLayoutOrder();
        } else if (CrudPage.MODE_DETAIL.equals(mode)) {
            return getPage().getActualDetailLayoutOrder();
        } else {
            throw new IllegalStateException("Unsupported mode: " + mode);
        }
    }

    @Override
    public boolean removeChild(Page page) {
        if (CrudPage.MODE_SEARCH.equals(mode)) {
            return getPage().removeChild(page);
        } else if (CrudPage.MODE_DETAIL.equals(mode)) {
            return getPage().removeDetailChild(page);
        } else {
            throw new IllegalStateException("Unsupported mode: " + mode);
        }
    }

    @Override
    public void addChild(Page page) {
        if (CrudPage.MODE_SEARCH.equals(mode)) {
            getPage().addChild(page);
        } else if (CrudPage.MODE_DETAIL.equals(mode)) {
            getPage().addDetailChild(page);
        } else {
            throw new IllegalStateException("Unsupported mode: " + mode);
        }
    }
}*/
