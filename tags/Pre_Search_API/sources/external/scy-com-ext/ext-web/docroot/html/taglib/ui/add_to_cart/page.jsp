<%@ include file="/html/taglib/init.jsp" %>
<%@ page import="com.liferay.portal.kernel.util.StringPool" %>
<%@ page import="com.liferay.util.PermissionUtil" %>

<%
String className = (String)request.getAttribute("liferay-ui-ext:add-to-cart:className");
long classPK = GetterUtil.getLong((String)request.getAttribute("liferay-ui-ext:add-to-cart:classPK"));
PortletURL portletURL = (PortletURL)request.getAttribute("liferay-ui-ext:liferay-ui-ext:add-to-cart:portletURL");
String struts_action = (String)request.getAttribute("liferay-ui-ext:add-to-cart:strutsAction");
String redirect = currentURL;
String portletModelName = PermissionUtil.portletModelName(className);
boolean showAddClipboardButton = PermissionUtil.contains(permissionChecker, portletModelName, scopeGroupId, "ADD_CLIPBOARD");
%>

<portlet:actionURL var="AddCartURL">
                    <portlet:param name="struts_action" value="<%= struts_action  %>" />
                    <portlet:param name="entryId" value="<%= String.valueOf(classPK) %>" />      
                    <portlet:param name="className" value="<%= className %>" />  
                    <portlet:param name="redirect" value="<%= redirect %>" />   
</portlet:actionURL>


<%
String onClickHtml = StringPool.BLANK;

if ((AddCartURL.startsWith(Http.HTTP_WITH_SLASH) ||AddCartURL.startsWith(Http.HTTPS_WITH_SLASH))) {
	onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";
}
boolean urlIsNotNull = Validator.isNotNull(AddCartURL);

%>


<c:if test="<%= showAddClipboardButton %>">     
   <c:if test="<%= urlIsNotNull %>">
        <a style="text-decoration: none;" href="<%= AddCartURL %>" <%= onClickHtml %>>
   		 </c:if><input type="button" value="Cart" title="save-to-own-cart">
    </a>
</c:if>