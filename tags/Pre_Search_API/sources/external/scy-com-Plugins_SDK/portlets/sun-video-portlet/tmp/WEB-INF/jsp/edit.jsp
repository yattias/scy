<%@ page import="com.sun.portal.videoportlet.*" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>

<portlet:defineObjects/>
<p align="center">
<%
   UserConfig cfg = (UserConfig)renderRequest.getPortletSession().getAttribute(Constants.SESSION_CONFIG_OBJECT);
   String loginID      = (cfg == null)? "":cfg.getUserID();
%>

<form action='<portlet:actionURL/>' method="post">
<table align="center" cellpadding="1" cellspacing="1">
        <tr><td>
            Login ID:<input type="text" name="<%=Constants.TXT_LOGIN_ID%>" value="<%=loginID%>" />
            </td>
        </tr>
        <tr>
            <td>
            <input type="submit" value="SAVE" />
            </td>
        </tr>        
    </table>
    <input type="hidden" name="<%=Constants.PORTLET_ACTION%>" value="<%=Constants.ACTION_CONFIG%>" />
</form>
</p>
