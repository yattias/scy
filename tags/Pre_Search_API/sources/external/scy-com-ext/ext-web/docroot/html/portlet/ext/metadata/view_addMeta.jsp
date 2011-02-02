<%@ include file="/html/portlet/image_gallery/init.jsp" %>
<%@ page import="com.liferay.util.MetadataActionUtil" %>
<%@ page import="com.ext.portlet.metadata.model.MetadataEntry" %>
<%@ page import="com.liferay.portlet.tags.model.TagsAsset" %>

<%
TagsAsset entry = (TagsAsset)request.getAttribute("assetEntry:view_addMeta.jsp");
long entryId = BeanParamUtil.getLong(entry, request, "classPK");
String className = ParamUtil.getString(request, "className");
String struts_action = ParamUtil.getString(request, "struts_action");
String redirect = ParamUtil.getString(request, "redirect");
String onClickHtml = StringPool.BLANK;
onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";

MetadataEntry metadataEntry = (MetadataEntry)request.getAttribute("meta");
%>
<div style="text-align: center" >
  Edit Metadata for Entrie <%= entry.getTitle() %>
</div>

<div align="right">
	&laquo; <a href="<%= HtmlUtil.escape(redirect) %>"<%= onClickHtml %>><liferay-ui:message key="back" /></a>
</div>
		
<br/><br/>
<form action="<portlet:actionURL windowState="<%= WindowState.MAXIMIZED.toString() %>">
                  <portlet:param name="struts_action" value="<%= struts_action %>" />
              </portlet:actionURL>"
                method="post" name="<portlet:namespace />fm">
                 <input name="<portlet:namespace />entryId" type="hidden" value="<%= String.valueOf(entryId) %>" />
                 <input name="<portlet:namespace />className" type="hidden" value="<%= className %>" />
                 <input name="<portlet:namespace />cmd" type="hidden" value="<%= Constants.ADD %>" />                 
                 <input name="<portlet:namespace />redirect" type="hidden" value="<%= redirect %>" />            
               
                 <%@ include file="/html/portlet/ext/metadata/view_addMeta.jspf" %>

                 <div style="color: #12558E">
                       <input onClick="submitForm(document.<portlet:namespace />fm);" style="margin-top 5px;" type="button" value="Update Metadata">
                 </div>
</form>  


