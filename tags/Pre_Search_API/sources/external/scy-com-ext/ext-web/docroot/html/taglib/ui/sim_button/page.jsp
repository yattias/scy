<%@ include file="/html/taglib/init.jsp" %>

<%
String entryId = (String)request.getAttribute("liferay-ui-ext:sim-button:classPK");
PortletURL portletURL = (PortletURL)request.getAttribute("liferay-ui-ext:sim-button:portletURL");
String struts_action = (String)request.getAttribute("liferay-ui-ext:sim-button:strutsAction");
String className = (String)request.getAttribute("liferay-ui-ext:sim-button:className");

String onClickHtml = StringPool.BLANK;
onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";

%>
<!--  remove sim button 
					<liferay-portlet:actionURL portletName="similarity" windowState="<%= WindowState.MAXIMIZED.toString() %>" var="myIPCUrl" >
		                <portlet:param name="struts_action" value="<%= struts_action %>" />    
		                <portlet:param name="entryId" value="<%= entryId %>" />      
						<portlet:param name="ipc" value="true" />
						<portlet:param name="className" value="<%= className %>" />						
					</liferay-portlet:actionURL>
		
					<a href="<%= myIPCUrl.toString() %>"<%= onClickHtml %>><input type="button" value="SIM"></a>
-->