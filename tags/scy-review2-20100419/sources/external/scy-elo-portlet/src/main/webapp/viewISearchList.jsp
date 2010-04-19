<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page import="com.liferay.portal.util.PortalUtil" %>
<%@ page import="com.liferay.portal.kernel.util.Validator" %>

<%@ page import="roolo.api.IExtensionManager" %>
<%@ page import="roolo.api.IRepository" %>
<%@ page import="roolo.api.search.ISearchResult" %>
<%@ page import="roolo.cms.repository.search.SearchResult" %>
<%@ page import="roolo.elo.api.IELO" %>
<%@ page import="roolo.elo.api.IMetadata" %>
<%@ page import="roolo.elo.api.IMetadataTypeManager" %>
<%@ page import="roolo.elo.api.metadata.CoreRooloMetadataKeyIds" %>
<%@ page import="roolo.elo.api.exceptions.EmptyKeyException" %>

<%@ page import="java.util.Enumeration" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.net.URI" %>
<%@ page import="java.util.GregorianCalendar" %>
<%@ page import="java.util.Date" %>

<%@ page import="javax.portlet.PortletRequest" %>
<%@ page import="javax.portlet.PortletSession" %>

<portlet:defineObjects />

<%
IMetadataTypeManager mtm = (IMetadataTypeManager)request.getAttribute("mtm");
List<IELO> iEloList = (List<IELO>) request.getAttribute("iEloList");
String currentURL = PortalUtil.getCurrentURL(request);

%>


<div>
	ROOLO SERVER is reachable :)
	<br />
	Elos on Server: <%= iEloList.size() %> 
</div>

<div class="separator"><!-- --></div>

<table class="sortable">
	<tr class="portlet-section-header results-header">
		<th width=300>
			<liferay-ui:message key="Title" />
		</th>
		<th width=120>Date</th>
		<th width=40>URI</th>
	</tr>
<%
	for (int i = 0; i  < iEloList.size(); i++) {
			 
		String title="";
		Long longDate = null;
		Long longModified = null;
		Date modifiedDate = null;
		Date createdDate =null;
		boolean showURI= false;
		Object objectTitle = null;
		List<Object> forkOfList = null;
		List<Object> forkByList = null;

		IELO elo = iEloList.get(i);				
		URI eloUri = elo.getUri();
		IMetadata metadata = elo.getMetadata();
		
		try{
			objectTitle = (Object) metadata.getMetadataValueContainer(mtm.getMetadataKey(CoreRooloMetadataKeyIds.TITLE)).getValue();
		}catch(EmptyKeyException eke){
			System.out.println(eke.getMessage());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}

		try{
			longDate = (Long) metadata.getMetadataValueContainer(mtm.getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED)).getValue();
		}catch(EmptyKeyException eke){
			System.out.println(eke.getMessage());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}

		try{
			longModified = (Long) metadata.getMetadataValueContainer(mtm.getMetadataKey(CoreRooloMetadataKeyIds.DATE_LAST_MODIFIED)).getValue();
		}catch(EmptyKeyException eke){
			System.out.println(eke.getMessage());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		try{
			forkOfList = (List<Object>) metadata.getMetadataValueContainer(mtm.getMetadataKey(CoreRooloMetadataKeyIds.IS_FORK_OF)).getValueList();		
		}catch(EmptyKeyException eke){
			System.out.println(eke.getMessage());
		}
		
		try{
			forkByList = (List<Object>) 				metadata.getMetadataValueContainer(mtm.getMetadataKey(CoreRooloMetadataKeyIds.IS_FORKED_BY)).getValueList();	
		}catch(EmptyKeyException eke){
			System.out.println(eke.getMessage());
		}
				
		if(longModified != null){
			modifiedDate = new Date();
			modifiedDate.setTime(longModified);				 						
		}else if(longModified == null && longDate != null){
			createdDate = new Date();
			createdDate.setTime(longDate);
		}else{
			createdDate = null;
		}
				
		try{
			title = objectTitle.toString();
		}catch(Exception e){
			title = "no String title"; 
		}
				
	
		String style = "class=\"portlet-section-body results-row\" onmouseover=\"this.className = 'portlet-section-body-hover results-row hover';\" onmouseout=\"this.className = 'portlet-section-body results-row';\"";
			
%>

		<portlet:renderURL var="viewCartEntryList">		        
	          	<portlet:param name="eloUri" value="<%= elo.getUri().toString() %>" />                  	
	          	<portlet:param name="redirect" value="<%= currentURL %>" />                  	
		</portlet:renderURL>
		

<%
			String onClickHtml = "onclick=\"location.href='" + viewCartEntryList +"';";
%>

		<c:if test="<%= forkOfList.size() == 0 && forkByList.size() == 0 || forkOfList.size() == 0 && forkByList.size() >= 0 %>">
  			<tr <%= style %>>
				<td  width=40 onclick="location.href='<portlet:renderURL><portlet:param name="eloUri" value="<%= elo.getUri().toString() %>" /><portlet:param name="redirect" value="<%= currentURL %>" /></portlet:renderURL>'">  
					   <%= title %>
				   <div id="<%= "div" + i %>" style="display: none;"><%= eloUri.toString() %></div>
				</td>
				<td> 			
					<c:if test="<%= longModified != null %>">
						<fmt:formatDate value="<%= modifiedDate %>" type="both" pattern="yyyy-dd-MMM HH:mm"/>
					</c:if>			
					<c:if test="<%= longModified == null && longDate != null %>">
						<fmt:formatDate value="<%= createdDate %>" type="both" pattern="yyyy-dd-MMM HH:mm"/>
					</c:if>			
				</td>	
				<td>
					<a href="#" onclick="showhide('<%= "div" + i %>');">Show</a>
				</td>
			</tr>			
		</c:if>		
<%
	}
%>            
</table>
	
	
This is the <b>Sample JSP Portlet</b>. Use this as a quick way to include JSPs. <%= currentURL %>


 <form action="<portlet:actionURL>
   		<portlet:param name="page" value="mainview"/>
   		<portlet:param name="redirect" value="<%= currentURL %>" />         
   </portlet:actionURL>"
         method="POST">
      Name:<br/>
      <input type="text" name="yourname"/>
   </form>
   <br/>
   You can also link to other pages, using a renderURL, like <a
      href="<portlet:renderURL><portlet:param name="yourname" value="Roy Russo"></portlet:param></portlet:renderURL>">this</a>.
</div>




<script type="text/javascript">
function showhide(id){
	if (document.getElementById){
		obj = document.getElementById(id);
		if (obj.style.display == "none"){
			obj.style.display = "";
		} else {
			obj.style.display = "none";
		}
	}
}
</script> 

