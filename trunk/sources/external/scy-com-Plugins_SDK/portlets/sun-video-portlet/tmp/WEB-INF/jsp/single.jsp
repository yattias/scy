<%@ page import="com.sun.portal.videoportlet.*" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>

<portlet:defineObjects/>

<%
String video_id = (String)renderRequest.getParameter(Constants.SELECTED_VIDEO);
%>

<!--
Prefer size style="width:425px; height:350px;"
-->
<div>
<p align="center">
<object type="application/x-shockwave-flash"  style="width:284px; height:234px;" data="http://www.youtube.com/watch?v=S2HIda5wSVU"><param name="movie" value="http://www.youtube.com/watch?v=S2HIda5wSVU" /></object>
</p>
</div>

<p align="center"><b>
<a href='<portlet:renderURL>
         <portlet:param name="<%=Constants.VIEW_TYPE%>" value="<%=Constants.VIEW_TYPE_DEFAULT%>"/>
         </portlet:renderURL>'
>Back </a>
</b></p>

