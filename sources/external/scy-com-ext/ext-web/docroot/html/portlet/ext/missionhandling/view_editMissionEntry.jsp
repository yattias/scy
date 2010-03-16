<%@ include file="/html/portlet/ext/missionhandling/init.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%@ page import="com.ext.portlet.missionhandling.model.MissionEntry"%>
<%@ page import="com.ext.portlet.missionhandling.service.MissionEntryLocalServiceUtil"%>

<%
String struts_action = (String)request.getAttribute("struts_action");
String redirect = (String)request.getAttribute("redirect");
String errorMessage = (String)request.getAttribute("errorMessage");
MissionEntry missionEntry = (MissionEntry)request.getAttribute("missionEntry");
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
	Change mission entry <b> <%= missionEntry.getMissionEntryId() %> </b> 
	<br />
	<form action="
		<portlet:actionURL>
			<portlet:param name="struts_action" value="/ext/mission/edit_missionEntry" />
		    <portlet:param name="cmd" value="send" />						        
		    <portlet:param name="redirect" value="<%= HtmlUtil.escape(redirect) %>" />       
		    <portlet:param name="missionEntryId" value="<%= String.valueOf(missionEntry.getMissionEntryId()) %>" />        
		</portlet:actionURL>" method="post" name="<portlet:namespace />editMissionEntry">
         
        Set days <input type=text name="zahlen" size="3" maxlength="3" > in future from current day where this mission set inactive. <br/>
        If days set to 0 or left field empty the scheduler entry of missionEntry will be delete. (the mission will be active with no endDate !!!)            
		<br />
		<input type=button value="send" onClick="submitForm(document.<portlet:namespace />editMissionEntry);">
	</form> 

</div>

