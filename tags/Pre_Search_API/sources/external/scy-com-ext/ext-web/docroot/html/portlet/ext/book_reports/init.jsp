<%@ include file="/html/common/init.jsp" %>
<%@ page import="com.ext.portlet.bookreports.model.BookReportsEntry" %>
<%@ page import="com.ext.portlet.bookreports.action.BookLocalServiceUtil" %>
<%@ page import="java.util.List" %>
<portlet:defineObjects />
<%
PortletPreferences preferences = renderRequest.getPreferences();
String currentURL = PortalUtil.getCurrentURL(request);
%>