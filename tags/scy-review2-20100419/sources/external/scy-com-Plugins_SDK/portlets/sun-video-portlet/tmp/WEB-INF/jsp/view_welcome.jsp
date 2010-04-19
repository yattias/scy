<%@ page import="com.sun.portal.videoportlet.*" %>
<%@ page import="com.sun.portal.videoportlet.util.*" %>
<%@ page import="java.util.*" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<portlet:defineObjects/>


<%
   User user = (User)renderRequest.getPortletSession().getAttribute(Constants.SESSION_USER);
   List list = (List)renderRequest.getPortletSession().getAttribute(Constants.SESSION_FAVS);
   
   Paginator paginator=(Paginator)renderRequest.getPortletSession().getAttribute("pagination");
  
   List currentlist=paginator.getCurrentPage();
   System.out.println("currentlist"+currentlist);
   

%>
<p>
Welcome <%=user.getFirstName()%> <%=user.getLastName()%> (<a href='<portlet:actionURL>
               <portlet:param name="<%=Constants.PORTLET_ACTION%>" value="<%=Constants.ACTION_CHANGEUSER%>"/>
            </portlet:actionURL>'>change user</a>)
</p>
<p>
    You are currently viewing <b><%=user.getFirstName()%>'s Favorites:</b>
</p>
<table cellpadding="2" cellspacing="2" align="center" style="portlet-section-header">
<%
for(int i=0; i<currentlist.size(); i++){
       Video vid = (Video)currentlist.get(i);
%>
<tr> 

<td><%=vid.getTitle()%></td>
<!--
<td><img src='<%=vid.getThumbnailURL()%>'></td>
-->
<td><a href='<portlet:actionURL>
             <portlet:param name="<%=Constants.PORTLET_ACTION%>" value="<%=Constants.ACTION_VIEW_VIDEO%>"/>
             <portlet:param name="<%=Constants.PARAM_VIDEO_ID%>" value="<%=vid.getId()%>"/>
             </portlet:actionURL>'><img src='<%=vid.getThumbnailURL()%>'></a></td>

</tr>       
<%       
}
%>
</table>
<table align="center">
    <tr>
        
        <td>
            <%
            if((paginator.getCurrentPageNumber())!=1){
            System.out.println("INprevious");
            %>
            
            <a href='<portlet:actionURL>
               <portlet:param name="<%=Constants.PORTLET_ACTION%>" value="<%=Constants.ACTION_PREVIOUS%>"/>
            </portlet:actionURL>'>Previous</a>
            <% } %>
        </td>  
        <td>
            <%
            if(((paginator.getCurrentPageNumber())!=(paginator.getNumberOfPages()))&&((paginator.getNumberOfPages())!=0)){
            System.out.println("INnext");
            %>
            <a href='<portlet:actionURL>
               <portlet:param name="<%=Constants.PORTLET_ACTION%>" value="<%=Constants.ACTION_NEXT%>"/>
            </portlet:actionURL>'>Next</a>
            <% } %>
    </td></tr>
</table>
