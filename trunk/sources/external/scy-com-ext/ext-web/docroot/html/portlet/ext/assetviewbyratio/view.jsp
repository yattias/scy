<%@ include file="/html/portlet/asset_publisher/init.jsp" %>
<%@ page import="com.liferay.portlet.ratings.model.RatingsEntry" %>
<%@ page import="com.liferay.portlet.ratings.service.RatingsEntryLocalServiceUtil" %>
<%@page import="com.ext.portlet.assetviewbyratio.action.ViewAction"%>
<%@ page import="java.util.*" %>

<%
List<TagsAsset> results = (List<TagsAsset>)request.getAttribute("allAllowdAssetEntries");
String assetSortType= (String)request.getAttribute("assetSortType");
String assetLanguageType= (String)request.getAttribute("assetLanguageType");
String assetContentType= (String)request.getAttribute("assetContentType");
String[] metadataAgeChecklist = (String[]) request.getAttribute("metadataAgeChecklist");
Boolean showDurable= (Boolean)request.getAttribute("showDurable");

if(showDurable == null){
	showDurable = false;
}

String onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";

if(assetSortType == null){
	assetSortType = ViewAction.MOST_RECENT;
}
if(assetLanguageType == null){
	assetSortType = ViewAction.LANGUAGE_ALL;
}
if(assetContentType == null){
	assetContentType = "allContent";
}

Vector metadataAgeListVector = new Vector();
 if (metadataAgeChecklist != null)
   {
      for (int i=0; i<metadataAgeChecklist.length; i++)
      {
    	  metadataAgeListVector.add(metadataAgeChecklist[i]);
      }
   }


request.setAttribute("view.jsp-results", results);
%>


<c:if test="<%= assetSortType.equals(ViewAction.MOST_RECENT) %>">	
			<div class="div-center">
				<h2>Top 5 with most ratio</h2>
			</div>
</c:if>
<c:if test="<%= assetSortType.equals(ViewAction.LATEST_DATE) %>">	
			<div class="div-center">
				<h2>Top 5 latest date</h2>
			</div>
</c:if>
<c:if test="<%= assetSortType.equals(ViewAction.MOST_VIEWS) %>">	
			<div class="div-center">
				<h2>Top 5 most views</h2>
			</div>
</c:if>



<div class="div-center">
	<form action="<portlet:actionURL>
				                    <portlet:param name="struts_action" value="/ext/asset_by_ratio/view" />  
				   </portlet:actionURL>" method="post" name="<portlet:namespace />choseFilterType">			                
										 Sort options:
										 <select class="top-option-panel" name="assetSortType" size="1" onChange="submitForm(document.<portlet:namespace />choseFilterType);">
								             <option value="mostRecent"<%= "mostRecent".equals(assetSortType) ? " selected" : "" %>>Most Recent</option>
								             <option value="latestDate"<%= "latestDate".equals(assetSortType) ? " selected" : "" %>>Latest Date</option>
								             <option value="mostViews"<%= "mostViews".equals(assetSortType) ? " selected" : "" %>>Most views</option>
	         							  </select>
	</form>
</div>


<c:if test="<%= !showDurable %>">	
<div id="<portlet:namespace />block3" class="box1">  
</c:if>
<c:if test="<%= showDurable %>">	
<div id="<portlet:namespace />block4" class="box2">  
</c:if>

	<table class="table-extra-settings">
				<tr>
					<td class="td-extra-settings">
						Language:
					</td>
					<td class="td-extra-settings">
						<form action="<portlet:actionURL>
				                    <portlet:param name="struts_action" value="/ext/asset_by_ratio/view" />
									  </portlet:actionURL>" method="post" name="<portlet:namespace />chooseLanguageType">		                
										 <select name="assetLanguageType" size="1" onChange="submitForm(document.<portlet:namespace />chooseLanguageType);">
							             <option value="all"<%= "all".equals(assetLanguageType) ? " selected" : "" %>>all Languages</option>
							             <option value="en"<%= "en".equals(assetLanguageType) ? " selected" : "" %>>english</option>
							             <option value="de"<%= "de".equals(assetLanguageType) ? " selected" : "" %>>german</option>
							             <option value="nl"<%= "nl".equals(assetLanguageType) ? " selected" : "" %>>dutch</option>
							             <option value="fr"<%= "fr".equals(assetLanguageType) ? " selected" : "" %>>french</option>
							             <option value="no"<%= "no".equals(assetLanguageType) ? " selected" : "" %>>norse</option>
							             <option value="cy"<%= "cy".equals(assetLanguageType) ? " selected" : "" %>>cypriot</option>
	         							  </select>
						</form>

					</td>
				</tr>
				<tr>
					<td class="td-extra-settings">
						Content Type:
					</td>
					<td class="td-extra-settings">
						<form action="<portlet:actionURL>
				                    <portlet:param name="struts_action" value="/ext/asset_by_ratio/view" />
									  </portlet:actionURL>" method="post" name="<portlet:namespace />chooseContentType">		                
										 <select name="assetContentType" size="1" onChange="submitForm(document.<portlet:namespace />chooseContentType);">
							             <option value="allContent"<%= "allContent".equals(assetContentType) ? " selected" : "" %>>all content</option>
							             <option value="image"<%= "image".equals(assetContentType) ? " selected" : "" %>>only images</option>
							             <option value="text"<%= "text".equals(assetContentType) ? " selected" : "" %>>only text</option>
							             <option value="video"<%= "video".equals(assetContentType) ? " selected" : "" %>>only video</option>
							             <option value="elo"<%= "elo".equals(assetContentType) ? " selected" : "" %>>only elo</option>
	         							  </select>
						</form>
        			 </td>
				</tr>
				<tr>
					<td class="td-extra-settings">
						Age:
					</td>
					<td class="td-extra-settings">
 						<form action="<portlet:actionURL>
					                    <portlet:param name="struts_action" value="/ext/asset_by_ratio/view" />
										  </portlet:actionURL>" method="post" name="<portlet:namespace />choseAgeChecklist">		
					
							<c:choose>
								<c:when test="<%= metadataAgeListVector.size() > 0 %>">
						  		   <input type="checkbox" name="metadataAgeChecklist" value="1012"<%= metadataAgeListVector.toString().contains("1012") ? " checked" : "" %>  onclick="submitForm(document.<portlet:namespace />choseAgeChecklist);"> 10-12
						           <input type="checkbox" name="metadataAgeChecklist" value="1214"<%= metadataAgeListVector.toString().contains("1214") ? " checked" : "" %> onclick="submitForm(document.<portlet:namespace />choseAgeChecklist);"> 12-14
						           <input type="checkbox" name="metadataAgeChecklist" value="1416"<%= metadataAgeListVector.toString().contains("1416") ? " checked" : "" %> onclick="submitForm(document.<portlet:namespace />choseAgeChecklist);"> 14-16
						           <input type="checkbox" name="metadataAgeChecklist" value="1618"<%= metadataAgeListVector.toString().contains("1618") ? " checked" : "" %> onclick="submitForm(document.<portlet:namespace />choseAgeChecklist);"> 16-18
								</c:when>
								<c:otherwise>
						  		   <input type="checkbox" name="metadataAgeChecklist" value="1012" checked onclick="submitForm(document.<portlet:namespace />choseAgeChecklist);"> 10-12
						           <input type="checkbox" name="metadataAgeChecklist" value="1214" checked onclick="submitForm(document.<portlet:namespace />choseAgeChecklist);"> 12-14
						           <input type="checkbox" name="metadataAgeChecklist" value="1416" checked onclick="submitForm(document.<portlet:namespace />choseAgeChecklist);"> 14-16
						           <input type="checkbox" name="metadataAgeChecklist" value="1618" checked onclick="submitForm(document.<portlet:namespace />choseAgeChecklist);"> 16-18
								</c:otherwise>
							</c:choose>			        
						</form>
        			 </td>
				</tr>
				<tr>
					<td class="td-extra-settings">
						extra settings:
					</td>
					<td class="td-extra-settings">
						<portlet:actionURL var="hideExtras">
		          					    <portlet:param name="struts_action" value="/ext/asset_by_ratio/view" />
					                    <portlet:param name="showDurable" value="<%= String.valueOf(!showDurable) %>" />
					                    <portlet:param name="changeDurable" value="true" />
								</portlet:actionURL>
					

									<a href="<%= hideExtras %>" <%= onClickHtml %>>hide</a>

        			 </td>
				</tr>
			</table>
</div>



<c:if test="<%= !showDurable %>">
	<div id="showFadeInLink" class="div-fade-in">
		<portlet:actionURL var="linkButton">
		                    <portlet:param name="struts_action" value="/ext/asset_by_ratio/view" />
					        <portlet:param name="showDurable" value="true" />
					        <portlet:param name="changeDurable" value="true" />      	
		</portlet:actionURL>	
			
		<a id="<portlet:namespace />link3" href="<%= linkButton %>" <%= onClickHtml %>>show extra search options</a>
	</div>
</c:if>

<%
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



<c:if test="<%= results.size() < 1 %>">	
	<div class="div-no-result">
				<p>no results for search index</p>
	</div>
</c:if>


<script type="text/javascript">
jQuery(document).ready(function(){
    jQuery('#<portlet:namespace />link1').click(function() {
        jQuery('#<portlet:namespace />block3').fadeOut('slow');
		obj = document.getElementById('showFadeInLink');
		if (obj.style.display == "none"){
			obj.style.display = "";
		} else {
			obj.style.display = "none";
		}
     });
	jQuery('#<portlet:namespace />link3').click(function() {
            jQuery('#<portlet:namespace />block3').fadeIn(2000);	
            obj = document.getElementById('showFadeInLink');
    		if (obj.style.display == "none"){
    			obj.style.display = "";
    		} else {
    			obj.style.display = "none";
    		}		
         });
});
</script>
