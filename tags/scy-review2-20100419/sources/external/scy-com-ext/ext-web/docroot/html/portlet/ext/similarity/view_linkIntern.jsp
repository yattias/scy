<%@ include file="/html/portlet/ext/similarity/init.jsp" %>

<%
Long entryId = (Long)request.getAttribute("entryId");
String startResourceclassName = ParamUtil.getString(request, "className");
String redirect = ParamUtil.getString(request, "redirect");
String sType = request.getParameter("sType");
String checkedResource = (String)request.getAttribute("checkedResource");
BlogsEntry blogPreviewEntry = (BlogsEntry)request.getAttribute("blogPreviewEntry");
IGImage imagePreviewEntry = (IGImage)request.getAttribute("imagePreviewEntry");
BookmarksEntry bookmarkPreviewEntry = (BookmarksEntry)request.getAttribute("bookmarkPreviewEntry");
DLFileEntry documentPreviewEntry = (DLFileEntry)request.getAttribute("documentPreviewEntry");
MBMessage messagePreviewEntry = (MBMessage)request.getAttribute("messagePreviewEntry");
WikiPage wikiPreviewEntry = (WikiPage)request.getAttribute("wikiPreviewEntry");
JournalArticle journalPreviewEntry = (JournalArticle)request.getAttribute("journalPreviewEntry");
FreestylerImage freestylerPreviewEntry = (FreestylerImage)request.getAttribute("freestylerPreviewEntry");
String classPK = startResourceclassName;
String struts_action = "/ext/similarity/link_intern";
String struts_actionViewContent = "/ext/similarity/view_links";

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
			              