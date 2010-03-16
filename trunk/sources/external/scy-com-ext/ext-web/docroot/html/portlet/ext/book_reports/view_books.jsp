<%@ include file="/html/portlet/ext/book_reports/init.jsp" %>
<% List books = (List) BookLocalServiceUtil.getAll(); 
  BookReportsEntry book = null; %>
<table style="border: 1px solid #CCC; width: 100%; padding: 5px;">
  <tr>
    <td style="color: #12558E;"> Book ID</td>
    <td style="color: #12558E;"> Group ID</td>
    <td style="color: #12558E;"> Company ID</td>
    <td style="color: #12558E;"> User ID</td>
    <td style="color: #12558E;"> User Name</td>
    <td style="color: #12558E;"> Title</td>
    <td style="color: #12558E;"> Actions</td>
  </tr>
  <c:if test="<%= books != null %>">
  <% for (int i=0; i < books.size(); i++){
  book = (BookReportsEntry) books.get(i); %>
  <tr>
    <td><%= book.getEntryId() %></td>
    <td><%= book.getGroupId() %></td>
    <td><%= book.getCompanyId() %></td>
    <td><%= book.getUserId() %></td>
    <td><%= book.getUserName() %></td>
    <td><%= book.getTitle() %></td>
    <td>
   <%@ include file="entry_action.jspf" %>
    </td>
  </tr>
  <% } %> </c:if>
</table>
<br/>
  <a href="<portlet:renderURL><portlet:param name="struts_action"
    value="/ext/book_reports/view" /></portlet:renderURL>">Back to
    View Page</a>
<br/>