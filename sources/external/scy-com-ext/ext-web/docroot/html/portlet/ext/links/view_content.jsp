<%@ include file="/html/portlet/asset_publisher/init.jsp" %>
<%
String redirect = ParamUtil.getString(request, "redirect");

long assetId = ParamUtil.getLong(renderRequest, "assetId");
String type = ParamUtil.getString (renderRequest, "type");
String urlTitle = ParamUtil.getString (renderRequest, "urlTitle");

List results = new ArrayList();

int assetIndex = 0;

TagsAsset asset = null;

String className = StringPool.BLANK;
long classPK = 0;

String onClickHtml = StringPool.BLANK;
onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";

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

		asset = TagsAssetLocalServiceUtil.getAsset(className, classPK);
	}
	else {
		asset = TagsAssetLocalServiceUtil.getAsset(assetId);

		className = PortalUtil.getClassName(asset.getClassNameId());
		classPK = asset.getClassPK();
	}

	String title = asset.getTitle();
	String summary = StringPool.BLANK;
	String viewURL = StringPool.BLANK;
	String viewURLMessage = StringPool.BLANK;
	String editURL = StringPool.BLANK;

	boolean show = true;

	request.setAttribute("view.jsp-results", results);

	request.setAttribute("view.jsp-assetIndex", new Integer(assetIndex));

	request.setAttribute("view.jsp-asset", asset);

	request.setAttribute("view.jsp-title", title);
	request.setAttribute("view.jsp-summary", summary);
	request.setAttribute("view.jsp-viewURL", viewURL);
	request.setAttribute("view.jsp-viewURLMessage", viewURLMessage);

	request.setAttribute("view.jsp-className", className);
	request.setAttribute("view.jsp-classPK", new Long(classPK));

	request.setAttribute("view.jsp-show", new Boolean(show));
%>


	<div align="right">
			&laquo; <a href="<%= HtmlUtil.escape(redirect) %>"<%= onClickHtml %>><liferay-ui:message key="back" /></a>
	</div>

	<div>
		<liferay-util:include page="/html/portlet/ext/links/full_content.jsp" />
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