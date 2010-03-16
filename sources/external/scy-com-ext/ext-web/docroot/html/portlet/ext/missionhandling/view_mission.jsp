<%@ include file="/html/portlet/ext/missionhandling/init.jsp"%>

<%@ page import="com.liferay.portlet.tags.model.TagsAsset"%>
<%@ page import="com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>


<%@page import="com.ext.portlet.missionhandling.model.MissionEntry"%>
<%@page import="com.ext.portlet.missionhandling.service.MissionEntryLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.Group"%>
<%@page import="com.liferay.portal.model.Organization"%>
<%@page import="com.liferay.portal.service.GroupLocalServiceUtil"%>
<%@page import="com.liferay.portal.service.OrganizationLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.User"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.UserGroupRole"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.Role"%>
<%@page import="com.liferay.portal.service.RoleLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.RoleConstants"%>
<%@page import="com.liferay.portal.service.UserGroupRoleLocalServiceUtil"%>

<div>
<h2>Portal Missions:</h2>
<br />
</div>

<table class="sortable">
	<tr class="portlet-section-header results-header">
		<th width=300>
			<liferay-ui:message key="Mission" />
		</th>
		<th>Status</th>
		<th>Start Date</th>
		<th>End Date</th>
		<th>Change state</th>
	</tr>


<%
	String onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";
	String style = "class=\"portlet-section-body results-row\" onmouseover=\"this.className = 'portlet-section-body-hover results-row hover';\" onmouseout=\"this.className = 'portlet-section-body results-row';\"";

	List<Role> roleList;
	long companyId;
	List<Organization> organizationsList;
	List<Group> companyOrganizationsGroupsList;
	boolean activeOrg;
	boolean hasEndDate;
	try {
		companyId = 10113;
		organizationsList = OrganizationLocalServiceUtil.getOrganizations(0, OrganizationLocalServiceUtil.getOrganizationsCount());
		Role roleForOrganizationActiveMembers = RoleLocalServiceUtil.getRole(companyId, RoleConstants.ORGANIZATION_ACTIVE_MEMBERS);
	
		
		for (Organization organization : organizationsList) {
			if(organization.getCompanyId() == companyId){
				Group companyOrganizationsGroup =  GroupLocalServiceUtil.getOrganizationGroup(companyId, organization.getOrganizationId());
				List<User> orzanizationUserList = UserLocalServiceUtil.getOrganizationUsers(organization.getOrganizationId());
				List<UserGroupRole> organizationActiveUserList = UserGroupRoleLocalServiceUtil.getUserGroupRolesByGroupAndRole(companyOrganizationsGroup.getGroupId(), roleForOrganizationActiveMembers.getRoleId());
%>
				<tr <%= style %>>
					<td>  
						<%= organization.getName() %>
					</td>			
<%
					Boolean hasNoSchedulerEntry = null;
					TagsAsset asset = null;
					if(companyOrganizationsGroup != null){
						long missionEntryId = 0l;
						MissionEntry missionEntry = null;
						asset = TagsAssetLocalServiceUtil.getAsset(companyOrganizationsGroup.getClassName(), companyOrganizationsGroup.getClassPK());					
						List<MissionEntry> missionEntryList = MissionEntryLocalServiceUtil.getMissionEntries(0, MissionEntryLocalServiceUtil.getMissionEntriesCount());
						for(MissionEntry entry : missionEntryList){
							if(entry.getOrganizationId() == organization.getOrganizationId()){
								missionEntry = entry;
							}
							
						}
							
						if(missionEntry == null){
							hasNoSchedulerEntry = true;
						}else{
							hasNoSchedulerEntry = false;
							
							if(missionEntry.getEndDate() == null || missionEntry.getEndDate().toString().length() <= 0){
								hasEndDate = false;								
							}else{
								hasEndDate = true;
							}

							
						
%>
							<c:if test="<%= missionEntry.isActive() && hasEndDate %>">
								<portlet:actionURL var="viewChangeMissionEntry">
						            <portlet:param name="struts_action" value="/ext/mission/edit_missionEntry" />
						            <portlet:param name="cmd" value="edit" />						        
						          	<portlet:param name="redirect" value="<%= HtmlUtil.escape(currentURL) %>" />       
						          	<portlet:param name="missionEntryId" value="<%= String.valueOf(missionEntry.getMissionEntryId()) %>" />          	
								</portlet:actionURL>
					
					
								<td> 			
									active		
								</td>	
								<td ALIGN="center">
									<c:if test="<%= missionEntry.getCreateDate() != null %>">  
										<fmt:formatDate value="<%= missionEntry.getCreateDate() %>" type="date" pattern="dd-MMM-yyyy"/> 
									</c:if>
								</td>
								<td ALIGN="center">
									<c:if test="<%= missionEntry.getEndDate() != null %>">  
										<fmt:formatDate value="<%= missionEntry.getEndDate() %>" type="date" pattern="dd-MMM-yyyy"/> 
									</c:if>
								</td>
								<td ALIGN="center"> 
									<a href="<%= viewChangeMissionEntry %>" <%= onClickHtml %>>
									         change
									</a> 				
								</td>
							</c:if>
							<c:if test="<%= missionEntry.isActive() && !hasEndDate %>">
			
						      	<portlet:actionURL var="viewAddMissionEntry2">
						            <portlet:param name="struts_action" value="/ext/mission/add_missionEntry" />
						            <portlet:param name="cmd" value="view" />						        
						          	<portlet:param name="redirect" value="<%= HtmlUtil.escape(currentURL) %>" />       
						          	<portlet:param name="organizationId" value="<%= String.valueOf(organization.getOrganizationId()) %>" />   
						        </portlet:actionURL>
					
					
								<td> 			
									active		
								</td>	
								<td ALIGN="center">
									<c:if test="<%= asset.getCreateDate() != null %>">  
										<fmt:formatDate value="<%= asset.getCreateDate() %>" type="date" pattern="dd-MMM-yyyy"/> 
									</c:if>
								</td>
								<td ALIGN="center">
									-
								</td>
								<td ALIGN="center"> 
									<a href="<%= viewAddMissionEntry2 %>" <%= onClickHtml %>>
									         set deactivate time
									</a> 				
								</td>
							</c:if>
							<c:if test="<%= !missionEntry.isActive()  %>">
							
						      	<portlet:actionURL var="activateMissionEntry">
						            <portlet:param name="struts_action" value="/ext/mission/edit_missionEntry" />
						            <portlet:param name="cmd" value="activate" />						        
						          	<portlet:param name="redirect" value="<%= HtmlUtil.escape(currentURL) %>" />       
						          	<portlet:param name="organizationId" value="<%= String.valueOf(organization.getOrganizationId()) %>" />          	
						          	<portlet:param name="missionEntryId" value="<%= String.valueOf(missionEntry.getMissionEntryId()) %>" />          	
								</portlet:actionURL>
							
								<td> 			
									inactive		
								</td>	
								<td>									
								</td>
								<td>								
								</td>
								<td ALIGN="center"> 
									<a href="<%= activateMissionEntry %>" <%= onClickHtml %>>
									        activate
									</a> 				
								</td>			
							</c:if>
<%							
						}
					}

%>
		
					<c:if test="<%= hasNoSchedulerEntry %>">
			
				      	<portlet:actionURL var="viewAddMissionEntry">
				            <portlet:param name="struts_action" value="/ext/mission/add_missionEntry" />
				            <portlet:param name="cmd" value="view" />						        
				          	<portlet:param name="redirect" value="<%= HtmlUtil.escape(currentURL) %>" />       
				          	<portlet:param name="organizationId" value="<%= String.valueOf(organization.getOrganizationId()) %>" />          	
						</portlet:actionURL>
			
			
						<td> 			
							active		
						</td>	
						<td ALIGN="center">
							<c:if test="<%= asset.getCreateDate() != null %>">  
								<fmt:formatDate value="<%= asset.getCreateDate() %>" type="date" pattern="dd-MMM-yyyy"/> 
							</c:if>
						</td>
						<td ALIGN="center">
							-
						</td>
						<td ALIGN="center"> 
							<a href="<%= viewAddMissionEntry %>" <%= onClickHtml %>>
							         set deactivate time
							</a> 				
						</td>
					</c:if>				
				</tr>	
	
<%	
			}
		}
	} catch (SystemException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
%>
</table>

