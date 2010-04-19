<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page import="com.liferay.portal.util.PortalUtil" %>
<%@ page import="com.liferay.portal.kernel.util.*" %>

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
String redirect = (String) request.getAttribute("redirect");
IELO ielo = (IELO) request.getAttribute("ielo");
IMetadataTypeManager mtm = (IMetadataTypeManager)request.getAttribute("mtm");

String onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";
IMetadata metadata = ielo.getMetadata();

String title="";
String contentLanguage="";
String description="";
String technicalFormat="";
String version="";

Date createdDate= null;
Date modifiedDate= null;

Object objectTitle = null;
Object objectContentLanguage = null;
Object objectDescription = null;
Object objectTechnicalFormat = null;
Object objectVersion = null;

Long longDate = null;
Long longModified = null;

List<Object> forkOfList = null;

try{
	objectTitle = (Object) metadata.getMetadataValueContainer(mtm.getMetadataKey(CoreRooloMetadataKeyIds.TITLE)).getValue();
}catch(EmptyKeyException eke){
	System.out.println(eke.getMessage());
}



try{
	longDate = (Long) metadata.getMetadataValueContainer(mtm.getMetadataKey(CoreRooloMetadataKeyIds.DATE_CREATED)).getValue();
}catch(EmptyKeyException eke){
	System.out.println(eke.getMessage());
}

try{
	longModified = (Long) metadata.getMetadataValueContainer(mtm.getMetadataKey(CoreRooloMetadataKeyIds.DATE_LAST_MODIFIED)).getValue();
}catch(EmptyKeyException eke){
	System.out.println(eke.getMessage());
}

try{
	objectDescription = (Object) metadata.getMetadataValueContainer(mtm.getMetadataKey(CoreRooloMetadataKeyIds.DESCRIPTION)).getValue();
	description = objectDescription.toString();
}catch(EmptyKeyException eke){
	System.out.println(eke.getMessage());
}catch(Exception e){
	description = ""; 
}

try{
	objectTechnicalFormat = (Object) metadata.getMetadataValueContainer(mtm.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT)).getValue();
	technicalFormat = objectTechnicalFormat.toString();
}catch(EmptyKeyException eke){
	System.out.println(eke.getMessage());
}catch(Exception e){
	technicalFormat = ""; 
}

try{
	objectVersion = (Object) metadata.getMetadataValueContainer(mtm.getMetadataKey(CoreRooloMetadataKeyIds.VERSION)).getValue();
	version = objectVersion.toString();
}catch(EmptyKeyException eke){
	System.out.println(eke.getMessage());
}catch(Exception e){
	version = ""; 
}

try{
	forkOfList = (List<Object>) metadata.getMetadataValueContainer(mtm.getMetadataKey(CoreRooloMetadataKeyIds.IS_FORK_OF)).getValueList();		
}catch(EmptyKeyException eke){
	System.out.println(eke.getMessage());
}

try{
	title = objectTitle.toString();
}catch(Exception e){
	title = ""; 
}

try{
	contentLanguage = objectContentLanguage.toString();
}catch(Exception e){
	contentLanguage = ""; 
}

if(longDate != null){
	createdDate = new Date(longDate);
}

if(longModified != null){
	modifiedDate = new Date(longModified);
}


%>

<div align="right">
	&laquo; <a href="<%= HtmlUtil.escape(redirect) %>"<%= onClickHtml %>><liferay-ui:message key="back" /></a>
</div>

<div>
	<h1>Elo:</h1>

	<ul>
		<li>Title: <%= title %></li>
		<li>Created Date: <c:if test="<%= createdDate != null %>">  <fmt:formatDate value="<%= createdDate %>" type="both" pattern="dd-MMM-yyyy HH:mm"/> </c:if></li>
		<li>Modified Date: <c:if test="<%= modifiedDate != null %>">  <fmt:formatDate value="<%= modifiedDate %>" type="both" pattern="dd-MMM-yyyy HH:mm"/> </c:if></li>
		<li>Description: <%= description %></li>
		<li>URI: <%= ielo.getUri() %></li>
		<li>Size: <%= ielo.getContent().getSize() %></li>
		<li>Content Language: <%= contentLanguage %></li>
		<li>Technical Format: <%= technicalFormat %></li>
		<li>Version: <%= version %></li>
	</ul>
</div>
