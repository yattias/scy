<%@ page import="com.sun.portal.videoportlet.*" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>

<portlet:defineObjects/>

<%
String video_id = (String)portletConfig.getPortletContext().getAttribute(Constants.CONTEXT_VIDEO_ID);
%>
<!--
Prefer size style="width:425px; height:350px;"
-->
<div>
<p align="center">
<object type="application/x-shockwave-flash"  style="width:284px; height:234px;" data="http://www.youtube.com/v/<%=video_id%>"><param name="movie" value="http://www.youtube.com/v/<%=video_id%>" /></object>
</p>
</div>

