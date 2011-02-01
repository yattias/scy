/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor.datamodel;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.swing.JFileChooser;
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
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author pg
 * this class is able to read from roolo/text files :)
 *
 * SINGLETON? :3
 *
 */
public class DataHandler {
    private static DataHandler instance;
    private static FormDataModel fdm;
    private DataHandler() {
        
    }

    public synchronized static DataHandler getInstance() {
        if(instance == null) {
            instance = new DataHandler();
        }
        return instance;
    }

    public Boolean saveToFile(FormDataModel fdm) {
        Document doc;
        String filename = null;
        try {
            doc = createXMLDocumentFromFDM(fdm);
            //get filename
            final JFileChooser jfc = new JFileChooser();
            jfc.setDialogType(JFileChooser.SAVE_DIALOG);
            int result = jfc.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                filename = jfc.getSelectedFile().toString();
            }
            //check if .xml
            if(! filename.endsWith(".xml")) {
                filename += ".xml";
            }
            //create doc
            docToFile(doc, filename);
        }
        catch(Exception e) {
            // System.out.println("AHH IM AN ERROR! PLEASE TAKE CARE OF ME OR ILL CRY :-(");
            e.printStackTrace();
        }
        return true;
    }

    public String saveToString(FormDataModel fdm) {
        try {
            return docToString(createXMLDocumentFromFDM(fdm));
        }
        catch(Exception e) {
            // System.out.println("AHH IM AN ERROR! PLEASE TAKE CARE OF ME OR ILL CRY :-(");
            e.printStackTrace();
        }
        return null;
    }


    private String docToString(Document doc) {
        try {
            Source source = new DOMSource(doc);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(source, result);
            return stringWriter.toString();
        }
        catch(Exception e) {
            // System.out.println("AHH IM AN ERROR! PLEASE TAKE CARE OF ME OR ILL CRY :-(");
            e.printStackTrace();
        }
        return null;
    }

    private boolean docToFile(Document doc, String filename) {
        try {
            Source source = new DOMSource(doc);
            // Prepare the output file
            File file = new File(filename);
            Result result = new StreamResult(file);
            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance()
                            .newTransformer();
            xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            xformer.transform(source, result);
        }
        catch(Exception e) {
             // System.out.println("AHH IM AN ERROR! PLEASE TAKE CARE OF ME OR ILL CRY :-(");
            e.printStackTrace();
        }

        return true;
    }

    public FormDataModel loadFromFile() {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogType(JFileChooser.OPEN_DIALOG);
        int result = jfc.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) {
            String filename = jfc.getSelectedFile().toString();
            FormDataModel fdm = null;
            try {
                FileInputStream fis = new FileInputStream(filename);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(fis);
                doc.getDocumentElement().normalize();
                fdm = createFDMfromXML(doc);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            finally {
                this.fdm = fdm;
                return fdm;
            }
        } else {
            return null;
        }
    }

    public FormDataModel loadFromString(String xml) {
        FormDataModel fdm = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xml)));
            doc.getDocumentElement().normalize();
            fdm = createFDMfromXML(doc);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            this.fdm = fdm;
            return fdm;
        }
    }

    private FormDataModel createFDMfromXML(Document doc) {
        // System.out.println("createFDMfromXML");
        FormDataModel fdm = new FormDataModel();
        NodeList nl;
        nl = doc.getElementsByTagName("form");
        for (int i = 0; i < nl.getLength(); i++) {
            //why a loop? a valid xml file should have only one <form> tag..
            fdm.setTitle(((Element) nl.item(i)).getAttribute("title"));
            // System.out.println(fdm.getTitle());
            fdm.setDescription(((Element) nl.item(i)).getAttribute("description"));
            // System.out.println(fdm.getDescription());
            fdm.setVersion(Integer.parseInt(((Element) nl.item(i)).getAttribute("version")));
        }
        nl = doc.getElementsByTagName("field");
        for (int i = 0; i < nl.getLength(); i++) {
            String title = ((Element) nl.item(i)).getAttribute("title");
            // System.out.println("Element: "+title);
            //int to type
            // System.out.println(((Element) nl.item(i)).getAttribute("type").toString());
            FormElementDataType type = FormElementDataType.valueOf(((Element) nl.item(i)).getAttribute("type").toString());
            // System.out.println("Type: "+type);

            int card;
            if(((Element) nl.item(i)).getAttribute("cardinality").equals("")) {
                card = 0;
            }
            else {
                card = Integer.parseInt(((Element) nl.item(i)).getAttribute("cardinality"));
            }
            FormDataElement fde = new FormDataElement(title, type, card);

            NodeList nlFieldChilds = nl.item(i).getChildNodes();
            ArrayList<FormDataEvent> events = new ArrayList<FormDataEvent>();

            for (int j = 0; j < nlFieldChilds.getLength(); j++) {
                if (nlFieldChilds.item(j).getNodeType() == Element.ELEMENT_NODE) {
                    Element fieldChild = (Element) nlFieldChilds.item(j);
                    if (fieldChild.getNodeName().equalsIgnoreCase("fielddata")) {
                        String fieldData = ((Element) nlFieldChilds.item(j))
                                .getAttribute("data");
                        fde.addData(Base64.decodeBase64(fieldData
                                .getBytes()));
                    }
                    else {

                        if (fieldChild.getNodeName().equalsIgnoreCase("event")) {
                            FormDataEvent fdev = new FormDataEvent(
                                    FormEventDataType.valueOf(((Element) nlFieldChilds.item(j)) .getAttribute("datatype")),
                                    FormEventType.valueOf(((Element) nlFieldChilds.item(j)).getAttribute("type")));
                            NodeList eventChilds = fieldChild.getChildNodes();
                            for (int k = 0; k < eventChilds.getLength(); k++) {
                                if (eventChilds.item(k).getNodeType() == Element.ELEMENT_NODE) {
                                    if (eventChilds.item(k).getNodeName()
                                            .equalsIgnoreCase("eventdata")) {
                                        String eventData = ((Element) eventChilds
                                                .item(k)).getAttribute("data");
                                        fdev.addData(Base64
                                                .decodeBase64(eventData
                                                        .getBytes()));
                                        // TODO WARUM GEHT decodeBase64 nur
                                        // mit
                                        // Bytes?
                                        //wzf soll dieser kommentar?
                                    }
                                }
                            }
                            events.add(fdev);
                        }
                    }
                }
                fde.setEvents(events);
            }
            fdm.addElement(fde);
        }
        // System.out.println("returning fdm");
        this.fdm = fdm;
        return fdm;
    }


    private Document createXMLDocumentFromFDM(FormDataModel fdm) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser;
        parser = factory.newDocumentBuilder();
        Document doc = parser.newDocument();

        Element root = doc.createElement("form");
        root.setAttribute("title", fdm.getTitle());
        root.setAttribute("description", fdm.getDescription());
        root.setAttribute("version", String.valueOf(fdm.getVersion()));

        for(FormDataElement fem : fdm.getElements()) {
            Element elField = doc.createElement("field");
            elField.setAttribute("title", fem.getTitle());
            elField.setAttribute("type", fem.getType().name());
            elField.setAttribute("cardinality", String.valueOf(fem.getCardinality()));
            //here: save data
            if((fem.getEvents() != null) && fem.getEvents().size() > 0) {
                for(FormDataEvent fev : fem.getEvents()) {
                    Element elEvent = doc.createElement("event");
                    elEvent.setAttribute("type", fev.getType().name());
                    elEvent.setAttribute("datatype", fev.getDataType().name());
                    //here: save data
                    elField.appendChild(elEvent);
                }
            }
            root.appendChild(elField);
        }
        doc.appendChild(root);
        return doc;
    }

    public FormDataModel getLastFDM() {
        return this.fdm;
    }
}
