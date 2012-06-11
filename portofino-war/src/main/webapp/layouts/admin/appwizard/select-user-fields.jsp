<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes-dynattr.tld"
%><%@ taglib prefix="mde" uri="/manydesigns-elements"
%><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"
%><%@ taglib tagdir="/WEB-INF/tags" prefix="portofino" %>
<stripes:layout-render name="/skins/default/admin-page.jsp">
    <jsp:useBean id="actionBean" scope="request" type="com.manydesigns.portofino.actions.admin.appwizard.ApplicationWizard"/>
    <stripes:layout-component name="pageTitle">
        Step 3a. Customize user table
    </stripes:layout-component>
    <stripes:layout-component name="contentHeader">
        <portofino:buttons list="select-user-fields" cssClass="contentButton" />
    </stripes:layout-component>
    <stripes:layout-component name="portletTitle">
        Step 3a. Customize user table
    </stripes:layout-component>
    <stripes:layout-component name="portletBody">
        <mde:sessionMessages />
        <ul>
            <li><mde:write name="actionBean" property="userIdPropertyField"/></li>
            <li><mde:write name="actionBean" property="userNamePropertyField"/></li>
            <li><mde:write name="actionBean" property="userPasswordPropertyField"/></li>
        </ul>
        <div style="display: none;">
            <mde:write name="actionBean" property="userTableField"/>
            <mde:write name="actionBean" property="rootsForm"/>
            <mde:write name="actionBean" property="schemasForm"/>
            <input type="hidden" name="connectionProviderType" value="${actionBean.connectionProviderType}" />
            <input type="hidden" name="advanced" value="${actionBean.advanced}" />
            <mde:write name="actionBean" property="jndiCPForm"/>
            <mde:write name="actionBean" property="jdbcCPForm"/>
        </div>
    </stripes:layout-component>
    <stripes:layout-component name="contentFooter">
        <portofino:buttons list="select-user-fields" cssClass="contentButton" />
    </stripes:layout-component>
</stripes:layout-render>