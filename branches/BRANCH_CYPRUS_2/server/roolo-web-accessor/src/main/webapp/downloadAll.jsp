<%@ page import="eu.scy.server.exporter.*,java.io.*" %>

<% 
	int port = Integer.parseInt(request.getParameter("port"));
	Converter c = new Converter(port, request.getParameterValues("user"));
	byte[] logs = c.exportZip();
	
	 OutputStream pw = response.getOutputStream();
	 response.reset();
	        response.setContentType("application/zip");
	        response.setHeader("Content-Disposition", "attachement; filename=actionLogsAndElos.zip" );
	        pw.write(logs);
%>

