<%@ include file="/html/portlet/blogs/init.jsp" %>

<%
String startResourceclassName = StringPool.BLANK;
BlogsEntry entry = (BlogsEntry)request.getAttribute(WebKeys.BLOGS_ENTRY);
Long entryId = BeanParamUtil.getLong(entry, request, "entryId");
String redirect = ParamUtil.getString(request, "redirect");
String sType = request.getParameter("sType");
String checkedResource = (String)request.getAttribute("checkedResource");
int abstractLength = GetterUtil.getInteger(preferences.getValue("abstract-length", StringPool.BLANK), 200);
BlogsEntry blogPreviewEntry = (BlogsEntry)request.getAttribute("blogPreviewEntry");
IGImage imagePreviewEntry = (IGImage)request.getAttribute("imagePreviewEntry");
BookmarksEntry bookmarkPreviewEntry = (BookmarksEntry)request.getAttribute("bookmarkPreviewEntry");
DLFileEntry documentPreviewEntry = (DLFileEntry)request.getAttribute("documentPreviewEntry");
MBMessage messagePreviewEntry = (MBMessage)request.getAttribute("messagePreviewEntry");
WikiPage wikiPreviewEntry = (WikiPage)request.getAttribute("wikiPreviewEntry");
JournalArticle journalPreviewEntry = (JournalArticle)request.getAttribute("journalPreviewEntry");
FreestylerImage freestylerPreviewEntry = (FreestylerImage)request.getAttribute("freestylerPreviewEntry");
String classPK = BlogsEntry.class.getName();
String struts_action = "/document_library/link_intern";
String struts_actionViewContent = "/document_library/view_links";

String onClickHtml = StringPool.BLANK;
onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";

request.setAttribute("struts_action", struts_action);
request.setAttribute("struts_actionViewContent", struts_actionViewContent);
request.setAttribute("entryId", String.valueOf(entryId));

%>
<%!
private String _checkViewURL(String viewURL, String currentURL, ThemeDisplay themeDisplay) {
	if (viewURL.startsWith(themeDisplay.getURLPortal())) {
		viewURL = HttpUtil.setParameter(viewURL, "redirect", currentURL);
	}

	return viewURL;
}
%>	
<%@ include file="/html/portlet/ext/links/view_linkIntern.jspf" %>
			              