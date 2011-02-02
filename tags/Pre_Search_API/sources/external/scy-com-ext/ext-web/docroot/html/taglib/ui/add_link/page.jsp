<%@ include file="/html/taglib/init.jsp" %>
<%@ page import="com.liferay.portal.kernel.util.StringPool" %>
<%@ page import="com.liferay.util.PermissionUtil" %>

<%
String className = (String)request.getAttribute("liferay-ui-ext:add-link:className");
long classPK = GetterUtil.getLong((String)request.getAttribute("liferay-ui-ext:add-link:classPK"));
String message = GetterUtil.getString((String)request.getAttribute("liferay-ui-ext:add-link:message"), StringPool.BLANK);
PortletURL portletURL = (PortletURL)request.getAttribute("liferay-ui-ext:liferay-ui-ext:add-link:portletURL");
String struts_action = (String)request.getAttribute("liferay-ui-ext:add-link:strutsAction");
String portletModelName = PermissionUtil.portletModelName(className);
boolean showAddLinkButton = PermissionUtil.contains(permissionChecker, portletModelName, scopeGroupId, "LINK");

%>

<portlet:actionURL var="linkButton">
                    <portlet:param name="struts_action" value="<%= struts_action  %>" />
                    <portlet:param name="entryId" value="<%= String.valueOf(classPK) %>" />
                  	<portlet:param name="className" value="<%= className %>" />
                  	<portlet:param name="redirect" value="<%= currentURL %>" />               	
</portlet:actionURL>

<%
String onClickHtml = StringPool.BLANK;

if ((linkButton.startsWith(Http.HTTP_WITH_SLASH) || linkButton.startsWith(Http.HTTPS_WITH_SLASH))) {
	onClickHtml = "onclick=\"Liferay.Util.forcePost(this); return false;\"";
}
boolean urlIsNotNull = Validator.isNotNull(linkButton);
%>

  
<c:if test="<%= showAddLinkButton %>">       
 	<c:if test="<%= urlIsNotNull %>">
	    <a style="text-decoration: none;" href="<%= linkButton %>" <%= onClickHtml %>>
	 </c:if><input type="button" value="Link">
	    </a>
  </c:if>
    
				