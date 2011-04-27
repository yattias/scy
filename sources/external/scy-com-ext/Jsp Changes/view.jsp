<%
/**
 * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
%>

<%@ include file="/html/portlet/communities/init.jsp" %>

<%
String tabs1 = ParamUtil.getString(request, "tabs1", "communities-joined");

boolean showTabs1 = true;

if (portletName.equals(PortletKeys.ENTERPRISE_ADMIN_COMMUNITIES)) {
	if (permissionChecker.isCompanyAdmin()) {
		tabs1 = "all-communities";
	}
	else {
		tabs1 = "communities-joined";
	}

	showTabs1 = false;
}

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setWindowState(WindowState.MAXIMIZED);

portletURL.setParameter("struts_action", "/communities/view");
portletURL.setParameter("tabs1", tabs1);

pageContext.setAttribute("portletURL", portletURL);
%>



<liferay-ui:success key="membership_request_sent" message="your-request-was-sent-you-will-receive-a-reply-by-email" />

<form action="<%= portletURL.toString() %>" method="get" name="<portlet:namespace />fm">
<liferay-portlet:renderURLParams varImpl="portletURL" />

<c:choose>
	<c:when test="<%= showTabs1 %>">
		<liferay-ui:tabs
			names="communities-joined,available-communities,communities-owned"
			url="<%= portletURL.toString() %>"
		/>
	</c:when>
	<c:otherwise>
		<liferay-util:include page="/html/portlet/communities/toolbar.jsp">
			<liferay-util:param name="toolbarItem" value="view-all" />
		</liferay-util:include>
	</c:otherwise>
</c:choose>

<%
GroupSearch searchContainer = new GroupSearch(renderRequest, portletURL);
%>

<!-- Original value of showAddButton is "<%= showTabs1 %>" -->
<liferay-ui:search-form
	page="/html/portlet/enterprise_admin/group_search.jsp"
	searchContainer="<%= searchContainer %>"
	showAddButton="" 
/>

<!-- The following lines are made due to the changed value of showAddButton("choose"). -->
<c:choose>
	<c:when test="<%= request.isUserInRole(\"administrator\")%>">
		<div><input type="button" onclick="_29_addGroup();" value="Add Project"></div>
		<p><span style="color:red">Only Visible for Admins:</span><br>If other Roles then <span style="color:green">Administrators</span> or <span style="color:green">CommunityCreators</span> or <span style="color:green">Project Leader</span> get the Permission to ADD Community's then change this File under:"liferay-portal-5.2.3/tomcat-6.0.18/webapps/ROOT/html/portlet/communities/view.jsp"</p>
	</c:when>
	<c:when test="<%= request.isUserInRole(\"communitycreator\")%>">
		<div><input type="button" onclick="_29_addGroup();" value="Add Project"></div>
	</c:when>
	<c:when test="<%= request.isUserInRole(\"project leader\")%>">
		<div><input type="button" onclick="_29_addGroup();" value="Add Project"></div>
	</c:when>
	<c:otherwise>
	</c:otherwise>
</c:choose>


<c:if test="<%= true %>">

	<%
	GroupSearchTerms searchTerms = (GroupSearchTerms)searchContainer.getSearchTerms();

	LinkedHashMap groupParams = new LinkedHashMap();

	if (tabs1.equals("communities-owned")) {
		Role role = RoleLocalServiceUtil.getRole(company.getCompanyId(), RoleConstants.COMMUNITY_OWNER);

		List userGroupRole = new ArrayList();

		userGroupRole.add(new Long(user.getUserId()));
		userGroupRole.add(new Long(role.getRoleId()));

		groupParams.put("userGroupRole", userGroupRole);
		//groupParams.put("active", Boolean.TRUE);
	}
	else if (tabs1.equals("communities-joined")) {
		groupParams.put("usersGroups", new Long(user.getUserId()));
		//groupParams.put("active", Boolean.TRUE);
	}
	else if (tabs1.equals("available-communities")) {
		List types = new ArrayList();

		types.add(new Integer(GroupConstants.TYPE_COMMUNITY_OPEN));
		types.add(new Integer(GroupConstants.TYPE_COMMUNITY_RESTRICTED));

		groupParams.put("types", types);
		groupParams.put("active", Boolean.TRUE);
	}

	int total = GroupLocalServiceUtil.searchCount(company.getCompanyId(), searchTerms.getName(), searchTerms.getDescription(), groupParams);

	searchContainer.setTotal(total);

	List results = GroupLocalServiceUtil.search(company.getCompanyId(), searchTerms.getName(), searchTerms.getDescription(), groupParams, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());

	searchContainer.setResults(results);
	%>

	<div class="separator"><!-- --></div>

	<liferay-ui:error exception="<%= NoSuchLayoutSetException.class %>">

		<%
		NoSuchLayoutSetException nslse = (NoSuchLayoutSetException)errorException;

		PKParser pkParser = new PKParser(nslse.getMessage());

		long groupId = pkParser.getLong("groupId");

		Group group = GroupLocalServiceUtil.getGroup(groupId);
		%>

		<%= LanguageUtil.format(pageContext, "community-x-does-not-have-any-private-pages", group.getName()) %>
	</liferay-ui:error>

	<liferay-ui:error exception="<%= RequiredGroupException.class %>">

		<%
		RequiredGroupException rge = (RequiredGroupException)errorException;

		long groupId = GetterUtil.getLong(rge.getMessage());

		Group group = GroupLocalServiceUtil.getGroup(groupId);
		%>

		<c:choose>
			<c:when test="<%= PortalUtil.isSystemGroup(group.getName()) %>">
				<liferay-ui:message key="the-group-cannot-be-deleted-because-it-is-a-required-system-group" />
			</c:when>
			<c:otherwise>
				<liferay-ui:message key="the-group-cannot-be-deleted-because-you-are-accessing-the-group" />
			</c:otherwise>
		</c:choose>
	</liferay-ui:error>

	<%
	List<String> headerNames = new ArrayList<String>();

	headerNames.add("name");
	headerNames.add("type");
	headerNames.add("members");
	headerNames.add("online-now");

	if (tabs1.equals("communities-owned") || tabs1.equals("communities-joined") || tabs1.equals("all-communities")) {
		headerNames.add("active");
	}

	if (tabs1.equals("communities-owned")) {
		headerNames.add("pending-requests");
	}
	headerNames.add("description");

	headerNames.add(StringPool.BLANK);

	searchContainer.setHeaderNames(headerNames);

	boolean isAdmin = RoleLocalServiceUtil.hasUserRole(user.getUserId(), user.getCompanyId(), RoleConstants.ADMINISTRATOR, false);

	
	List resultRows = searchContainer.getResultRows();
		
	for (int i = 0; i < results.size(); i++) {
		Group group = (Group)results.get(i);

		group = group.toEscapedModel();
		
		ResultRow row = new ResultRow(new Object[] {group, tabs1}, group.getGroupId(), i);

		PortletURL rowURL = renderResponse.createActionURL();

		rowURL.setWindowState(WindowState.NORMAL);

		rowURL.setParameter("struts_action", "/communities/page");
		rowURL.setParameter("groupId", String.valueOf(group.getGroupId()));
		rowURL.setParameter("redirect", currentURL);

		// Name

		StringBuilder sb = new StringBuilder();
		
		if(group.getName().equals("Guest")){
			sb.append("SCYCom");	
		}else{
			sb.append(group.getName());
		}

		if(!isAdmin && group.getName().contains("TEMPLATE")){
			continue;
		}
		
		int publicLayoutsPageCount = group.getPublicLayoutsPageCount();
		int privateLayoutsPageCount = group.getPrivateLayoutsPageCount();
		
		if(!isAdmin && group.getName().equals("Projects")){
			privateLayoutsPageCount = 0;
		}

		Group stagingGroup = null;

		if (group.hasStagingGroup()) {
			stagingGroup = group.getStagingGroup();
		}

		if ((tabs1.equals("communities-owned") || tabs1.equals("communities-joined") || tabs1.equals("all-communities")) &&
			((publicLayoutsPageCount > 0) || (privateLayoutsPageCount > 0))) {


			if (publicLayoutsPageCount > 0) {
				sb.append("<br />");
				rowURL.setParameter("groupId", String.valueOf(group.getGroupId()));
				rowURL.setParameter("privateLayout", Boolean.FALSE.toString());

				sb.append("<a href=\"");
				sb.append(rowURL.toString());
				sb.append("\" target=\"_blank\">");
				sb.append(LanguageUtil.get(pageContext, "view"));
				sb.append(" (");
				sb.append(group.getPublicLayoutsPageCount());
				sb.append(")");
				sb.append("</a>");
			}
		
			if ((stagingGroup != null) && GroupPermissionUtil.contains(permissionChecker, group.getGroupId(), ActionKeys.MANAGE_LAYOUTS)) {
				rowURL.setParameter("groupId", String.valueOf(stagingGroup.getGroupId()));
				rowURL.setParameter("privateLayout", Boolean.FALSE.toString());

				if (stagingGroup.getPublicLayoutsPageCount() > 0) {
					sb.append(" / ");
					sb.append("<a href=\"");
					sb.append(rowURL.toString());
					sb.append("\">");
					sb.append(LanguageUtil.get(pageContext, "staging"));
					sb.append("</a>");
				}
			}


			if (privateLayoutsPageCount > 0) {
				sb.append("<br />");
				rowURL.setParameter("groupId", String.valueOf(group.getGroupId()));
				rowURL.setParameter("privateLayout", Boolean.TRUE.toString());

				sb.append("<a href=\"");
				sb.append(rowURL.toString());
				sb.append("\" target=\"_blank\">");
				sb.append(LanguageUtil.get(pageContext, "view"));
				sb.append(" (");
				sb.append(group.getPrivateLayoutsPageCount());
				sb.append(")");
				sb.append("</a>");
			}
			
			if ((stagingGroup != null) && GroupPermissionUtil.contains(permissionChecker, group.getGroupId(), ActionKeys.MANAGE_LAYOUTS)) {
				rowURL.setParameter("groupId", String.valueOf(stagingGroup.getGroupId()));
				rowURL.setParameter("privateLayout", Boolean.TRUE.toString());

				if (stagingGroup.getPrivateLayoutsPageCount() > 0) {
					sb.append(" / ");
					sb.append("<a href=\"");
					sb.append(rowURL.toString());
					sb.append("\">");
					sb.append(LanguageUtil.get(pageContext, "staging"));
					sb.append("</a>");
				}
			}
		}

		row.addText(sb.toString());

		// Type

		row.addText(LanguageUtil.get(pageContext, group.getTypeLabel()));


		// Members

		LinkedHashMap userParams = new LinkedHashMap();

		userParams.put("usersGroups", new Long(group.getGroupId()));

		int membersCount = UserLocalServiceUtil.searchCount(company.getCompanyId(), null, Boolean.TRUE, userParams);

		row.addText(String.valueOf(membersCount));

		// Online Now

		int onlineCount = LiveUsers.getGroupUsersCount(company.getCompanyId(), group.getGroupId());

		row.addText(String.valueOf(onlineCount));

		// Active

		if (tabs1.equals("communities-owned") || tabs1.equals("communities-joined") || tabs1.equals("all-communities")) {
			row.addText(LanguageUtil.get(pageContext, (group.isActive() ? "yes" : "no")));
		}

		// Restricted number of petitions

		if (tabs1.equals("communities-owned")) {
			int pendingRequests = MembershipRequestLocalServiceUtil.searchCount(group.getGroupId(), MembershipRequestImpl.STATUS_PENDING);

			if (group.getType() == GroupConstants.TYPE_COMMUNITY_RESTRICTED) {
				row.addText(String.valueOf(pendingRequests));
			}
			else {
				row.addText(StringPool.BLANK);
			}
		}

		// Description
		
		StringBuilder sb2 = new StringBuilder();
		
		if(group.getDescription() !=null && group.getDescription().length() > 0){
			sb2.append("<a style=\"text-decoration: none;\" href=\"");
			sb2.append("\" title=\"");
			sb2.append(group.getDescription());
			sb2.append("\">");
			sb2.append("info");
			sb2.append("</a>");			
		}else{
			sb2.append("<a style=\"text-decoration: none;\" href=\"");
			sb2.append("\" title=\"");
			sb2.append("not description set");
			sb2.append("\">");
			sb2.append("info");
			sb2.append("</a>");
		}
		
		
		row.addText(sb2.toString());
		
		// Action
		
		row.addJSP("right", SearchEntry.DEFAULT_VALIGN, "/html/portlet/communities/community_action.jsp");

		// Add result row

		resultRows.add(row);
	}
	%>
	<liferay-ui:search-iterator searchContainer="<%= searchContainer %>" />
</c:if>

</form>

