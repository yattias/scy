<%@ page import="eu.scy.server.exporter.*"%>
<%
	    String portStr = request.getParameter("port");
	    if (portStr == null || portStr.length() == 0) {
	        portStr = "2525";
	    }
%>
<html>
<head>
<title>SCY Action log and ELO Exporter</title>
<script type="text/javascript">
	function submitForm() {
		var formats = document.forms["format"].type;
		for (var i = 0; i < formats.length; i++) {
			if (formats[i].checked) {
				document.forms["form"].action = formats[i].value;
				setLoadAnimation();
				window.setTimeout("document.forms['form'].submit()", 100);
			}
		} 
	}
	function refresh() {
		document.forms["refresh"].port.value = document.forms["form"].port.value;
		document.forms["refresh"].submit();
	}
	function setLoadAnimation() {
		var bt = document.getElementById("submitbutton");
		var parent = bt.parentNode;
		parent.removeChild(bt);
		var imgNode = document.createElement("img");
		imgNode.src = "table-images/wait.gif";
		parent.appendChild(imgNode);
	}
	
	//-->
</script>
<style type="text/css">
<!--
@import url("style.css");
-->
</style>
<style type="text/css">
select { width: 100%; }
</style>

</head>
<body>
<a href="index.jsp?port=<%= portStr %>"><img id="logo" src="table-images/scylogo.gif" height="109" width="192" /></a>
<h1>Welcome to the SCY action log and ELO exporter</h1>
<p>Choose the desired format and optionally a list of users that should be only taken into account.</p>
<table id="rounded-corner">
<tr><th>Format</th><th>Filtering</th></tr>
<tr><td class="format">
<form name="format">
    <input type="radio" name="type" value="display.jsp" checked="checked"> Just display logs<br/>
    <span class="comment">This will just display all action events from the log in a browser. Also the referenced ELOs can be downloaded.</span><br/>
    <input type="radio" name="type" value="download.jsp"> Download only logs<br/>
    <span class="comment">This will produce an XML file that contains all action events from the log.</span><br/>
    <input type="radio" name="type" value="downloadAll.jsp"> Download logs with ELOs<br/>
    <span class="comment">This will produce a ZIP file that contains all action events from the log as a single XML file and the referenced ELOs as ELO-XML files.</span><br/>
</form>
</td><td>
<form method="get" name="form">
<table>
	<tr>
		<td>Port:</td>
		<td><input name="port" value="<%= portStr %>" /></td>
	</tr>
	<tr>
		<td>User:</td>
		<td><select multiple="multiple" name="user" size="10" width="100%">
			<%
		        int port = Integer.parseInt(portStr);
			    Converter c = new Converter(port);
			    String[] users = c.getUsers();
			    for (String user : users) {
			        out.println("<option value=\"" + user + "\">" + user + "</option>");
			    }
			%>
		</select></td>
	</tr>
</table>
</form>
<form name="refresh">
<input type="hidden" name="port" />
<center><a href="javascript:refresh();">Refresh user</a></center>
</form>
</td></tr>
<tr><td colspan="2" align="center"><input <%= c.isConnected() ? "" : "disabled=\"disabled\""%> id="submitbutton" type="button" value="Do it!" onclick="javascript:submitForm();"/></td></tr>
</table>

</body>