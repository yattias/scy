<%@ include file="/html/portlet/asset_publisher/init.jsp" %>

<%
String className = (String)request.getAttribute("viewSim.jsp-className");
long classPK = ((Long)request.getAttribute("viewSim.jsp-classPK")).longValue();

boolean showIconLabel = true;
boolean showEditIcon = false;

PortletURL editPortletURL = null;

if (className.equals(BlogsEntry.class.getName())) {
	BlogsEntry entry = BlogsEntryLocalServiceUtil.getEntry(classPK);

	showEditIcon = BlogsEntryPermission.contains(permissionChecker, entry, ActionKeys.UPDATE);

	editPortletURL = new PortletURLImpl(request, PortletKeys.BLOGS, plid, PortletRequest.RENDER_PHASE);

	editPortletURL.setParameter("struts_action", "/blogs/edit_entry");
	editPortletURL.setParameter("entryId", String.valueOf(entry.getEntryId()));
}
else if (className.equals(BookmarksEntry.class.getName())) {
	BookmarksEntry entry = BookmarksEntryLocalServiceUtil.getEntry(classPK);

	showEditIcon = BookmarksEntryPermission.contains(permissionChecker, entry, ActionKeys.UPDATE);

	editPortletURL = new PortletURLImpl(request, PortletKeys.BOOKMARKS, plid, PortletRequest.RENDER_PHASE);

	editPortletURL.setParameter("struts_action", "/bookmarks/edit_entry");
	editPortletURL.setParameter("folderId", String.valueOf(entry.getFolderId()));
	editPortletURL.setParameter("entryId", String.valueOf(entry.getEntryId()));
}
else if (className.equals(DLFileEntry.class.getName())) {
	DLFileEntry fileEntry = DLFileEntryLocalServiceUtil.getFileEntry(classPK);

	showEditIcon = DLFileEntryPermission.contains(permissionChecker, fileEntry, ActionKeys.UPDATE);

	editPortletURL = new PortletURLImpl(request, PortletKeys.DOCUMENT_LIBRARY, plid, PortletRequest.RENDER_PHASE);

	editPortletURL.setParameter("struts_action", "/document_library/edit_file_entry");
	editPortletURL.setParameter("folderId", String.valueOf(fileEntry.getFolderId()));
	editPortletURL.setParameter("name", fileEntry.getName());
}
else if (className.equals(IGImage.class.getName())) {
	IGImage image = IGImageLocalServiceUtil.getImage(classPK);

	showEditIcon = IGImagePermission.contains(permissionChecker, image, ActionKeys.UPDATE);

	editPortletURL = new PortletURLImpl(request, PortletKeys.IMAGE_GALLERY, plid, PortletRequest.RENDER_PHASE);

	editPortletURL.setParameter("struts_action", "/image_gallery/edit_image");
	editPortletURL.setParameter("folderId", String.valueOf(image.getFolderId()));
	editPortletURL.setParameter("imageId", String.valueOf(image.getImageId()));
}
else if (className.equals(JournalArticle.class.getName())) {
	JournalArticleResource articleResource = JournalArticleResourceLocalServiceUtil.getArticleResource(classPK);

	String languageId = LanguageUtil.getLanguageId(request);

	JournalArticleDisplay articleDisplay = JournalContentUtil.getDisplay(articleResource.getGroupId(), articleResource.getArticleId(), null, null, languageId, themeDisplay);

	if (articleDisplay != null) {
		showEditIcon = JournalArticlePermission.contains(permissionChecker, articleDisplay.getGroupId(), articleDisplay.getArticleId(), ActionKeys.UPDATE);

		editPortletURL = new PortletURLImpl(request, PortletKeys.JOURNAL, plid, PortletRequest.RENDER_PHASE);

		editPortletURL.setParameter("struts_action", "/journal/edit_article");
		editPortletURL.setParameter("groupId", String.valueOf(articleDisplay.getGroupId()));
		editPortletURL.setParameter("articleId", articleDisplay.getArticleId());
		editPortletURL.setParameter("version", String.valueOf(articleDisplay.getVersion()));
	}
}
else if (className.equals(MBMessage.class.getName())) {
	MBMessage message = MBMessageLocalServiceUtil.getMessage(classPK);

	showEditIcon = MBMessagePermission.contains(permissionChecker, message, ActionKeys.UPDATE);

	editPortletURL = new PortletURLImpl(request, PortletKeys.MESSAGE_BOARDS, plid, PortletRequest.RENDER_PHASE);

	editPortletURL.setParameter("struts_action", "/message_boards/edit_message");
	editPortletURL.setParameter("messageId", String.valueOf(message.getMessageId()));
}
else if (className.equals(WikiPage.class.getName())) {
	WikiPageResource pageResource = WikiPageResourceLocalServiceUtil.getPageResource(classPK);

	WikiPage wikiPage = WikiPageLocalServiceUtil.getPage(pageResource.getNodeId(), pageResource.getTitle());

	WikiNode node = wikiPage.getNode();

	showEditIcon = WikiPagePermission.contains(permissionChecker, wikiPage, ActionKeys.UPDATE);

	editPortletURL = new PortletURLImpl(request, PortletKeys.WIKI, plid, PortletRequest.RENDER_PHASE);

	editPortletURL.setParameter("struts_action", "/wiki/edit_page");
	editPortletURL.setParameter("nodeId", String.valueOf(wikiPage.getNodeId()));
	editPortletURL.setParameter("title", wikiPage.getTitle());
}

if (editPortletURL != null) {
	editPortletURL.setWindowState(WindowState.MAXIMIZED);
	editPortletURL.setPortletMode(PortletMode.VIEW);

	editPortletURL.setParameter("redirect", currentURL);
}
%>

<c:if test="<%= showEditIcon && ((layout != null) && !layout.getGroup().hasStagingGroup()) %>">
	<div class="lfr-meta-actions asset-actions">
		<liferay-ui:icon image="edit" url="<%= editPortletURL.toString() %>" label="<%= showIconLabel %>" />
	</div>
</c:if>