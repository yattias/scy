<%@ include file="/html/portlet/ext/similarity/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");
String className = ParamUtil.getString(request, "className");
String referringPortletResource = ParamUtil.getString(request, "referringPortletResource");
BookmarksEntry entry = (BookmarksEntry)request.getAttribute(WebKeys.BOOKMARKS_ENTRY);
long entryId = BeanParamUtil.getLong(entry, request, "entryId");
long folderId = BeanParamUtil.getLong(entry, request, "folderId");
String onClickHtml = StringPool.BLANK;
onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";
String struts_action_link = "/ext/similarity/link";
String struts_action_linkBrowse = "/ext/similarity/link_intern";
%>

<%@ include file="/html/portlet/ext/links/view_link.jspf" %>