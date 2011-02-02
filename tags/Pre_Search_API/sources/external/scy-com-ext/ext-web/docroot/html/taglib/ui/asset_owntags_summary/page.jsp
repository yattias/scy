<%@ include file="/html/taglib/init.jsp" %>
<%@ page import="com.liferay.portlet.tags.model.TagsEntry" %>
<%@ page import="com.liferay.portlet.tags.service.TagsEntryLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.tags.model.TagsProperty" %>
<%@ page import="com.liferay.portlet.tags.service.TagsPropertyLocalServiceUtil" %>
<%@ page import="com.liferay.util.PermissionUtil" %>
<%@ page import="com.liferay.portlet.tags.model.TagsAsset"%>
<%@ page import="com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil"%>

<%
String className2 = (String)request.getAttribute("liferay-ui-ext:asset-owntags-summary:className");
long classPK2 = GetterUtil.getLong((String)request.getAttribute("liferay-ui-ext:asset-owntags-summary:classPK"));
String message2 = GetterUtil.getString((String)request.getAttribute("liferay-ui-ext:asset-owntags-summary:message"), StringPool.BLANK);
PortletURL portletURL2 = (PortletURL)request.getAttribute("liferay-ui-ext:asset-owntags-summary:portletURL");
String struts_action = (String)request.getAttribute("liferay-ui-ext:add-own-tags:strutsAction");
String portletModelName = PermissionUtil.portletModelName(className2);
boolean showDeleteTagButton = PermissionUtil.contains(permissionChecker, portletModelName, scopeGroupId, "DELETE_TAG");

List<TagsEntry> tagsList = TagsEntryLocalServiceUtil.getEntries(className2, classPK2);
List<TagsEntry> ownTags = new ArrayList<TagsEntry>();
List<TagsEntry> ownTagsProperties = new ArrayList<TagsEntry>();

TagsAsset asset = TagsAssetLocalServiceUtil.getAsset(className2, classPK2);
String assetId = String.valueOf(asset.getAssetId());

for(TagsEntry tag: tagsList){
	try{
		List<TagsProperty> tagsPropertiesList = TagsPropertyLocalServiceUtil.getProperties(tag.getEntryId());
		for (TagsProperty tagsProperty : tagsPropertiesList) {
			if(themeDisplay.getUserId() == tagsProperty.getUserId() && tagsProperty.getValue().equals(assetId)){
				ownTags.add(tag);
			}
		}
		
		if(tagsPropertiesList.size() == 0){
			System.out.println("no tags properties found for TagsEntry with id: " + tag.getEntryId());
			TagsProperty tagProperty =  TagsPropertyLocalServiceUtil.addProperty(tag.getUserId(), tag.getEntryId(), String.valueOf(tag.getUserId()) + assetId + String.valueOf(tag.getEntryId()), assetId);
			System.out.println("new property created with propertyId: " + tagProperty.getPropertyId());
			
		}
		
	}catch (Exception e) {
		System.out.println("no tags properties found for TagsEntry with id: " + tag.getEntryId());
	}
}



%>

<c:if test="<%= showDeleteTagButton && ownTags.size() > 0 %>">
	<br/>
	<div style="color: #12558E">Eigene Tags:

      <c:if test="<%= !ownTags.isEmpty() %>">
      		<%= Validator.isNotNull(message2) ? (LanguageUtil.get(pageContext, message2) + ": ") : "" %>
      
      		<c:choose>
      			<c:when test="<%= portletURL2 != null %>">
      
      				<%
      				for (TagsEntry tag : ownTags) {
      					portletURL2.setParameter("tag", tag.getName());
      				%>  
                    
                  
                  		<portlet:actionURL var="deleteEntryURL">
                	 		    <portlet:param name="struts_action" value="<%= struts_action  %>" />
                			  	<portlet:param name="entryId" value="<%= String.valueOf(classPK2) %>" />
                	 		  	<portlet:param name="tagId" value="<%= String.valueOf(tag.getEntryId()) %>" />
                	 		  	<portlet:param name="className" value="<%= className2 %>" />
                	 		  	<portlet:param name="redirect" value="<%= currentURL %>" />
                	     	</portlet:actionURL>
                     	<liferay-ui:icon-delete url="<%= deleteEntryURL %>" />
                     	
                     	<span class="tag"><%= tag.getName() %></span>


      				<%
      				}
      				%>
      
      			</c:when>
      			<c:otherwise>
      
      				<%
      				for (TagsEntry tag : ownTags) {
      				%>
      	
      
      
      					<span class="tag"><%= tag.getName() %></span>
 
      				<%
      				}
      				%>
      
      			</c:otherwise>
      		</c:choose>
      </c:if>
    </div>
</c:if>
  

