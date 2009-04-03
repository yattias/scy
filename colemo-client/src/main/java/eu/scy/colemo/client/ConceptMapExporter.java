package eu.scy.colemo.client;

import java.util.Iterator;
import java.io.*;

import eu.scy.colemo.server.uml.UmlLink;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.XMLStreamException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 25.mar.2009
 * Time: 13:03:35
 * To change this template use File | Settings | File Templates.
 */
public class ConceptMapExporter {

    private static ConceptMapExporter defaultInstance;

    private XMLStreamWriter xtw;
    private StringWriter sw;

    private ConceptMapExporter() {
        initialize();
    }

    public static ConceptMapExporter getDefaultInstance() {
        if (defaultInstance == null) defaultInstance = new ConceptMapExporter();
        return defaultInstance;
    }

    public void initialize() {
        try {
            System.out.println("INITIALIZING....");
            sw = new StringWriter();
            XMLOutputFactory xof = XMLOutputFactory.newInstance();
            xtw = xof.createXMLStreamWriter(sw);
        } catch (XMLStreamException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String createXML() {
        try {

            createDocumentStart();

            Iterator nodeIterator = getNodeIterator();
            parseNodes(nodeIterator);

            Iterator linkIterator = getLinkIterator();
            parseLinks(linkIterator);


            createDocumentEnd();

        } catch (XMLStreamException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        return sw.toString();


        //return createXMLString(xmldoc);
    }

    private void createDocumentEnd() throws XMLStreamException {
        System.out.println("CREATING DOCUMNENT END!");
        xtw.writeEndElement();
        xtw.writeEndDocument();
        xtw.flush();
        xtw.close();
    }

    private void createDocumentStart() throws XMLStreamException {
        System.out.println("CREATING DOCUMENT START!");
        xtw.writeStartDocument();
        xtw.writeStartElement("conceptmap");
    }


    public String createXML(Iterator nodes, Iterator links) {
        try {
            createDocumentStart();

            parseNodes(nodes);
            parseLinks(links);

            createDocumentEnd();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        return sw.toString();
    }


    public void parseLinks(Iterator linkIterator) throws XMLStreamException {
        xtw.writeStartElement("links");

        while (linkIterator.hasNext()) {
            ConceptLink conceptLink = (ConceptLink) linkIterator.next();
            UmlLink link = (UmlLink) conceptLink.getModel();
            System.out.println("ADDING: " + link.getTo());
            xtw.writeStartElement("link");
            if(link.getId() != null) xtw.writeAttribute("id", link.getId());
            xtw.writeAttribute("to", link.getTo());
            xtw.writeAttribute("from", link.getFrom());
            xtw.writeAttribute("label", link.getName());
        }
        xtw.writeEndElement();
    }

    public void parseNodes(Iterator nodeIteratget) throws XMLStreamException {
        xtw.writeStartElement("nodes");
        while (nodeIteratget.hasNext()) {
            xtw.writeStartElement("node");
            ConceptNode conceptNode = (ConceptNode) nodeIteratget.next();
            xtw.writeAttribute("id", conceptNode.getModel().getId());
            xtw.writeAttribute("name", conceptNode.getModel().getName());
            xtw.writeAttribute("xpos", String.valueOf(conceptNode.getX()));
            xtw.writeAttribute("ypos", String.valueOf(conceptNode.getY()));
            xtw.writeEndElement();
        }
        xtw.writeEndElement();
    }


    /*public String createXMLString(Document xmlDocument) {
        try {
            return createXMLStringFromDocument(xmlDocument);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
    */
    /*
    private String createXMLStringFromDocument(Document xmlDocument) throws IOException {
        OutputFormat of = new OutputFormat();
        of.setIndent(1);
        of.setIndenting(true);
        //of.setDoctype(null, "projectConfiguration.dtd");
        StringWriter sw = new StringWriter();

        XMLSerializer serializer = new XMLSerializer(sw, of);
        serializer.asDOMSerializer();
        serializer.serialize(xmlDocument.getDocumentElement());
        String xml = sw.toString();

        xml = xml.substring(xml.indexOf("<conceptmap>"), xml.length());
        System.out.println(xml);
        return xml;
    }

    private File createXMLFile(Document xmldoc) {
        File outputFile = null;
        try {
            File tempdirectory = new File(System.getProperty("user.home"));
            System.out.println("tempDi:" + tempdirectory);
            outputFile = File.createTempFile("assessmentXMLoutput", ".xml", tempdirectory);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //for debugging
        try {
            FileOutputStream fos = new FileOutputStream(outputFile.getAbsolutePath());
            OutputFormat of = new OutputFormat("XML", "ISO-8859-1", true);
            of.setIndent(1);
            of.setIndenting(true);
            //of.setDoctype(null, "projectConfiguration.dtd");
            XMLSerializer serializer = new XMLSerializer(fos, of);
            serializer.asDOMSerializer();
            serializer.serialize(xmldoc.getDocumentElement());
            fos.close();
        } catch (IOException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        System.out.println("WROTE FILE: " + outputFile.getAbsolutePath());

        return outputFile;
    }
     */

    public Iterator getNodeIterator() {
        return ApplicationController.getDefaultInstance().getGraphicsDiagram().getNodes().iterator();
    }

    public Iterator getLinkIterator() {
        return ApplicationController.getDefaultInstance().getGraphicsDiagram().getLinks().iterator();
    }
}
