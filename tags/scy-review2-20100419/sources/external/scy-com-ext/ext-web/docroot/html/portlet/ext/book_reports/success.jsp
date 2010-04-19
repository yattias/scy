<%@ include file="/html/portlet/ext/book_reports/init.jsp" %>
<% String title = request.getParameter("title"); %>
<div style="text-align: center" >
  Success! Book Title: <i><%= title %></i>
</div>
<br/>
  <a href="<portlet:renderURL><portlet:param name="struts_action"
    value="/ext/book_reports/view" /></portlet:renderURL>">Back to
    View Page</a>
<br/>