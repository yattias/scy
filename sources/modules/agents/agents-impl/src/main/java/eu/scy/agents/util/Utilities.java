/*
 * Created on 23.09.2010
 * @author Jörg Kindermann
 *
 * utility functions for eloXML manipulation etc.
 */
package eu.scy.agents.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;

public class Utilities {

  private Utilities() {
  }

//  private static String getChildValue(Node node, String childName) {
//    Node child = getChild(node, childName);
//    if (child != null) {
//      return child.getTextContent();
//    } else {
//      return null;
//    }
//  }

  private static Node getChild(Node node, String childName) {
    NodeList childNodes = node.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++) {
      Node child = childNodes.item(i);
      if (child.getNodeName().equals(childName)) {
        return child;
      }
    }
    return null;
  }

  public static String findElementContent(List<String> xmlPath, Element element) {
    String text = "";
    String name = element.getName();
    if (xmlPath.size() == 0) {// end of path is reached -this must be the
      // text element we are looking for
      text = element.getText();
    } else { // move on down the XMLPATH
      String pathElement = xmlPath.get(0);
      if (name.equals(pathElement)) {
        List descendants = element.getChildren();
        Iterator iterator = descendants.iterator();
        if (iterator.hasNext()) {
          while (text.equals("") & iterator.hasNext()) {
            // if there are no further descendants, we return an empty text
            Element child = (Element) iterator.next();
            text = findElementContent(xmlPath.subList(1, xmlPath.size()), child);
          }
        } else {
          text = element.getText();
        }
      }
    }
    return text;
  }

  public static String[] tokenize(String text) {
    String regex = "\\s+";
    String[] sent = text.split(regex);
    return sent;
  }

  public static String getEloText(IELO elo, String pathFragment, Logger logger) {
    String text = "";
    IContent content = elo.getContent();
    if (content == null) {
      logger.fatal("Content of elo is null");
    } else {
      String contentText = content.getXmlString();
      DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
      domFactory.setNamespaceAware(true); // never forget this!
      DocumentBuilder builder;
      try {
        builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(contentText)));

        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        XPathExpression expr = xpath.compile(pathFragment);

        NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
          Node nodeItem = nodeList.item(i);
          String id = nodeItem.getTextContent();
          if (!id.equals("")) {
            text = text + " " + id;
          }
        }
      } catch (ParserConfigurationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (SAXException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (XPathException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }
    return text;
  }

//  public static String getEloText(IELO elo, List<String> path, Logger logger) {
//    IContent content = elo.getContent();
//    if (content == null) {
//      logger.fatal("Content of elo is null");
//      return "";
//    }
//    String text = "";
//
//    String contentText = content.getXmlString();
//    try {
//      SAXBuilder builder = new SAXBuilder();
//      StringReader stringReader = new StringReader(contentText);
//      org.jdom.Document document = builder.build(stringReader);
//      Element element = document.getRootElement();
//      text = findElementContent(path, element);
//    } catch (IOException e) {
//      logger.fatal(e.toString());
//    } catch (JDOMException e) {
//      logger.fatal("Content of elo XML is malformed");
//      e.printStackTrace();
//    }
//    logger.debug("Got text " + text);
//    return text;
//  }

  public static String getEloContentAsString(IELO elo, Logger logger) {
    IContent content = elo.getContent();
    if (content == null) {
      logger.fatal("Content of elo is null");
      return "";
    }
    String text = "";
    SAXBuilder builder = new SAXBuilder();
    try {
      Element rootElement = builder.build(new StringReader(content.getXml())).getRootElement();
      text = rootElement.getTextTrim();
    } catch (JDOMException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    logger.debug("Got text " + text);
    return text;
  }

  public static de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document convertTextToDocument(String text) {
    de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document doc = new de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document(
                                                                                                                            "id");
    doc.setFeature(Features.TEXT, text);
    return doc;
  }

}
