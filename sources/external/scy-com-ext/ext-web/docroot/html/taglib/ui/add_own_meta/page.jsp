<%@ include file="/html/taglib/init.jsp" %>
<%@ page import="com.liferay.portlet.tags.model.TagsEntry" %>
<%@ page import="com.liferay.portlet.tags.service.TagsEntryLocalServiceUtil" %>
<%@ page import="com.liferay.portal.kernel.util.StringPool" %>
<%@ page import="com.liferay.util.PermissionUtil" %>


<%
String className = (String)request.getAttribute("liferay-ui-ext:add-own-meta:className");
long classPK = GetterUtil.getLong((String)request.getAttribute("liferay-ui-ext:add-own-meta:classPK"));
String message = GetterUtil.getString((String)request.getAttribute("liferay-ui-ext:add-own-meta:message"), StringPool.BLANK);
PortletURL portletURL = (PortletURL)request.getAttribute("liferay-ui-ext:liferay-ui-ext:add-own-meta:portletURL");
String struts_action = (String)request.getAttribute("liferay-ui-ext:add-own-meta:strutsAction");
String redirect = currentURL;
String portletModelName = PermissionUtil.portletModelName(className);
boolean showEditMetaButton = PermissionUtil.contains(permissionChecker, portletModelName, scopeGroupId, "EDIT_META");

%>

		<portlet:actionURL var="deleteEntryURL">
                    <portlet:param name="struts_action" value="<%= struts_action  %>" />
                    <portlet:param name="entryId" value="<%= String.valueOf(classPK) %>" />
                  	<portlet:param name="cmd" value="<%= Constants.VIEW %>" />
                  	<portlet:param name="className" value="<%= className %>" />
                  	<portlet:param name="redirect" value="<%= redirect %>" />
		</portlet:actionURL>

<%
String onClickHtml = StringPool.BLANK;

if ((deleteEntryURL.startsWith(Http.HTTP_WITH_SLASH) || deleteEntryURL.startsWith(Http.HTTPS_WITH_SLASH))) {
	onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";
}
boolean urlIsNotNull = Validator.isNotNull(deleteEntryURL);
%>

  
	 <c:if test="<%= showEditMetaButton %>">
       <c:if test="<%= urlIsNotNull %>">
        <a style="text-decoration: none;" href="<%= deleteEntryURL %>" <%= onClickHtml %>>
           </c:if><input type="button" value="Edit Metadata">
        </a>
     </c:if>
