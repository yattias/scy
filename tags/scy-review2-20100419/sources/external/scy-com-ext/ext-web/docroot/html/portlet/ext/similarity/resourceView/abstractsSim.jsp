<%@ include file="/html/portlet/asset_publisher/init.jsp" %>
<%@ page import="com.ext.portlet.linkTool.model.LinkEntry" %>
<%@ page import="com.ext.portlet.linkTool.service.LinkEntryLocalServiceUtil" %>
<%@ page import="com.ext.portlet.freestyler.model.FreestylerEntry" %>
<%@ page import="com.ext.portlet.freestyler.model.FreestylerFolder" %>
<%@ page import="com.ext.portlet.freestyler.model.FreestylerImage" %>
<%@ page import="com.ext.portlet.freestyler.permission.FreestylerImagePermission" %>
<%@ page import="com.ext.portlet.freestyler.permission.FreestylerEntryPermission" %>
<%@ page import="com.ext.portlet.freestyler.service.FreestylerEntryLocalServiceUtil" %>
<%@ page import="com.ext.portlet.freestyler.service.FreestylerFolderLocalServiceUtil" %>
<%@ page import="com.ext.portlet.freestyler.service.FreestylerImageLocalServiceUtil" %>
<%
List results = (List)request.getAttribute("viewSimContent.jsp-results");

int assetIndex = ((Integer)request.getAttribute("viewSimContent.jsp-assetEntryIndex")).intValue();

TagsAsset asset = (TagsAsset)request.getAttribute("viewSimContent.jsp-assetEntry");

String simValue = (String)request.getAttribute("viewSimContent.jsp-simValue");

String title = (String)request.getAttribute("viewSimContent.jsp-title");
String summary = (String)request.getAttribute("viewSimContent.jsp-summary");
String viewURL = (String)request.getAttribute("viewSimContent.jsp-viewURL");
String viewURLMessage = (String)request.getAttribute("viewSimContent.jsp-viewURLMessage");
String cssClassName = StringPool.BLANK;

String className = (String)request.getAttribute("viewSimContent.jsp-className");
long classPK = ((Long)request.getAttribute("viewSimContent.jsp-classPK")).longValue();

boolean show = ((Boolean)request.getAttribute("viewSimContent.jsp-show")).booleanValue();
boolean isLinked=false;

request.setAttribute("viewSimContent.jsp-showIconLabel", true);

PortletURL viewFullContentURL = renderResponse.createRenderURL();

viewFullContentURL.setParameter("struts_action", "/ext/similarity/view_content");
viewFullContentURL.setParameter("assetId", String.valueOf(asset.getAssetId()));

if (className.equals(BlogsEntry.class.getName())) {
	BlogsEntry entry = BlogsEntryLocalServiceUtil.getEntry(classPK);

	if (Validator.isNull(title)) {
		title = entry.getTitle();
	}

	viewFullContentURL.setParameter("type", AssetPublisherUtil.TYPE_BLOG);

	String urlTitle = entry.getUrlTitle();

	if (Validator.isNotNull(urlTitle)) {
		viewFullContentURL.setParameter("urlTitle", urlTitle);
	}

	summary = StringUtil.shorten(HtmlUtil.stripHtml(entry.getContent()), abstractLength);
	viewURL = viewInContext ? themeDisplay.getURLPortal() + themeDisplay.getPathMain() + "/blogs/find_entry?noSuchEntryRedirect=" + HttpUtil.encodeURL(viewFullContentURL.toString()) + "&entryId=" + entry.getEntryId() : viewFullContentURL.toString();
	viewURLMessage = viewInContext ? "view-in-context" : "read-more";
	cssClassName = AssetPublisherUtil.TYPE_BLOG;
}
else if (className.equals(BookmarksEntry.class.getName())) {
	BookmarksEntry entry = BookmarksEntryLocalServiceUtil.getEntry(classPK);

	if (Validator.isNull(title)) {
		title = entry.getName();
	}

	String entryURL = themeDisplay.getPathMain() + "/bookmarks/open_entry?entryId=" + entry.getEntryId();

	viewFullContentURL.setParameter("type", AssetPublisherUtil.TYPE_BOOKMARK);

	summary = entry.getComments();
	viewURL = viewInContext ? entryURL : viewFullContentURL.toString();
	viewURLMessage = viewInContext ? "go" : "read-more";
	cssClassName = AssetPublisherUtil.TYPE_BOOKMARK;
}
else if (className.equals(DLFileEntry.class.getName())) {
	DLFileEntry fileEntry = DLFileEntryLocalServiceUtil.getFileEntry(classPK);

	String fileEntryURL = themeDisplay.getPathMain() + "/document_library/get_file?p_l_id=" + themeDisplay.getPlid() + "&folderId=" + fileEntry.getFolderId() + "&name=" + HttpUtil.encodeURL(fileEntry.getName());

	StringBuilder sb = new StringBuilder();

	sb.append("<a href=\"");
	sb.append(fileEntryURL);
	sb.append("\"><img align=\"left\" border=\"0\" src=\"");
	sb.append(themeDisplay.getPathThemeImages());
	sb.append("/document_library/");
	sb.append(DLUtil.getFileExtension(fileEntry.getName()));
	sb.append(".png\" />");
	sb.append(fileEntry.getTitle());
	sb.append("</a>");

	summary = fileEntry.getDescription();

	if (Validator.isNull(title)) {
		title = sb.toString();
	}
	else {
		sb.append("<br />");
		sb.append(summary);

		summary = sb.toString();
	}

	viewFullContentURL.setParameter("type", AssetPublisherUtil.TYPE_DOCUMENT);

	viewURL = viewInContext ? fileEntryURL : viewFullContentURL.toString();
	viewURLMessage = viewInContext ? "download" : "read-more";
	cssClassName = AssetPublisherUtil.TYPE_DOCUMENT;
}
else if (className.equals(IGImage.class.getName())) {
	IGImage image = IGImageLocalServiceUtil.getImage(classPK);

	PortletURL imageURL = new PortletURLImpl(request, PortletKeys.IMAGE_GALLERY, plid, PortletRequest.RENDER_PHASE);

	imageURL.setWindowState(WindowState.MAXIMIZED);

	imageURL.setParameter("struts_action", "/image_gallery/view");
	imageURL.setParameter("folderId", String.valueOf(image.getFolderId()));

	viewFullContentURL.setParameter("type", AssetPublisherUtil.TYPE_IMAGE);

	viewURL = viewInContext ? imageURL.toString() : viewFullContentURL.toString();
	viewURLMessage = viewInContext ? "view-album" : "view";
	cssClassName = AssetPublisherUtil.TYPE_IMAGE;

	StringBuilder sb = new StringBuilder();

	Image smallImage = ImageLocalServiceUtil.getImage(image.getSmallImageId());

	if (smallImage != null) {
		long smallImageId = smallImage.getImageId();

		sb.append("<a href=\"");
		sb.append(_checkViewURL(viewURL, currentURL, themeDisplay));
		sb.append("\"><img align=\"left\" alt=\"");
		sb.append(LanguageUtil.get(pageContext, viewURLMessage));
		sb.append("\" border=\"0\" src=\"");
		sb.append(themeDisplay.getPathImage());
		sb.append("/image_gallery?img_id=");
		sb.append(smallImageId);
		sb.append("\" style=\"float: left; padding-right: 10px;\" /></a>");
	}

	sb.append(StringUtil.shorten(image.getDescription(), abstractLength));

	summary = sb.toString();
}
else if (className.equals(JournalArticle.class.getName())) {
	JournalArticleResource articleResource = JournalArticleResourceLocalServiceUtil.getArticleResource(classPK);

	String languageId = LanguageUtil.getLanguageId(request);

	JournalArticleDisplay articleDisplay = JournalContentUtil.getDisplay(articleResource.getGroupId(), articleResource.getArticleId(), null, null, languageId, themeDisplay);

	if (articleDisplay != null) {
		if (Validator.isNull(title)) {
			title = articleDisplay.getTitle();
		}

		StringBuilder sb = new StringBuilder();

		if (articleDisplay.isSmallImage()) {
			sb.append("<div style=\"float: left; padding-right: 10px;\"><img alt=\"");
			sb.append(LanguageUtil.get(pageContext, "web-content-image"));
			sb.append("\" src=\"");

			if (Validator.isNotNull(articleDisplay.getSmallImageURL())) {
				sb.append(articleDisplay.getSmallImageURL());
			}
			else {
				sb.append(themeDisplay.getPathImage());
				sb.append("/journal/article?img_id=");
				sb.append(articleDisplay.getSmallImageId());
				sb.append("&t=");
				sb.append(ImageServletTokenUtil.getToken(articleDisplay.getSmallImageId()));
			}

			sb.append("\" /></div>");
		}

		sb.append(articleDisplay.getDescription());

		summary = sb.toString();

		if (Validator.isNull(summary)) {
			summary = StringUtil.shorten(HtmlUtil.stripHtml(articleDisplay.getContent()), abstractLength);
		}

		viewFullContentURL.setParameter("urlTitle", articleDisplay.getUrlTitle());
		viewFullContentURL.setParameter("type", AssetPublisherUtil.TYPE_CONTENT);

		viewURL = viewFullContentURL.toString();
		viewURLMessage = "read-more";
		cssClassName = AssetPublisherUtil.TYPE_CONTENT;
	}
	else {
		show = false;
	}
}
else if (className.equals(MBMessage.class.getName())) {
	MBMessage message = MBMessageLocalServiceUtil.getMessage(classPK);

	viewFullContentURL.setParameter("type", "thread");

	summary = StringUtil.shorten(message.getBody(), abstractLength);
	viewURL = viewInContext ? themeDisplay.getURLPortal() + themeDisplay.getPathMain() + "/message_boards/find_message?messageId=" + message.getMessageId() : viewFullContentURL.toString();
	viewURLMessage = viewInContext ? "view-in-context" : "read-more";
	cssClassName = "thread";
}
else if (className.equals(WikiPage.class.getName())) {
	WikiPageResource pageResource = WikiPageResourceLocalServiceUtil.getPageResource(classPK);

	WikiPage wikiPage = WikiPageLocalServiceUtil.getPage(pageResource.getNodeId(), pageResource.getTitle());

	summary = wikiPage.getContent();

	if (wikiPage.getFormat().equals("html")) {
		summary = HtmlUtil.stripHtml(summary);
	}

	viewFullContentURL.setParameter("type", AssetPublisherUtil.TYPE_WIKI);

	summary = StringUtil.shorten(summary, abstractLength);
	viewURL = viewInContext ? themeDisplay.getURLPortal() + themeDisplay.getPathMain() + "/wiki/find_page?pageResourcePrimKey=" + wikiPage.getResourcePrimKey() : viewFullContentURL.toString();
	viewURLMessage = viewInContext ? "view-in-context" : "read-more";
	cssClassName = AssetPublisherUtil.TYPE_WIKI;
}
else if (className.equals(FreestylerImage.class.getName())) {
	FreestylerImage freestylerImage = FreestylerImageLocalServiceUtil.getFreestylerImage(classPK);
	FreestylerEntry freestylerEntry = FreestylerEntryLocalServiceUtil.getFreestylerEntry(freestylerImage.getFreestylerId());
	
	title = freestylerEntry.getName();

	viewFullContentURL.setParameter("type", "freestyler");
	
	StringBuilder sb = new StringBuilder();
	sb.append(freestylerEntry.getDescription());

	summary = sb.toString();
	viewURL = viewFullContentURL.toString();
	cssClassName = "freestyler";
}

viewURL = _checkViewURL(viewURL, currentURL, themeDisplay);
%>

<c:if test="<%= show %>">
	<div class="asset-abstract">
		<h3 class="asset-title <%= cssClassName %>">
			<liferay-util:include page="/html/portlet/ext/similarity/resourceView/edit_actionSim.jsp" />

			<c:choose>
				<c:when test="<%= Validator.isNotNull(viewURL) %>">
					<a href="<%= viewURL %>"><%= title %></a>
				</c:when>
				<c:otherwise>
					<%= title %>
				</c:otherwise>
			</c:choose>
		</h3>

		<div class="asset-content">
			Wert: <%= simValue %>
			<p class="asset-summary">
				<%= summary %>
			</p>

			<c:if test="<%= Validator.isNotNull(viewURL) %>">
				<div class="asset-more">
					<a href="<%= viewURL %>"><liferay-ui:message key="<%= viewURLMessage %>" /> &raquo; </a>
				</div>
			</c:if>
		</div>
		
			<div class="entry-tags">
					<liferay-ui:tags-summary
							className="<%= className %>"
							classPK="<%= classPK %>"
							portletURL="<%= renderResponse.createRenderURL() %>"
						/>
						<br/>
		
					<span class="entry-tags">
						<liferay-ui-ext:asset-owntags-summary
							className="<%= className %>"
							classPK="<%= classPK %>"
							portletURL="<%= renderResponse.createRenderURL() %>"
							strutsAction="/ext/similarity/delete_tag"
						/>
					</span>
						
						<br/>
						
						<liferay-ui-ext:add-own-tags
							className="<%=className %>"
							classPK="<%= classPK %>"
							portletURL="<%= renderResponse.createRenderURL() %>"
							strutsAction="/ext/similarity/add_tag"
						/>
						
								
						<liferay-ui-ext:add-own-meta
							className="<%=className %>"
							classPK="<%= classPK %>"
							portletURL="<%= renderResponse.createRenderURL() %>"
							strutsAction="/ext/similarity/add_meta"
						/>
						
						<liferay-ui-ext:add-to-cart
							className="<%= className %>"
							classPK="<%= classPK %>"
							portletURL="<%= renderResponse.createRenderURL() %>"
							strutsAction="/ext/similarity/add_cart"
						/>
			
						<liferay-ui-ext:add-link
								className="<%= className %>"
								classPK="<%= classPK %>"
								portletURL="<%= renderResponse.createRenderURL() %>"
								strutsAction="/ext/similarity/link"
						/>

						
						<liferay-ui-ext:sim-button
							className="<%=className %>"
							classPK="<%= classPK %>"
							portletURL="<%= renderResponse.createRenderURL() %>"
							strutsAction="/ext/similarity/view_sim"
						/>
									
							
						<liferay-ui-ext:show-link
							classPK="<%= classPK %>"
							portletURL="<%= renderResponse.createRenderURL() %>"
							strutsAction="/ext/similarity/"
						/>		
			</div>

		<div class="asset-metadata">
			<%@ include file="/html/portlet/asset_publisher/asset_metadata.jspf" %>
		</div>
	</div>

	<c:if test="<%= (assetIndex + 1) == results.size() %>">
		<div class="final-separator"><!-- --></div>
	</c:if>
</c:if>