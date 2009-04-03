package eu.scy.colemo.client;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.util.List;
import java.util.Collections;
import java.util.LinkedList;
import java.io.Reader;
import java.io.StringReader;
import java.io.IOException;

import eu.scy.colemo.server.uml.UmlClass;
import eu.scy.colemo.server.uml.UmlLink;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.apr.2009
 * Time: 06:33:02
 * To change this template use File | Settings | File Templates.
 */
public class ConceptMapLoader {

    private static ConceptMapLoader defaultInstance;

    private ConceptMapLoader() {
    }

    public static ConceptMapLoader getDefaultInstance() {
        if (defaultInstance == null) defaultInstance = new ConceptMapLoader();
        return defaultInstance;
    }

    public List parseConcepts(String xml) {
        List conceptList = new LinkedList();
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Reader reader = new StringReader(xml);
            Document doc = builder.parse(new org.xml.sax.InputSource(reader));
            NodeList conceptNodes = doc.getElementsByTagName("node");
            for (int counter = 0; counter < conceptNodes.getLength(); counter++) {
                Element conceptElement = (Element) conceptNodes.item(counter);
                String name = conceptElement.getAttribute("name");
                String id = conceptElement.getAttribute("id");
                String xPos = conceptElement.getAttribute("xpos");
                String yPos = conceptElement.getAttribute("ypos");

                UmlClass umlClass = new UmlClass(name, "c", "Henrik");
                umlClass.setX(new Integer(xPos));
                umlClass.setY(new Integer(yPos));
                umlClass.setId(id);
                conceptList.add(umlClass);
            }

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return conceptList;
    }

    public List parseLinks(String xml) {
       List linkedList = new LinkedList();
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Reader reader = new StringReader(xml);
            Document doc = builder.parse(new org.xml.sax.InputSource(reader));
            NodeList conceptNodes = doc.getElementsByTagName("link");
            for (int counter = 0; counter < conceptNodes.getLength(); counter++) {
                Element conceptElement = (Element) conceptNodes.item(counter);
                String name = conceptElement.getAttribute("name");
                String id = conceptElement.getAttribute("id");
                String to = conceptElement.getAttribute("to");
                String from = conceptElement.getAttribute("from");

                UmlLink link = new UmlLink(from, to, "Henrik");
                link.setId(id);
                link.setName("name");

                linkedList.add(link);
            }

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return linkedList;
    }
}
