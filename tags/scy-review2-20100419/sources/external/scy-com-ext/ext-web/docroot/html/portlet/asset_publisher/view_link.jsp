<%@ include file="/html/portlet/blogs/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");
String referringPortletResource = ParamUtil.getString(request, "referringPortletResource");
BookmarksEntry entry = (BookmarksEntry)request.getAttribute(WebKeys.BOOKMARKS_ENTRY);
long entryId = BeanParamUtil.getLong(entry, request, "entryId");
long folderId = BeanParamUtil.getLong(entry, request, "folderId");
String onClickHtml = StringPool.BLANK;
String className = (String)request.getAttribute("className");
onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";
String struts_action_link = "/asset_publisher/link";
String struts_action_linkBrowse = "/asset_publisher/link_intern";
%>
<%@ include file="/html/portlet/ext/links/view_link.jspf" %>