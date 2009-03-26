package eu.scy.colemo.client;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import java.util.Iterator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 25.mar.2009
 * Time: 13:03:35
 * To change this template use File | Settings | File Templates.
 */
public class ConceptMapExporter {

    private static ConceptMapExporter defaultInstance;

    private ConceptMapExporter() {

    }

    public static ConceptMapExporter getDefaultInstance() {
        if (defaultInstance == null) defaultInstance = new ConceptMapExporter();
        return defaultInstance;
    }

    public void createXML() {
        Document xmldoc = createXMLDocument();
        Element root = xmldoc.createElement("conceptmap");
        xmldoc.appendChild(root);
        Element nodes = createElement(xmldoc, "nodes");
        root.appendChild(nodes);
        Element links = createElement(xmldoc, "links");
        root.appendChild(links);
        Iterator nodeIteratget = ApplicationController.getDefaultInstance().getGraphicsDiagram().getNodes().iterator();
        while (nodeIteratget.hasNext()) {
            ConceptNode conceptNode = (ConceptNode) nodeIteratget.next();
            System.out.println("ADDING: " + conceptNode.getModel().getName());
            Element conceptNodeElement = createElement(xmldoc, "node");
            conceptNodeElement.setAttribute("id", conceptNode.getModel().getId());
            conceptNodeElement.setAttribute("name", conceptNode.getModel().getName());
            conceptNodeElement.setAttribute("xpos", String.valueOf(conceptNode.getX()));
            conceptNodeElement.setAttribute("ypos", String.valueOf(conceptNode.getY()));
            System.out.println("Created element");
            nodes.appendChild(conceptNodeElement);
            System.out.println("ADDED NODE");
        }
        
        Iterator linkIterator = ApplicationController.getDefaultInstance().getGraphicsDiagram().getLinks().iterator();
        while (linkIterator.hasNext()) {
            ConceptLink conceptLink = (ConceptLink) linkIterator.next();
            System.out.println("ADDING: " + conceptLink.getModel().getTo());
            Element linkElement = createElement(xmldoc, "link");
            linkElement.setAttribute("id", conceptLink.getModel().getId());
            linkElement.setAttribute("to", conceptLink.getModel().getTo());
            linkElement.setAttribute("from", conceptLink.getModel().getFrom());
            linkElement.setAttribute("label", conceptLink.getModel().getName());
            links.appendChild(linkElement);
        }

        createXMLFile(xmldoc);
    }

    private Document createXMLDocument() {
        Document xmldoc = new DocumentImpl();
        return xmldoc;
    }

    private Element createElement(Document xmlDoc, String id) {
        System.out.println("Creating element : " + id);
        try {
            return xmlDoc.createElement(String.valueOf(id));
        } catch(Exception e)  {
            e.printStackTrace();
        }
        return null;
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


}
