<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>


<%@ page import="com.liferay.portal.model.Group" %>
<%@ page import="com.liferay.portal.model.MembershipRequest" %>
<%@ page import="com.liferay.portal.model.Role" %>
<%@ page import="com.liferay.portal.service.GroupLocalServiceUtil" %>
<%@ page import="com.liferay.portal.service.MembershipRequestLocalServiceUtil" %>
<%@ page import="com.liferay.portal.service.RoleLocalServiceUtil" %>
<%@ page import="com.liferay.portal.util.PortalUtil" %>
<%@ page import="com.liferay.portal.util.PortletKeys" %>
<%@ page import="java.lang.StringBuilder" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>

<liferay-theme:defineObjects />
<portlet:defineObjects />

<%
 // Definition of used Variables:
LinkedHashMap<String, Object> groupParams = new LinkedHashMap<String, Object>();
groupParams.put("active", Boolean.TRUE);

long companyId = themeDisplay.getCompanyId(); //ID of the portal
List<Group> results = GroupLocalServiceUtil.search(companyId, null, null, groupParams, 0, 20);
Collections.sort(results, new Comparator<Group>(){
	public int compare(Group g1, Group g2){
		return g1.getDescriptiveName().compareTo(g2.getDescriptiveName());
	}
});


int publicLayoutsPageCount = 0;
int privateLayoutsPageCount = 0;
String serverName = themeDisplay.getServerName();
String serverPort = String.valueOf(themeDisplay.getServerPort());

// The Address for the ADD Picture
String addressOfPictureAdd = "/scy-theme/images/common/assign.png";

//The Address for the InProgress Picture
String addressOfPictureProgress= "/scy-theme/images/common/view.png";	

//The Name of the Role which is used for Filtering.
String roleName = "SPGroup"; 

StringBuilder sBuilder = new StringBuilder();
String userName = themeDisplay.getUser().getFirstName().toLowerCase();

%>



<%
/**
* If an ask membership button is pressed, the associated group name and group id are set as parameters in the form.
*/
%>
<script type="text/javascript">
	function requestMembership(group,id){
    	document.formular.groupName.value = group;
    	document.formular.groupId.value = id;
    	document.formular.submit();
    }    
</script>

<% 
/**
* Processing of membership requests of the askmembership.jsp.
*/
long userId = themeDisplay.getUserId();
if(request.getParameter("button") != null) {
	String button = request.getParameter("button");
	if(button.equals("submit")){
		String community = request.getParameter("community");
	    long groupId = Long.parseLong(community);
	    String comments = request.getParameter("comment");
	    if(comments != null && comments.length()>0){    	
		    MembershipRequestLocalServiceUtil.addMembershipRequest(userId, groupId, comments);  	
	    } else { 
	    	//if there is no comment
%>
			<span class="portlet-msg-error"><liferay-ui:message key="errorMissingComment"/></span>
<%	
	    }
	}
}
%>

<%
/**
* Definition of the view.
*/
%> 
<FORM NAME="formular" METHOD="POST" action="<portlet:renderURL><portlet:param name="jspPage" value="/askmembership.jsp" /></portlet:renderURL>">
<INPUT type="HIDDEN" name="groupName" value="">
<INPUT type="HIDDEN" name="groupId" value="" />
<%
sBuilder.append("<ul>");

// If the User is not the Default User, then proceed with generating Links to the Personal Site and Communities.
if(!themeDisplay.getUser().isDefaultUser()){
	//Add Personal-Site-Link.
	sBuilder.append("<li>");
	sBuilder.append("<a href=\"");
	sBuilder.append("http://");
	sBuilder.append(serverName);
	sBuilder.append(":");
	sBuilder.append(serverPort);
	sBuilder.append("/web/");
	sBuilder.append(userName);
	sBuilder.append("/personal-site");
	sBuilder.append("\">");
	sBuilder.append("Personal Site");
	sBuilder.append("</a>");
	sBuilder.append("</li>");	
	%>
		<p><%= sBuilder.toString() %></p>
	<%	
	/**
	* For each community add a div for the icon and a link to the public pages.
	*/	
	for (int i = 0; i < results.size(); i++) {
		
		Group group = (Group)results.get(i);
		
		/**
		* Check if the community has the right role  
		*/
		boolean isOIGroup = false;
		
		List<Role> roles = RoleLocalServiceUtil.getGroupRoles(group.getGroupId());
		for(int j = 0; j < roles.size(); j++){
			Role role = (Role) roles.get(j);
			if (role.getName().equals(roleName)){ 
				isOIGroup = true;
			}
		}
		
		publicLayoutsPageCount = group.getPublicLayoutsPageCount();
		privateLayoutsPageCount = group.getPrivateLayoutsPageCount();
		
		if (isOIGroup) {		
			
			StringBuilder sb = new StringBuilder();		
			/* 
				Grouptypes are:
					
				1 = OPEN
				2 = RESTRICTED	
				3 = PRIVATE
				
				and can be proven width "group.getType()".
			*/
			
			// If the User is a Member and there exists Private Sites, then LINK to these Sites!
			if(GroupLocalServiceUtil.hasUserGroup(userId, group.getGroupId()) ){
							
				// Link to public and private Sites, if both exists.
				if((publicLayoutsPageCount > 0) & (privateLayoutsPageCount > 0)){
					
					sb.append("<li>");
					
					// The Name of the Group.
					sb.append(group.getDescriptiveName());
				
					sb.append("<ul>");
					
					// Link to Open Sites.
					sb.append("<li>");
					sb.append("<a href=\"");
					sb.append(group.getPathFriendlyURL(false, themeDisplay));
					sb.append(group.getFriendlyURL());
					sb.append("\">");		
					%>
						<%= sb.toString() %>
					<%
					sb.delete(0, sb.length());
					%>
						<liferay-ui:message key="public"/>					
					<%
					sb.append("</a>"); 			
					sb.append("</li>");
					
					// Link to private Sites.
					sb.append("<li>");
					sb.append("<a href=\"");
					sb.append("http://");
					sb.append(serverName);
					sb.append(":");
					sb.append(serverPort);
					sb.append("/web/general-subenvironment/subscriptions?p_p_id=29&p_p_lifecycle=1&p_p_state=normal&p_p_mode=view&p_p_col_id=column-2&p_p_col_count=1&_29_struts_action=%2Fcommunities%2Fpage&_29_groupId=");
					sb.append(group.getGroupId());
					sb.append("&_29_redirect=%2Fweb%2Fgeneral-subenvironment%2Fsubscriptions&_29_privateLayout=true");
					sb.append('"');
					sb.append(">");
					%>
						<%= sb.toString() %>
					<%
					sb.delete(0, sb.length());
					%>
						<liferay-ui:message key="private"/>					
					<%
					sb.append("</a>"); 	
					sb.append("</li>");
					
					sb.append("</ul>");
					sb.append("</li>");	
					%>
						<%= sb.toString() %>
					<%
				}
				else{
					
					if(privateLayoutsPageCount > 0){
						
					// Link to private Sites.
					sb.append("<li>");
					sb.append("<a href=\"");
					sb.append("http://");
					sb.append(serverName);
					sb.append(":");
					sb.append(serverPort);
					sb.append("/web/general-subenvironment/subscriptions?p_p_id=29&p_p_lifecycle=1&p_p_state=normal&p_p_mode=view&p_p_col_id=column-2&p_p_col_count=1&_29_struts_action=%2Fcommunities%2Fpage&_29_groupId=");
					sb.append(group.getGroupId());
					sb.append("&_29_redirect=%2Fweb%2Fgeneral-subenvironment%2Fsubscriptions&_29_privateLayout=true");
					sb.append('"');
					sb.append(">");
					sb.append(group.getDescriptiveName());
					sb.append("</a>");	
					sb.append("</li>");
					%>
						<%= sb.toString() %>
					<%
					sb.delete(0, sb.length());
					}
					else{					
						// Link to Open Sites.
						sb.append("<li>");
						sb.append("<a href=\"");
						sb.append(group.getPathFriendlyURL(false, themeDisplay));
						sb.append(group.getFriendlyURL());
						sb.append("\">");
						sb.append(group.getDescriptiveName());
						sb.append("</a>");
						sb.append("</li>");
						%>
							<%= sb.toString() %>
						<%
					}
				}		
			}
			else {
				
				/**
				* If the current user is not member of the community, check if there is an active membership request for her/him. 
				*/
				int status = 0; //asked for assignment
				int mrCount = MembershipRequestLocalServiceUtil.searchCount(group.getGroupId(), status);
				List<MembershipRequest> mrs = MembershipRequestLocalServiceUtil.search(group.getGroupId(), status, 0, mrCount);
				boolean membershipRequest = false;
				for(int j = 0; j < mrs.size(); j++){
					MembershipRequest mr = mrs.get(j);
					if(mr.getUserId() == userId){
						membershipRequest = true;
					}
				}
				
				/**
				* If the User is NOT a Member 
				* and there exists Public Sites
				* and the Community is not a Private one, then LINK to these Sites!
				*/
				if(group.getType()!=3 & publicLayoutsPageCount > 0){
					
					sb.append("<li>");
					
					// The Name of the Group.
					sb.append("<font  	color=\"#BEBEBE\">");
					sb.append(group.getDescriptiveName()+"		");
					sb.append("</font>");	
					
					
					/**
					* If there is an active membership request for the current user, print this information. 
					*/
					if(membershipRequest){
						// Add Icon for requested Membership
						sb.append("<img src=");
						sb.append('"'+addressOfPictureProgress);
						sb.append('"'+" alt=");
						sb.append('"');
						%>
							<%= sb.toString() %>
						<%
						sb.delete(0, sb.length());
						%><liferay-ui:message key="askedMembership"/><%
						sb.append('"'+" title=");
						sb.append('"');	
						%>
							<%= sb.toString() %>
						<%
						sb.delete(0, sb.length());
						%><liferay-ui:message key="askedMembership"/><%
						sb.append('"'+"/></a>");
					}
					else{
									
						// Add Icon for request Membership
						sb.append("<a href=");
						sb.append('"'+"#"+'"');
						sb.append("ONCLICK=");
						sb.append('"');
						sb.append("requestMembership(");
						sb.append("'");
						sb.append(group.getName());
						sb.append("'");
						sb.append(",");
						sb.append("'");
						sb.append(group.getGroupId());
						sb.append("'");
						sb.append(")");
						sb.append('"');
						sb.append(" ><img src=");
						sb.append('"'+addressOfPictureAdd);
						sb.append('"'+" alt=");		
						sb.append('"');
						%>
							<%= sb.toString() %>
						<%
						sb.delete(0, sb.length());						
						%><liferay-ui:message key="askForMembership"/><%
						sb.append('"'+" title=");
						sb.append('"');	
						%>
							<%= sb.toString() %>
						<%
						sb.delete(0, sb.length());
						%><liferay-ui:message key="askForMembership"/><%
						sb.append('"'+"/></a>");
					}
					
					// Link to public Sites.
					sb.append("<ul>");
					sb.append("<li>");
					sb.append("<a href=\"");
					sb.append(group.getPathFriendlyURL(false, themeDisplay));
					sb.append(group.getFriendlyURL());
					sb.append("\">");
					%>
						<%= sb.toString() %>
					<%
					sb.delete(0, sb.length());	
					%>
						<liferay-ui:message key="public"/>					
					<%
					sb.append("</a>");	
					sb.append("</li>");
					sb.append("</ul>");
					sb.append("</li>");
					%>
						<%= sb.toString() %>
					<%
				}
				else{
					// If the Community is NOT Private!
					if(group.getType()!=3){ 
						sb.append("<li>");					
						sb.append("<font  	color=\"#BEBEBE\">");
						sb.append(group.getDescriptiveName()+"		");
						sb.append("</font>");	
						/**
						* If there is an active membership request for the current user, print this information. 
						*/
						if(membershipRequest){
							// Add Icon for requested Membership
							sb.append("<img src=");
							sb.append('"'+addressOfPictureProgress);
							sb.append('"'+" alt=");
							sb.append('"');
							%>
								<%= sb.toString() %>
							<%
							sb.delete(0, sb.length());
							%><liferay-ui:message key="askedMembership"/><%
							sb.append('"'+" title=");
							sb.append('"');	
							%>
								<%= sb.toString() %>
							<%
							sb.delete(0, sb.length());
							%><liferay-ui:message key="askedMembership"/><%
							sb.append('"'+"/></a>");
							sb.append("</li>");
						}
						else{
							// Add Icon for request Membership
							sb.append("<a href=");
							sb.append('"'+"#"+'"');
							sb.append("ONCLICK=");
							sb.append('"');
							sb.append("requestMembership(");
							sb.append("'");
							sb.append(group.getName());
							sb.append("'");
							sb.append(",");
							sb.append("'");
							sb.append(group.getGroupId());
							sb.append("'");
							sb.append(")");
							sb.append('"');
							sb.append(" ><img src=");
							sb.append('"'+addressOfPictureAdd);
							sb.append('"'+" alt=");		
							sb.append('"');
							%>
								<%= sb.toString() %>
							<%
							sb.delete(0, sb.length());						
							%><liferay-ui:message key="askForMembership"/><%
							sb.append('"'+" title=");
							sb.append('"');	
							%>
								<%= sb.toString() %>
							<%
							sb.delete(0, sb.length());
							%><liferay-ui:message key="askForMembership"/><%
							sb.append('"'+"/></a>");
							sb.append("</li>");
						}
						sb.append("</li>");
						%>
							<%= sb.toString() %>
						<%
					}
				}
			}
		}
	}
}
%>
</ul>
<!--  One-Site-Back -->
	<br></br>
	<a href="javascript:history.back(-1)">
	<% sBuilder.delete(0, sBuilder.length());%>
	<liferay-ui:message key="back"/>
	</a>  
<!--  One-Site-Back -->
</FORM>