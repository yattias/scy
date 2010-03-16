<%@ include file="/html/common/init.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.liferay.portal.*"%>

<portlet:defineObjects />
<%
	PortletPreferences preferences = renderRequest.getPreferences();
	String currentURL = PortalUtil.getCurrentURL(request);
%>