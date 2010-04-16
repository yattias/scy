<%@ include file="/html/portlet/asset_publisher/init.jsp" %>
<%@ page import="com.ext.portlet.freestyler.model.FreestylerEntry" %>
<%@ page import="com.ext.portlet.freestyler.model.FreestylerFolder" %>
<%@ page import="com.ext.portlet.freestyler.model.FreestylerImage" %>
<%@ page import="com.ext.portlet.freestyler.permission.FreestylerImagePermission" %>
<%@ page import="com.ext.portlet.freestyler.permission.FreestylerEntryPermission" %>
<%@ page import="com.ext.portlet.freestyler.service.FreestylerEntryLocalServiceUtil" %>
<%@ page import="com.ext.portlet.freestyler.service.FreestylerFolderLocalServiceUtil" %>
<%@ page import="com.ext.portlet.freestyler.service.FreestylerImageLocalServiceUtil" %>

<%
List results = (List)request.getAttribute("view.jsp-results");

int assetEntryIndex = ((Integer)request.getAttribute("view.jsp-assetEntryIndex")).intValue();

TagsAsset assetEntry = (TagsAsset)request.getAttribute("view.jsp-assetEntry");

String title = (String)request.getAttribute("view.jsp-title");
String viewURL = (String)request.getAttribute("view.jsp-viewURL");
String cssClassName = StringPool.BLANK;

String className = (String)request.getAttribute("view.jsp-className");
long classPK = ((Long)request.getAttribute("view.jsp-classPK")).longValue();

boolean show = ((Boolean)request.getAttribute("view.jsp-show")).booleanValue();

request.setAttribute("view.jsp-showIconLabel", false);

PortletURL viewFullContentURL = renderResponse.createRenderURL();

String struts_action = (String)request.getAttribute("struts_action");
String struts_freestyler = struts_action + "view_freestylerPreview";
String struts = struts_action + "view_links";
viewFullContentURL.setParameter("struts_action", struts);
viewFullContentURL.setParameter("assetId", String.valueOf(assetEntry.getAssetId()));

boolean isBlog=false;
boolean isBookmark=false;
boolean isDLFile=false;
boolean isIGImage=false;
boolean isJournalArticle=false;
boolean isMBMessage=false;
boolean isWikiPage=false;
Boolean isFreestylerImage = false;
String freestylerId = "";
String actualImageId = "";

if (className.equals(BlogsEntry.class.getName())) {
	BlogsEntry entry = BlogsEntryLocalServiceUtil.getEntry(classPK);

	if (Validator.isNull(title)) {
		title = entry.getTitle();
	}

	String urlTitle = entry.getUrlTitle();

	if (Validator.isNotNull(urlTitle)) {
		viewFullContentURL.setParameter("urlTitle", urlTitle);
	}

	viewFullContentURL.setParameter("type", AssetPublisherUtil.TYPE_BLOG);

	viewURL = viewInContext ? themeDisplay.getPortalURL() + themeDisplay.getPathMain() + "/blogs/find_entry?noSuchEntryRedirect=" + HttpUtil.encodeURL(viewFullContentURL.toString()) + "&entryId=" + entry.getEntryId() : viewFullContentURL.toString();
	cssClassName = AssetPublisherUtil.TYPE_BLOG;
	isBlog=true;
}
else if (className.equals(BookmarksEntry.class.getName())) {
	BookmarksEntry entry = BookmarksEntryLocalServiceUtil.getEntry(classPK);

	if (Validator.isNull(title)) {
		title = entry.getName();
	}

	String entryURL = themeDisplay.getPathMain() + "/bookmarks/open_entry?entryId=" + entry.getEntryId();

	viewFullContentURL.setParameter("type", AssetPublisherUtil.TYPE_BOOKMARK);

	viewURL = viewInContext ? entryURL : viewFullContentURL.toString();
	cssClassName = AssetPublisherUtil.TYPE_BOOKMARK;
	isBookmark=true;
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

	viewURL = viewInContext ? themeDisplay.getPathMain() + "/document_library/get_file?p_l_id=" + themeDisplay.getPlid() + "&folderId=" + fileEntry.getFolderId() + "&name=" + HttpUtil.encodeURL(fileEntry.getName()) : viewFullContentURL.toString();
	cssClassName = AssetPublisherUtil.TYPE_DOCUMENT;
	isDLFile=true;
}
else if (className.equals(IGImage.class.getName())) {
	IGImage image = IGImageLocalServiceUtil.getImage(classPK);

	PortletURL imageURL = new PortletURLImpl(request, PortletKeys.IMAGE_GALLERY, plid, PortletRequest.RENDER_PHASE);

	imageURL.setWindowState(WindowState.MAXIMIZED);

	imageURL.setParameter("struts_action", "/image_gallery/view");
	imageURL.setParameter("folderId", String.valueOf(image.getFolderId()));

	viewFullContentURL.setParameter("type", AssetPublisherUtil.TYPE_IMAGE);

	viewURL = viewInContext ? imageURL.toString() : viewFullContentURL.toString();
	cssClassName = AssetPublisherUtil.TYPE_IMAGE;
	isIGImage=true;
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

		articleURL.setParameter("struts_action", struts);
		articleURL.setParameter("urlTitle", articleDisplay.getUrlTitle());
		articleURL.setParameter("type", AssetPublisherUtil.TYPE_CONTENT);

		viewURL = articleURL.toString();
		cssClassName = AssetPublisherUtil.TYPE_CONTENT;
		isJournalArticle=true;
	}
	else {
		show = false;
	}
}
else if (className.equals(MBMessage.class.getName())) {
	MBMessage message = MBMessageLocalServiceUtil.getMBMessage(classPK);

	viewFullContentURL.setParameter("type", "thread");

	viewURL = viewInContext ? themeDisplay.getPortalURL() + themeDisplay.getPathMain() + "/message_boards/find_message?messageId=" + message.getMessageId() : viewFullContentURL.toString();
	cssClassName = "thread";
	isMBMessage=true;
}
else if (className.equals(WikiPage.class.getName())) {
	WikiPageResource pageResource = WikiPageResourceLocalServiceUtil.getPageResource(classPK);

	WikiPage wikiPage = WikiPageLocalServiceUtil.getPage(pageResource.getNodeId(), pageResource.getTitle());

	viewFullContentURL.setParameter("type", AssetPublisherUtil.TYPE_WIKI);

	viewURL = viewInContext ? themeDisplay.getPortalURL() + themeDisplay.getPathMain() + "/wiki/find_page?pageResourcePrimKey=" + wikiPage.getResourcePrimKey() : viewFullContentURL.toString();
	cssClassName = AssetPublisherUtil.TYPE_WIKI;
	isWikiPage=true;
}
else if (className.equals(FreestylerImage.class.getName())) {
	FreestylerImage feImage = FreestylerImageLocalServiceUtil.getFreestylerImage(classPK);
	FreestylerEntry feEntry = FreestylerEntryLocalServiceUtil.getFreestylerEntry(feImage.getFreestylerId());
	freestylerId = String.valueOf(feImage.getFreestylerId());
	actualImageId = String.valueOf(feImage.getImageId());
	isFreestylerImage = true;
	StringBuilder sb = new StringBuilder();
	sb.append(feEntry.getName());
	sb.append("/");
	sb.append(title);
	title = sb.toString();
}
else if (className.equals(FreestylerEntry.class.getName())) {
	FreestylerEntry freestylerEntry = FreestylerEntryLocalServiceUtil.getFreestylerEntry(classPK);

	viewFullContentURL.setParameter("type", "freestyler");
	
	viewURL = viewFullContentURL.toString();
	cssClassName = "freestyler";
}


viewURL = _checkViewURL(viewURL, currentURL, themeDisplay);
%>
	<c:if test="<%= isBlog %>">	
		<img src="<%= themeDisplay.getURLPortal() %>/html/icons/blogs.png" class="icon"  />		
	</c:if>
	<c:if test="<%= isBookmark %>">
		<img src="<%= themeDisplay.getURLPortal() %>/html/themes/classic/images/ratings/star_hover.png" class="icon"  />		
	</c:if>
	<c:if test="<%= isDLFile %>">
		<img src="<%= themeDisplay.getURLPortal() %>/html/themes/classic/images/common/clip.png" class="icon"  />		
	</c:if>
	<c:if test="<%= isIGImage %>">
		<img src="<%= themeDisplay.getURLPortal() %>/html/icons/image_gallery.png" class="icon"  />		
	</c:if>
	<c:if test="<%= isJournalArticle %>">
		<img src="<%= themeDisplay.getURLPortal() %>/html/themes/classic/images/common/history.png" class="icon"  />		
	</c:if>
	<c:if test="<%= isMBMessage %>">
		<img src="<%= themeDisplay.getURLPortal() %>/html/icons/message_boards.png" class="icon"  />		
	</c:if>
	<c:if test="<%= isWikiPage %>">
		<img src="<%= themeDisplay.getURLPortal() %>/html/themes/classic/images/common/pages.png" class="icon"  />		
	</c:if>
	
	<c:if test="<%= show %>">
			<c:choose>
				<c:when test="<%= Validator.isNotNull(viewURL) %>">
					<a href="<%= viewURL %>">					
					<%= title %></a>
				</c:when>
				<c:when test="<%= isFreestylerImage %>">
					<portlet:renderURL var="viewFreestylerURL">
							<portlet:param name="struts_action" value="<%= struts_freestyler %>" />
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
	</c:if>
