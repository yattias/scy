<%@ include file="/html/portlet/asset_publisher/init.jsp" %>
<%@ page import="com.liferay.portlet.ratings.model.RatingsEntry" %>
<%@ page import="com.liferay.portlet.ratings.service.RatingsEntryLocalServiceUtil" %>

<%
List<TagsAsset> results = (List<TagsAsset>)request.getAttribute("allAllowdAssetEntries");


request.setAttribute("view.jsp-results", results);

for (int assetIndex = 0; assetIndex < results.size(); assetIndex++) {
	TagsAsset assetEntry = (TagsAsset)results.get(assetIndex);

	long assetId = assetEntry.getAssetId();	
	String className = PortalUtil.getClassName(assetEntry.getClassNameId());
	long classPK = assetEntry.getClassPK();
	
	List<RatingsEntry> ratings = RatingsEntryLocalServiceUtil.getEntries(assetEntry.getClassName(), assetEntry.getClassPK());
	Double ratio=0.0;
	
	for(RatingsEntry ratingsEntry: ratings){
		if(ratio == 0.0){
			ratio += ratingsEntry.getScore();
		}else{
			ratio = (ratingsEntry.getScore() + ratio) / 2;
		}
	}
	
	
	String title = assetEntry.getTitle();
	String summary = StringPool.BLANK;
	String viewURL = StringPool.BLANK;
	String viewURLMessage = StringPool.BLANK;
	String editURL = StringPool.BLANK;

	boolean show = true;

	request.setAttribute("view.jsp-assetIndex", new Integer(assetIndex));

	request.setAttribute("view.jsp-asset", assetEntry);

	request.setAttribute("view.jsp-title", title);
	request.setAttribute("view.jsp-summary", summary);
	request.setAttribute("view.jsp-viewURL", viewURL);
	request.setAttribute("view.jsp-viewURLMessage", viewURLMessage);
	request.setAttribute("view.jsp-viewRatio", ratio);

	request.setAttribute("view.jsp-className", className);
	request.setAttribute("view.jsp-classPK", new Long(classPK));

	request.setAttribute("view.jsp-show", new Boolean(show));

	try {
%>
	<c:if test="<%= assetIndex < 5 %>">
		<liferay-util:include page="/html/portlet/ext/assetviewbyratio/abstracts.jsp" />	
		
	</c:if>
<%
	}
	catch (Exception e) {
		
	}
}
%>
