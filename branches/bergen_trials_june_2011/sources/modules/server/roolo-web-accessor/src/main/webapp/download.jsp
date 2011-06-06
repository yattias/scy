<%@ page import="eu.scy.server.exporter.*,java.io.*" %>

<% 
	int port = Integer.parseInt(request.getParameter("port"));
	Converter c = new Converter(port, request.getParameterValues("user"));
	String logs = c.convertToString();
	
	 PrintWriter pw = response.getWriter();
	 response.reset();
	        response.setContentType("application/xml");
	        response.setHeader("Content-Disposition", "attachement; filename=actionLogs.xml" );
	        pw.println(logs);
%>

