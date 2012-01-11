<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="actionBean" scope="request"
     type="com.manydesigns.portofino.actions.PortletAction"/>
<button onclick="showMovePageDialog(
                    '<%= actionBean.dispatch.getLastPageInstance().getPage().getId() %>',
                    '<%= request.getContextPath() %>');
                return false;"
        type="submit"
        class="transferthick-e-w ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only"
        role="button" aria-disabled="false"
        title="Move page">
    <span class="ui-button-icon-primary ui-icon ui-icon-transferthick-e-w"></span>
    <span class="ui-button-text">Move page</span>
</button>