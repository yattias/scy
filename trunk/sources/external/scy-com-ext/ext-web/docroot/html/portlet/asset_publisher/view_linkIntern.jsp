
<%@page import="com.liferay.portlet.blogs.model.BlogsEntry"%><%@ include file="/html/portlet/blogs/init.jsp" %>

<%
String classPK = ParamUtil.getString(request, "className");
Long entryId = Long.valueOf(ParamUtil.getString(request, "entryId"));
String redirect = ParamUtil.getString(request, "redirect");
String sType = request.getParameter("sType");
String startResourceclassName = classPK;
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
String struts_action = "/asset_publisher/link_intern";
String struts_actionViewContent = "/asset_publisher/view_links";

request.setAttribute("struts_action", struts_action);
request.setAttribute("struts_actionViewContent", struts_actionViewContent);
request.setAttribute("entryId", String.valueOf(entryId));
request.setAttribute("className", classPK);

String onClickHtml = StringPool.BLANK;
onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";

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
			              