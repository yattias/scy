<%@ include file="/html/portlet/ext/freestyler/init.jsp" %>

<%
String redirect = currentURL;
Long folderId = 10522l;
long groupId = themeDisplay.getPortletGroupId();
String name = portletDisplay.getRootPortletId();
String primKey = portletDisplay.getResourcePK();
boolean showAddEntryButton = permissionChecker.hasPermission(groupId, name, primKey, "ADD_FREESTYLER");
boolean showPermissionsButton = GroupPermissionUtil.contains(permissionChecker, scopeGroupId, ActionKeys.PERMISSIONS);
boolean showEditEntryPermissions = true;
boolean showView = FreestylerPermission.contains(permissionChecker, scopeGroupId, "VIEW");
String modelResource = "com.ext.portlet.freestyler";
String modelResourceDescription = themeDisplay.getScopeGroupName();
String resourcePrimKey = String.valueOf(scopeGroupId);


int total = 0;

if (true) {
	total = FreestylerEntryLocalServiceUtil.getGroupEntriesCount(scopeGroupId);
}
else {
	total = 0;
}

List results = null;

if (true) {
	results = FreestylerEntryLocalServiceUtil.getGroupEntries(scopeGroupId);
}else{
	results = new ArrayList<FreestylerEntry>();
}



%>

<div style="text-align: center" >
 Freestyler Portlet
 
 <br/><br/>
</div>




<c:if test="<%= showAddEntryButton || showPermissionsButton %>">
	<div style="text-align:right;">
		<c:if test="<%= showPermissionsButton %>">
			<liferay-security:permissionsURL
				modelResource="<%= modelResource %>"
				modelResourceDescription="<%= HtmlUtil.escape(modelResourceDescription) %>"
				resourcePrimKey="<%= resourcePrimKey %>"
				var="permissionsURL"
			/>
			<input type="button" value="<liferay-ui:message key="permissions" />" onClick="location.href = '<%= permissionsURL %>';" />
		</c:if>
		<c:if test="<%= showAddEntryButton %>">
			<input type="button" value="<liferay-ui:message key="add-freestyler" />" onClick="location.href = '<portlet:renderURL windowState="<%= WindowState.MAXIMIZED.toString() %>"><portlet:param name="struts_action" value="/ext/freestyler/add_free" /><portlet:param name="redirect" value="<%= currentURL %>" /><portlet:param name="folderId" value="<%= String.valueOf(folderId) %>" /></portlet:renderURL>';" />
		</c:if>
	</div>
	<br/>
</c:if>


<%
for (int i = 0; i < results.size(); i++) {
	FreestylerEntry entry = (FreestylerEntry)results.get(i);

	TagsUtil.addLayoutTagsEntries(request, TagsEntryLocalServiceUtil.getEntries(FreestylerEntry.class.getName(), entry.getFreestylerId(), true));
%>

	<%@ include file="/html/portlet/ext/freestyler/view_entry_content.jspf" %>
	
	<br/>

	<div class="separator"><!-- --></div>

<%
}
%>



