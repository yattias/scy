<%@ include file="/html/portlet/ext/missionhandling/init.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%@page import="com.liferay.portal.model.Organization"%>
<%@page import="com.liferay.portal.service.OrganizationLocalServiceUtil"%>

<%
String struts_action = (String)request.getAttribute("struts_action");
String redirect = (String)request.getAttribute("redirect");
String errorMessage = (String)request.getAttribute("errorMessage");
Organization organization = (Organization)request.getAttribute("organization");
String onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";
%>

<c:if test="<%= errorMessage != null %>">
	<span class="portlet-msg-error">
		<%= errorMessage %>
	</span>
</c:if>

<div align="right">
	&laquo; <a href="<%= HtmlUtil.escape(redirect) %>"<%= onClickHtml %>><liferay-ui:message key="back" /></a>
</div>


<div>
	Add mission entry <b> <%= organization.getName() %> </b> to deactivate scheduler. 
	<br />
	<form action="
		<portlet:actionURL>
			<portlet:param name="struts_action" value="/ext/mission/add_missionEntry" />
		    <portlet:param name="cmd" value="send" />						        
		    <portlet:param name="redirect" value="<%= HtmlUtil.escape(redirect) %>" />       
		    <portlet:param name="organizationId" value="<%= String.valueOf(organization.getOrganizationId()) %>" />        
		</portlet:actionURL>" method="post" name="<portlet:namespace />addMissionEntry">
         
        Deactivate the mission in                    
		<input type=text name="zahlen" size="3" maxlength="3" > days, if no days entered the mission will deactivate now. 
		<br />
		<input type=button value="send" onClick="submitForm(document.<portlet:namespace />addMissionEntry);">
	</form> 

</div>

