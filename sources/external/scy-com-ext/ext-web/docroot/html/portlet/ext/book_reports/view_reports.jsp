<%@ include file="/html/portlet/ext/book_reports/init.jsp" %>
<% List reports = (List)request.getAttribute("reports"); %>
<div> <liferay-ui:message key="view-reports" />
<% for (int i = 0; i < reports.size(); i++){
	String reportName = (String)reports.get(i); 
	%><%= reportName %><br><% 
} %>
</div>	



