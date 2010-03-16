<%@ include file="/html/portlet/ext/similarity/init.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import=" com.ext.portlet.similarity.action.*" %>


<%
TagsAsset assetEntry = (TagsAsset)request.getAttribute("entry");
String entryId = (String)request.getAttribute("entryId");
String className = (String)request.getAttribute("className");
ArrayList simValueList = (ArrayList)request.getAttribute("simValueList");
ArrayList simDateList = (ArrayList)request.getAttribute("simDateList");
ArrayList simViewCountList = (ArrayList)request.getAttribute("simViewCountList");
Long classPK = Long.valueOf(entryId);
String title = StringPool.BLANK;
String summary = StringPool.BLANK;
String viewURL = StringPool.BLANK;
String viewURLMessage = StringPool.BLANK;
String cssClassName = StringPool.BLANK;
String sType= (String)request.getAttribute("sType");


Vector checkListVector = new Vector();
String[] checklist = (String[]) request.getAttribute("checklist");
 if (checklist != null)
   {
      for (int i=0; i<checklist.length; i++)
      {
         checkListVector.add(checklist[i]);
      }
   }


Boolean isSortByValue=false;
Boolean isSortByDate=false;
Boolean isSortByViewCount=false;
if(sType == null){
	sType="value";	
	isSortByValue=true;
}else if(sType.equals("latestDate")){
	isSortByDate=true;
}else if(sType.equals("mostViews")){
	isSortByViewCount=true;
}else{
	isSortByValue=true;
}

String simMeasure = (String)request.getAttribute("simMeasure");
PortletPreferences prefs = renderRequest.getPreferences();
String[] measureMethods = prefs.getValues("measures", new String[]{});
if (Validator.isNull(simMeasure)) {
	simMeasure = SimilarityMeasureBuilder.SIMMEASURE_DEFAULT;
}

boolean show = true;

PortletURL viewFullContentURL = renderResponse.createRenderURL();

viewFullContentURL.setParameter("struts_action", "/ext/similarity/view_content");
viewFullContentURL.setParameter("assetId", String.valueOf(assetEntry.getAssetId()));
%>

<div style="text-align: center" >
 Similarity Portlet

 <br/><br/>
</div>


		     
			  

<div style="text-align: center" >
	<form action="<portlet:actionURL>
				                    <portlet:param name="struts_action" value="/ext/similarity/view_sim" />
				                    <portlet:param name="entryId" value="<%= String.valueOf(classPK) %>" />
				                  	<portlet:param name="className" value="<%= className %>" />
				                  	<portlet:param name="redirect" value="<%= currentURL %>" />      
				   </portlet:actionURL>" method="post" name="<portlet:namespace />choseType">			                
										 Sortieroption:
										 <select name="sType" size="1" onChange="submitForm(document.<portlet:namespace />choseType);">
							             <option value="mostRecent"<%= "mostRecent".equals(sType) ? " selected" : "" %>>Most Recent</option>
							             <option value="latestDate"<%= "latestDate".equals(sType) ? " selected" : "" %>>Latest Date</option>
							             <option value="mostViews"<%= "mostViews".equals(sType) ? " selected" : "" %>>Most views</option>
	         							  </select>
	</form>
</div>

<br/>

<div style="text-align: center" >
	<c:if test="<%= measureMethods.length > 0 %>">	
		<form action="<portlet:actionURL>
					                	<portlet:param name="struts_action" value="/ext/similarity/view_sim" />
					                    <portlet:param name="entryId" value="<%= String.valueOf(classPK) %>" />
					                  	<portlet:param name="className" value="<%= className %>" />
					                  	<portlet:param name="redirect" value="<%= currentURL %>" />  
					   </portlet:actionURL>" method="post" name="<portlet:namespace />choseMeasureMethod">			                             
					                         SimMeasure: 			                         
					                         <select name="simMeasure" size="1" onChange="submitForm(document.<portlet:namespace />choseMeasureMethod);">
		<% 										
												for (String measureMethod : measureMethods) {
		%>		
			                 						<option value="<%= measureMethod %>"<%= measureMethod.equals(simMeasure) ? " selected" : "" %>><%= LanguageUtil.get(request.getLocale(), measureMethod) %></option>
		<%
												 }
		%>
										      </select>
		</form>  
	</c:if>		
	
	
	<c:if test="<%= simMeasure.equals(SimilarityMeasureBuilder.SIMMEASURE_DEFAULT) %>">	
		<form action="<portlet:actionURL>
					                    <portlet:param name="struts_action" value="/ext/similarity/view_sim" />
					                    <portlet:param name="entryId" value="<%= String.valueOf(classPK) %>" />
					                  	<portlet:param name="className" value="<%= className %>" />
					                  	<portlet:param name="redirect" value="<%= currentURL %>" />      
					   </portlet:actionURL>" method="post" name="<portlet:namespace />choseChecklist">		
		
					<c:choose>
						<c:when test="<%= checkListVector.size() > 0 %>">
				  		   <input type="checkbox" name="checklist" value="tags"<%= checkListVector.toString().contains("tags") ? " checked" : "" %>  onChange="submitForm(document.<portlet:namespace />choseChecklist);"> Tags
				           <input type="checkbox" name="checklist" value="user"<%= checkListVector.toString().contains("user") ? " checked" : "" %> onChange="submitForm(document.<portlet:namespace />choseChecklist);"> User
				           <input type="checkbox" name="checklist" value="group"<%= checkListVector.toString().contains("group") ? " checked" : "" %> onChange="submitForm(document.<portlet:namespace />choseChecklist);"> Group
						</c:when>
						<c:otherwise>
				  		   <input type="checkbox" name="checklist" value="tags" checked onChange="submitForm(document.<portlet:namespace />choseChecklist);"> Tags
				           <input type="checkbox" name="checklist" value="user" checked onChange="submitForm(document.<portlet:namespace />choseChecklist);"> User
				           <input type="checkbox" name="checklist" value="group" checked onChange="submitForm(document.<portlet:namespace />choseChecklist);"> Group
						</c:otherwise>
					</c:choose>
		  	
		             
		</form>
	</c:if>		
</div>

 			
<br/>

	<%@ include file="/html/portlet/ext/similarity/resourceView/startResourceView.jspf" %>		
	

<c:if test="<%= isSortByValue %>">	
			
	<%@ include file="/html/portlet/ext/similarity/resourceView/simSortByValueView.jspf" %>		

</c:if>		
	
<c:if test="<%= isSortByDate %>">	

	<%@ include file="/html/portlet/ext/similarity/resourceView/simSortByDateView.jspf" %>		
			
</c:if>		
	
<c:if test="<%= isSortByViewCount %>">	
	
	<%@ include file="/html/portlet/ext/similarity/resourceView/simSortByViewCountView.jspf" %>		

</c:if>	