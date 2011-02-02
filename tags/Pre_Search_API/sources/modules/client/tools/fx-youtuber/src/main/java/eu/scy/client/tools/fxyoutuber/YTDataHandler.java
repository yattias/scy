/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxyoutuber;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author frogbox-desktop
 */
public class YTDataHandler {

    public static String createXMLDocument(ArrayList<YouTuberDataSet> dataSet) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser;
        parser = factory.newDocumentBuilder();
        Document doc = parser.newDocument();
        Element root = doc.createElement("youtuber");
        for(YouTuberDataSet set : dataSet) {
            Element elField = doc.createElement("item");
            elField.setAttribute("title", set.getTitle());
            elField.setAttribute("text", set.getText());
            elField.setAttribute("ytid", set.getYtid());
            root.appendChild(elField);
        }
        doc.appendChild(root);
        return docToString(doc);
    }

    private static String docToString(Document doc) {
    try {
        Source source = new DOMSource(doc);
        StringWriter stringWriter = new StringWriter();
        Result result = new StreamResult(stringWriter);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        //transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(source, result);
        // System.out.println(stringWriter.toString());
        return stringWriter.toString();
    }
    catch(Exception e) {
       //TODO: propper error handling
        e.printStackTrace();
    }
    return null;
    }

    public static ArrayList<YouTuberDataSet> createSetFromString(String input) {
        ArrayList<YouTuberDataSet> set = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(input)));
            doc.getDocumentElement().normalize();
            set = createSetFromXML(doc);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            return set;
        }
    }

    private static ArrayList<YouTuberDataSet> createSetFromXML(Document doc) {
        ArrayList<YouTuberDataSet> dataSet = new ArrayList<YouTuberDataSet>();
        NodeList nl;
        nl = doc.getElementsByTagName("item");
        for (int i = 0; i < nl.getLength(); i++) {
            YouTuberDataSet item = new YouTuberDataSet();
            item.setTitle(((Element) nl.item(i)).getAttribute("title"));
            item.setText(((Element) nl.item(i)).getAttribute("text"));
            item.setYtid(((Element) nl.item(i)).getAttribute("ytid"));
            dataSet.add(item);
        }
        return dataSet;
    }
 
}
