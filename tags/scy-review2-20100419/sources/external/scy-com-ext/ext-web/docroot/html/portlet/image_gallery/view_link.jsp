<%@ include file="/html/portlet/image_gallery/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");
String referringPortletResource = ParamUtil.getString(request, "referringPortletResource");
BookmarksEntry entry = (BookmarksEntry)request.getAttribute(WebKeys.BOOKMARKS_ENTRY);
IGImage image = (IGImage)request.getAttribute(WebKeys.IMAGE_GALLERY_IMAGE);
long entryId = BeanParamUtil.getLong(image, request, "imageId");
long folderId = BeanParamUtil.getLong(entry, request, "folderId");
String onClickHtml = StringPool.BLANK;
String className = StringPool.BLANK;
onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";
String struts_action_link = "/image_gallery/link";
String struts_action_linkBrowse = "/image_gallery/link_intern";
%>

<%@ include file="/html/portlet/ext/links/view_link.jspf" %>