<%@ include file="/html/portlet/ext/cart/init.jsp" %>

<%
List results = (List)request.getAttribute("view.jsp-results");

int assetIndex = ((Integer)request.getAttribute("view.jsp-assetEntryIndex")).intValue();

TagsAsset asset = (TagsAsset)request.getAttribute("view.jsp-assetEntry");

String title = (String)request.getAttribute("view.jsp-title");
String viewURL = (String)request.getAttribute("view.jsp-viewURL");
Boolean isFreestylerImage = false;

String className = (String)request.getAttribute("view.jsp-className");
long classPK = ((Long)request.getAttribute("view.jsp-classPK")).longValue();

boolean show = ((Boolean)request.getAttribute("view.jsp-show")).booleanValue();
boolean onlyView = ((Boolean)request.getAttribute("onlyView")).booleanValue();

PortletURL viewFullContentURL = renderResponse.createRenderURL();
String freestylerId = "";
String actualImageId = "";

viewFullContentURL.setParameter("struts_action", "/ext/cart/view_content");
viewFullContentURL.setParameter("assetEntryId", String.valueOf(asset.getAssetId()));


if (className.equals(BlogsEntry.class.getName())) {
	BlogsEntry entry = BlogsEntryLocalServiceUtil.getEntry(classPK);

	if (Validator.isNull(title)) {
		title = entry.getTitle();
	}
	

	viewFullContentURL.setParameter("type", AssetPublisherUtil.TYPE_BLOG);
	viewURL = viewInContext ? themeDisplay.getURLPortal() + themeDisplay.getPathMain() + "/blogs/find_entry?noSuchEntryRedirect=" + HttpUtil.encodeURL(viewFullContentURL.toString()) + "&entryId=" + entry.getEntryId() : viewFullContentURL.toString();
}
else if (className.equals(BookmarksEntry.class.getName())) {
	BookmarksEntry entry = BookmarksEntryLocalServiceUtil.getEntry(classPK);

	if (Validator.isNull(title)) {
		title = entry.getName();
	}

	String entryURL = themeDisplay.getPathMain() + "/bookmarks/open_entry?entryId=" + entry.getEntryId();

	viewFullContentURL.setParameter("type", AssetPublisherUtil.TYPE_BOOKMARK);

	viewURL = viewInContext? entryURL : viewFullContentURL.toString();
}
else if (className.equals(DLFileEntry.class.getName())) {
	DLFileEntry fileEntry = DLFileEntryLocalServiceUtil.getFileEntry(classPK);

	if (Validator.isNull(title)) {
		StringBuilder sb = new StringBuilder();

		sb.append("<img align=\"left\" border=\"0\" src=\"");
		sb.append(themeDisplay.getPathThemeImages());
		sb.append("/document_library/");
		sb.append(DLUtil.getFileExtension(fileEntry.getName()));
		sb.append(".png\" />");
		sb.append(fileEntry.getTitle());

		title = sb.toString();
	}

	viewFullContentURL.setParameter("type", AssetPublisherUtil.TYPE_DOCUMENT);

	viewURL = viewInContext? themeDisplay.getPathMain() + "/document_library/get_file?p_l_id=" + themeDisplay.getPlid() + "&folderId=" + fileEntry.getFolderId() + "&name=" + HttpUtil.encodeURL(fileEntry.getName()) : viewFullContentURL.toString();
}
else if (className.equals(IGImage.class.getName())) {
	IGImage image = IGImageLocalServiceUtil.getImage(classPK);

	PortletURL imageURL = new PortletURLImpl(request, PortletKeys.IMAGE_GALLERY, plid, PortletRequest.RENDER_PHASE);

	imageURL.setWindowState(WindowState.MAXIMIZED);

	imageURL.setParameter("struts_action", "/image_gallery/view");
	imageURL.setParameter("folderId", String.valueOf(image.getFolderId()));

	viewFullContentURL.setParameter("type", AssetPublisherUtil.TYPE_IMAGE);

	viewURL = viewInContext? imageURL.toString() : viewFullContentURL.toString();
}
else if (className.equals(JournalArticle.class.getName())) {
	JournalArticleResource articleResource = JournalArticleResourceLocalServiceUtil.getArticleResource(classPK);

	String languageId = LanguageUtil.getLanguageId(request);

	JournalArticleDisplay articleDisplay = JournalContentUtil.getDisplay(articleResource.getGroupId(), articleResource.getArticleId(), null, null, languageId, themeDisplay);

	if (articleDisplay != null) {
		if (Validator.isNull(title)) {
			title = articleDisplay.getTitle();
		}

		PortletURL articleURL = renderResponse.createRenderURL();
		
		System.out.println(articleResource.getPrimaryKey());
		
		viewFullContentURL.setParameter("resourcePrimKey", String.valueOf(articleResource.getResourcePrimKey()));
		viewFullContentURL.setParameter("primaryKey", String.valueOf(articleResource.getPrimaryKey()));
		viewFullContentURL.setParameter("type", AssetPublisherUtil.TYPE_CONTENT);

		viewURL = viewFullContentURL.toString();

	}
	else {
		show = false;
	}
}
else if (className.equals(MBMessage.class.getName())) {
	MBMessage message = MBMessageLocalServiceUtil.getMBMessage(classPK);

	viewFullContentURL.setParameter("type", "thread");

	viewURL = viewInContext ? themeDisplay.getURLPortal() + themeDisplay.getPathMain() + "/message_boards/find_message?messageId=" + message.getMessageId() : viewFullContentURL.toString();
}
else if (className.equals(WikiPage.class.getName())) {
	WikiPageResource pageResource = WikiPageResourceLocalServiceUtil.getPageResource(classPK);

	WikiPage wikiPage = WikiPageLocalServiceUtil.getPage(pageResource.getNodeId(), pageResource.getTitle());

	PortletURL pageURL = new PortletURLImpl(request, PortletKeys.WIKI, plid, PortletRequest.ACTION_PHASE);

	pageURL.setWindowState(WindowState.MAXIMIZED);
	pageURL.setPortletMode(PortletMode.VIEW);

	pageURL.setParameter("struts_action", "/wiki/view");
	pageURL.setParameter("title", wikiPage.getTitle());

	viewFullContentURL.setParameter("type", AssetPublisherUtil.TYPE_WIKI);

	viewURL = viewInContext? pageURL.toString() : viewFullContentURL.toString();
}
else if (className.equals(FreestylerImage.class.getName())) {
	FreestylerImage feImage = FreestylerImageLocalServiceUtil.getFreestylerImage(classPK);
	FreestylerEntry feEntry = FreestylerEntryLocalServiceUtil.getFreestylerEntry(feImage.getFreestylerId());
	freestylerId = String.valueOf(feImage.getFreestylerId());
	actualImageId = String.valueOf(feImage.getImageId());
	isFreestylerImage = true;
	StringBuilder sb = new StringBuilder();
	sb.append("FreestylerEntry: ");
	sb.append(feEntry.getName());
	sb.append(" Picture: ");
	sb.append(title);
	title = sb.toString();
}

viewURL = _checkViewURL(viewURL, currentURL, themeDisplay);
%>

<c:if test="<%= assetIndex == 0 %>">
	<table class="taglib-search-iterator">
	<tr class="portlet-section-header results-header">
		<th>
			<liferay-ui:message key="title" />
		</th>

		<%
		for (int m = 0; m < metadataFields.length; m++) {
		%>
			<th>
				<liferay-ui:message key="<%= metadataFields[m] %>" />
			</th>
		<%
		}
		%>

		<th></th>
	</tr>
</c:if>

<c:if test="<%= show %>">

	<%
	String style = "class=\"portlet-section-body results-row\" onmouseover=\"this.className = 'portlet-section-body-hover results-row hover';\" onmouseout=\"this.className = 'portlet-section-body results-row';\"";

	if (assetIndex % 2 == 0) {
		style = "class=\"portlet-section-alternate results-row alt\" onmouseover=\"this.className = 'portlet-section-alternate-hover results-row alt hover';\" onmouseout=\"this.className = 'portlet-section-alternate results-row alt';\"";
	}
	%>
			
	<tr <%= style %>>
		<td>
			<c:if test="<%= !onlyView %>">
				<input type="checkbox" name="cartId" value="<%= asset.getClassPK() %>">				
			</c:if>
	
			<c:choose>
				<c:when test="<%= Validator.isNotNull(viewURL) %>">
					<a href="<%= viewURL %>"><%= title %></a>
				</c:when>
				<c:when test="<%= isFreestylerImage %>">
					<portlet:renderURL var="viewFreestylerURL">
							<portlet:param name="struts_action" value="/ext/cart/view_freestylerPreview" />
							<portlet:param name="redirect" value="<%= currentURL %>" />
							<portlet:param name="freestylerEntryId" value="<%= freestylerId %>" />
							<portlet:param name="actualImageId" value="<%= actualImageId %>" />
					</portlet:renderURL>	
					<a href="<%= viewFreestylerURL %>"><%= title %></a>
				</c:when>
				<c:otherwise>
					<%= title %>
				</c:otherwise>
			</c:choose>
		</td>

	
		<td></td>
	</tr>
</c:if>

<c:if test="<%= (assetIndex + 1) == results.size() %>">
	</table>
</c:if>