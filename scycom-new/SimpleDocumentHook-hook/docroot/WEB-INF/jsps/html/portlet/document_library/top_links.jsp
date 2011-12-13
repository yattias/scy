<%/**
			 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
			 *
			 * This library is free software; you can redistribute it and/or modify it under
			 * the terms of the GNU Lesser General Public License as published by the Free
			 * Software Foundation; either version 2.1 of the License, or (at your option)
			 * any later version.
			 *
			 * This library is distributed in the hope that it will be useful, but WITHOUT
			 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
			 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
			 * details.
			 */%>

<%@ include file="/html/portlet/document_library/init.jsp" %>

<c:choose>
	<c:when test="<%= showTabs && portletName.equals(PortletKeys.DOCUMENT_LIBRARY) %>">

		<%
			String topLink = ParamUtil.getString(request, "topLink",
							"documents-home");

					long folderId = GetterUtil.getLong((String) request
							.getAttribute("view.jsp-folderId"));

					boolean viewFolder = GetterUtil.getBoolean((String) request
							.getAttribute("view.jsp-viewFolder"));

					boolean useAssetEntryQuery = GetterUtil
							.getBoolean((String) request
									.getAttribute("view.jsp-useAssetEntryQuery"));

					PortletURL portletURL = renderResponse.createRenderURL();

					portletURL.setParameter("categoryId", StringPool.BLANK);
					portletURL.setParameter("tag", StringPool.BLANK);
					
					DLFolder folder = null;
					String modelResource = null;
					String modelResourceDescription = null;
					String resourcePrimKey = null;
					boolean showPermissionsURL = false;
					folder = (DLFolder)request.getAttribute("view.jsp-folder");
					if (folder != null) {
						modelResource = DLFolder.class.getName();
						modelResourceDescription = folder.getName();
						resourcePrimKey = String.valueOf(folderId);

						showPermissionsURL = DLFolderPermission.contains(permissionChecker, folder, ActionKeys.PERMISSIONS);
					}
					else {
						modelResource = "com.liferay.portlet.documentlibrary";
						modelResourceDescription = themeDisplay.getScopeGroupName();
						resourcePrimKey = String.valueOf(scopeGroupId);

						showPermissionsURL = GroupPermissionUtil.contains(permissionChecker, scopeGroupId, ActionKeys.PERMISSIONS);
					}

		%>

		<div class="top-links-container">
			<div class="top-links"> 
			
				<liferay-portlet:renderURL varImpl="searchURL">
					<portlet:param name="struts_action" value="/document_library/search" />
				</liferay-portlet:renderURL>

				<c:if test="<%= showFoldersSearch %>">
					<div class="folder-search">
						<aui:form action="<%= searchURL %>" method="get" name="searchFm">
							<liferay-portlet:renderURLParams varImpl="searchURL" />
							<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
							<aui:input name="breadcrumbsFolderId" type="hidden" value="<%= folderId %>" />
							<aui:input name="searchFolderIds" type="hidden" value="<%= folderId %>" />

							<span class="aui-search-bar">
								<aui:input id="keywords1" inlineField="<%= true %>" label="" name="keywords" size="30" title="search-documents" type="text" />

								<aui:button type="submit" value="search" />
							</span>
						</aui:form>
					</div>

					<c:if test="<%= windowState.equals(WindowState.MAXIMIZED) %>">
						<aui:script>
							Liferay.Util.focusFormField(document.<portlet:namespace />searchFm.<portlet:namespace />keywords);
						</aui:script>
					</c:if>
				</c:if>
			<div class="top-links-buttons">
				<c:if test="<%= DLFolderPermission.contains(permissionChecker, scopeGroupId, folderId, ActionKeys.ADD_DOCUMENT) %>">
					<portlet:renderURL var="editFileEntryURL">
						<portlet:param name="struts_action"
							value="/document_library/edit_file_entry" />
						<portlet:param name="redirect" value="<%= currentURL %>" />
						<portlet:param name="folderId"
							value="<%= String.valueOf(folderId) %>" />
					</portlet:renderURL>
					<liferay-ui:icon image="../document_library/add_document" message="add-document" url="<%= editFileEntryURL %>" />
					<a href="<%= editFileEntryURL %>">
						<liferay-ui:message key="add-document" />
					</a>&nbsp;
				</c:if>
				<c:if test="<%= showPermissionsURL %>">
					<liferay-security:permissionsURL
						modelResource="<%= modelResource %>"
						modelResourceDescription="<%= HtmlUtil.escape(modelResourceDescription) %>"
						resourcePrimKey="<%= resourcePrimKey %>"
						var="permissionsURL"
					/>
					<liferay-ui:icon image="permissions" url="<%= permissionsURL %>" />
					<a href="<%= permissionsURL %>">
						<liferay-ui:message key="permissions" />
					</a>
				</c:if>
			</div>
			</div>
		</div>
	</c:when>
	<c:when test="<%= showTabs && portletName.equals(PortletKeys.DOCUMENT_LIBRARY_DISPLAY) %>">

		<%
			long folderId = GetterUtil.getLong((String) request
							.getAttribute("view.jsp-folderId"));
		%>

		<div class="top-links-container">
			<div class="top-links">
				<liferay-portlet:renderURL varImpl="searchURL">
					<portlet:param name="struts_action" value="/document_library/search" />
				</liferay-portlet:renderURL>

				<c:if test="<%= showFoldersSearch %>">
					<div class="folder-search">
						<aui:form action="<%= searchURL %>" method="get" name="searchFm">
							<liferay-portlet:renderURLParams varImpl="searchURL" />
							<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
							<aui:input name="breadcrumbsFolderId" type="hidden" value="<%= folderId %>" />
							<aui:input name="searchFolderIds" type="hidden" value="<%= folderId %>" />

							<span class="aui-search-bar">
								<aui:input id="keywords1" inlineField="<%= true %>" label="" name="keywords" size="30" title="search-documents" type="text" />

								<aui:button type="submit" value="search" />
							</span>
						</aui:form>
					</div>

					<c:if test="<%= windowState.equals(WindowState.MAXIMIZED) %>">
						<aui:script>
							Liferay.Util.focusFormField(document.<portlet:namespace />searchFm.<portlet:namespace />keywords);
						</aui:script>
					</c:if>
				</c:if>
			</div>
		</div>
	</c:when>
</c:choose>