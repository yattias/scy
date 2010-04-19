<%@ include file="/html/portlet/asset_publisher/init.jsp" %>
<%
String redirect = ParamUtil.getString(request, "redirect");

if (Validator.isNull(redirect)) {
	redirect = PortalUtil.getLayoutURL(layout, themeDisplay) + Portal.FRIENDLY_URL_SEPARATOR + "similarity";
}


if(redirect.equals(currentURL)){
	redirect = PortalUtil.getLayoutURL(layout, themeDisplay) + Portal.FRIENDLY_URL_SEPARATOR + "freestyler";
}

long assetId = ParamUtil.getLong(request, "assetId");
String type = ParamUtil.getString(request, "type");
String urlTitle = ParamUtil.getString(request, "urlTitle");

boolean show = true;
boolean print = ParamUtil.getString(request, "viewMode").equals(Constants.PRINT);

List results = new ArrayList();

int assetEntryIndex = 0;

TagsAsset assetEntry = null;

String className = StringPool.BLANK;
long classPK = 0;

String onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";


try {
	if (Validator.isNotNull(urlTitle)) {
		if (type.equals (AssetPublisherUtil.TYPE_BLOG)) {
			BlogsEntry entry = BlogsEntryServiceUtil.getEntry(scopeGroupId, urlTitle);

			className = BlogsEntry.class.getName();
			classPK = entry.getPrimaryKey();
		}
		else if (type.equals (AssetPublisherUtil.TYPE_CONTENT)) {
			JournalArticle article = JournalArticleServiceUtil.getArticleByUrlTitle(scopeGroupId, urlTitle);

			className = JournalArticle.class.getName();
			classPK = article.getResourcePrimKey();
		}

		assetEntry = TagsAssetLocalServiceUtil.getAsset(className, classPK);
	}
	else {
		assetEntry = TagsAssetLocalServiceUtil.getAsset(assetId);

		className = PortalUtil.getClassName(assetEntry.getClassNameId());
		classPK = assetEntry.getClassPK();
	}

	String title = assetEntry.getTitle();
	String summary = StringPool.BLANK;
	String viewURL = StringPool.BLANK;
	String viewURLMessage = StringPool.BLANK;
	String editURL = StringPool.BLANK;

	request.setAttribute("viewSim.jsp-results", results);

	request.setAttribute("viewSim.jsp-assetEntryIndex", new Integer(assetEntryIndex));

	request.setAttribute("viewSim.jsp-assetEntry", assetEntry);

	request.setAttribute("viewSim.jsp-title", title);
	request.setAttribute("viewSim.jsp-summary", summary);
	request.setAttribute("viewSim.jsp-viewURL", viewURL);
	request.setAttribute("viewSim.jsp-viewURLMessage", viewURLMessage);

	request.setAttribute("viewSim.jsp-className", className);
	request.setAttribute("viewSim.jsp-classPK", new Long(classPK));

	request.setAttribute("viewSim.jsp-show", new Boolean(show));
	request.setAttribute("viewSim.jsp-print", new Boolean(print));
%>
	

	<div align="right">
			&laquo; <a href="<%= HtmlUtil.escape(redirect) %>"<%= onClickHtml %>><liferay-ui:message key="back" /></a>
	</div>

	<div>
		<liferay-util:include page="/html/portlet/ext/similarity/resourceView/full_content.jsp" />
	</div>

	

<%
}
catch (Exception e) {
	_log.error(e.getMessage());
}
%>

<%!
private static Log _log = LogFactoryUtil.getLog("portal-web.docroot.html.portlet.asset_publisher.view_content.jsp");
%>