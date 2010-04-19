<%@ include file="/html/portlet/init.jsp" %>
<%@ page import="com.liferay.portlet.tags.model.TagsAsset" %>
<%@ page import="com.ext.portlet.cart.model.Cart" %>
<%@ page import="com.liferay.portlet.tags.model.TagsAsset" %>
<%@ page import="com.liferay.portlet.tags.model.TagsEntry" %>
<%@ page import="com.liferay.portlet.tags.model.TagsEntryConstants" %>
<%@ page import="com.liferay.portlet.tags.model.TagsVocabulary" %>
<%@ page import="com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.tags.service.TagsEntryLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.tags.service.TagsVocabularyLocalServiceUtil" %>

<portlet:defineObjects />
<%
TagsAsset entry = (TagsAsset)request.getAttribute("assetEntry:view_addTag.jsp");
long entryId = BeanParamUtil.getLong(entry, request, "classPK");
String className = ParamUtil.getString(request, "className");
String struts_action = ParamUtil.getString(request, "struts_action");
String redirect = ParamUtil.getString(request, "redirect");
String onClickHtml = StringPool.BLANK;
onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";

%>
<div align="right">
	&laquo; <a href="<%= HtmlUtil.escape(redirect) %>"<%= onClickHtml %>><liferay-ui:message key="back" /></a>
</div>
		
		
<div style="text-align: center" >
  Add new Tag for Entry <%= entry.getTitle() %>
</div>

<br/><br/>


<div style="color: #12558E">
	<form action="<portlet:actionURL windowState="<%= WindowState.MAXIMIZED.toString() %>">
	                  <portlet:param name="struts_action" value="<%= struts_action %>" />
	        	      </portlet:actionURL>" method="post" name="<portlet:namespace />addTag">
				   	  <input name="<portlet:namespace />entryId" type="hidden" value="<%= String.valueOf(entryId) %>" />
				   	  <input name="<portlet:namespace />className" type="hidden" value="<%= className %>" />
				      		<liferay-ui:tags-selector
								className="<%= className %>"
								classPK="<%= 0 %>"
								hiddenInput="tagsEntries"
							/>
				      <input name="<portlet:namespace />cmd" type="hidden" value="<%= Constants.ADD %>" />
				      <input name="<portlet:namespace />redirect" type="hidden" value="<%= redirect %>" />
				      <input onClick="submitForm(document.<portlet:namespace />addTag);" style="margin-top 5px;" type="button" value="Save">
	</form>  
</div>


