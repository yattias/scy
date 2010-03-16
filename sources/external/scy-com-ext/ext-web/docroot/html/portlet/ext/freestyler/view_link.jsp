<%@ include file="/html/portlet/ext/freestyler/init.jsp" %>


<%
String redirect = ParamUtil.getString(request, "redirect");
String referringPortletResource = ParamUtil.getString(request, "referringPortletResource");
BookmarksEntry entry = (BookmarksEntry)request.getAttribute(WebKeys.BOOKMARKS_ENTRY);
long entryId = BeanParamUtil.getLong(entry, request, "entryId");
long folderId = BeanParamUtil.getLong(entry, request, "folderId");
String onClickHtml = StringPool.BLANK;
String className = StringPool.BLANK;
onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";
String struts_action_link = "/ext/freestyler/link";
String struts_action_linkBrowse = "/ext/freestyler/link_intern";
%>

<%@ include file="/html/portlet/ext/links/view_link.jspf" %>