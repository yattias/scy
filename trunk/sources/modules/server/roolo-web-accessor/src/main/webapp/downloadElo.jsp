

<%@page import="info.collide.sqlspaces.commons.util.Base64"%>
<%@ page import="eu.scy.server.exporter.*,java.net.URI,java.io.*"%>
<%
    String eloUrlAsBase64 = request.getParameter("eloUri").toString();
	byte[] eloUrlAsBytes = Base64.decode(eloUrlAsBase64);
	String eloUrl = new String(eloUrlAsBytes);
    eloUrl = eloUrl.replaceAll(" ", "+");
    RetrieveElos getElo = new RetrieveElos();
    String eloXML;
    eloXML = getElo.getXmlELO(new URI(eloUrl));
    PrintWriter pw = response.getWriter();

    if (eloXML == null) {
        eloXML = "This Elo(" + eloUrl + ") is not available on the server";
        pw.println("<html>");
        pw.println("<title>");
        pw.println("Elo with URL " + eloUrl + " is not available on this server...");
        pw.println("</title>");
        pw.println("<body>");
        pw.println("<h1>The requested ELO is not available on the server</h1>");
        pw.println("<h2>(Roolo URL: "+eloUrl+")</h2>");
        pw.println("</body>");
        pw.println("</html>");

    } else {
        response.reset();
        response.setContentType("application/xml");
        response.setHeader("Content-Disposition", "attachement; filename=" + eloUrl);
        pw.println(eloXML);

    }
%>


