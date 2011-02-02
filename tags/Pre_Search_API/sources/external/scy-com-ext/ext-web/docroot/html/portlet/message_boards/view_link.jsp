<%@ include file="/html/portlet/image_gallery/init.jsp" %>
<%@ page import="com.liferay.portlet.messageboards.model.MBMessageDisplay" %>

<%
String redirect = ParamUtil.getString(request, "redirect");
String referringPortletResource = ParamUtil.getString(request, "referringPortletResource");
BookmarksEntry entry = (BookmarksEntry)request.getAttribute(WebKeys.BOOKMARKS_ENTRY);
MBMessageDisplay entryMessage = (MBMessageDisplay)request.getAttribute(WebKeys.MESSAGE_BOARDS_MESSAGE);
MBMessage mb = entryMessage.getMessage();
long entryId = BeanParamUtil.getLong(mb, request, "messageId");
long folderId = BeanParamUtil.getLong(entry, request, "folderId");
String onClickHtml = StringPool.BLANK;
String className = StringPool.BLANK;
onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";
String struts_action_link = "/message_boards/link";
String struts_action_linkBrowse = "/message_boards/link_intern";
%>

<%@ include file="/html/portlet/ext/links/view_link.jspf" %>