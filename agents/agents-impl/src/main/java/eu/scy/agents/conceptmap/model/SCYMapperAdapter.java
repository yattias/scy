package eu.scy.agents.conceptmap.model;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import eu.scy.agents.conceptmap.Graph;


public class SCYMapperAdapter implements ConceptMapAdapter<String> {

    @Override
    public Graph transformToGraph(String graphRepresentation) {
        Graph g = new Graph();
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            XPath xPath = XPathFactory.newInstance().newXPath();
            Document doc = builder.parse(new InputSource(new StringReader(graphRepresentation)));
            HashMap<String, String> nodes = new HashMap<String, String>();
            
            NodeList nodeList = (NodeList) xPath.evaluate("//nodes/*", doc, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node nodeItem = nodeList.item(i);
                String id = getChildValue(nodeItem, "id");
                String label = getChildValue(nodeItem, "label");
                nodes.put(id, label);
                g.addNode(label, id);
            }
            
            NodeList linkList = (NodeList) xPath.evaluate("//links/*", doc, XPathConstants.NODESET);
            for (int i = 0; i < linkList.getLength(); i++) {
                Node linkItem = linkList.item(i);
                String label = getChildValue(linkItem, "label");
                String xpathToFrom = getChildAttribute(linkItem, "fromNode", "reference");
                String xpathToTo = getChildAttribute(linkItem, "toNode", "reference");
                xpathToFrom ="//" + xpathToFrom.replaceAll("\\.\\./", "");
                xpathToTo ="//" + xpathToTo.replaceAll("\\.\\./", "");
                
                NodeList fromNodeList = (NodeList) xPath.evaluate(xpathToFrom, doc, XPathConstants.NODESET);
                NodeList toNodeList = (NodeList) xPath.evaluate(xpathToTo, doc, XPathConstants.NODESET);
                String fromNodeId =  getChildValue(fromNodeList.item(0), "id");
                String toNodeId =  getChildValue(toNodeList.item(0), "id");
                g.addEdge(fromNodeId, toNodeId, label, "dummy-id"); // TODO: find edge ID
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return g;
    }

    private String getChildValue(Node node, String childName) {
        Node child = getChild(node, childName);
        if (child != null) {
            return child.getTextContent();
        } else {
            return null;
        }
    }

    private String getChildAttribute(Node node, String childName, String attributeName) {
        Node child = getChild(node, childName);
        if (child != null) {
            return child.getAttributes().getNamedItem(attributeName).getTextContent();
        } else {
            return null;
        }
    }

    private Node getChild(Node node, String childName) {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child.getNodeName().equals(childName)) {
                return child;
            }
        }
        return null;
    }
    
}
