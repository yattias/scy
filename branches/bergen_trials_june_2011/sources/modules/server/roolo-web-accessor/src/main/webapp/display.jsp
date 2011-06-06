<%@ page import="eu.scy.server.exporter.*,java.util.*" %>
<%
	    String portStr = request.getParameter("port");
	    if (portStr == null || portStr.length() == 0) {
	        portStr = "2525";
	    }
		int port = Integer.parseInt(request.getParameter("port"));
		Converter c = new Converter(port, request.getParameterValues("user"));
		%>
<script src="toggleWidth.js"></script>
<script src="downloadElo.js"></script>
<style type="text/css">
<!--
@import url("style.css");
-->
</style>
<html>
<head>


<title>
Display ActionLogs
</title>
</head>
<body>
<a href="index.jsp?port=<%= portStr %>"><img id="logo" src="table-images/scylogo.gif" height="109" width="192" /></a>
<% c.convertToHTMLTable(out); %>
</body>
