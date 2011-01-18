package info.collide.android.scydatacollector;

import info.collide.android.scydatacollector.DataFormElementModel.DataFormElementTypes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;
import android.util.Xml.Encoding;

public class DataCollectorFormModel extends Observable implements Observer, Serializable {

    private static final long serialVersionUID = 1L;

    private String title;

    private String description;

    private int version;

    private ArrayList<DataFormElementModel> elementModels = new ArrayList<DataFormElementModel>();

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void addDataField() {
        getElementModels().add(new DataFormElementModel(this));
    }

    public ArrayList<DataFormElementModel> getElementModels() {
        return elementModels;
    }

    public void setElementModels(ArrayList<DataFormElementModel> elementModels) {
        this.elementModels = elementModels;
    }

    public String toXML() {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            // serializer.startDocument("UTF-8", true);
            serializer.startTag("", "form"); // <form>
            serializer.attribute("", "title", getTitle());
            serializer.attribute("", "description", getDescription());
            serializer.attribute("", "version", String.valueOf(getVersion()));

            for (DataFormElementModel dfem : getElementModels()) {
                serializer.startTag("", "field"); // <field>
                serializer.attribute("", "title", dfem.getTitle());
                serializer.attribute("", "type", dfem.getType().name());
                serializer.attribute("", "cardinality", dfem.getCardinality());
                if (dfem.getDataList() != null) {
                    for (byte[] data : dfem.getDataList()) {
                        serializer.startTag("", "fielddata"); // <fielddata>
                        serializer.attribute("", "data", new String(Base64.encodeBase64(data), "UTF-8"));
                        serializer.endTag("", "fielddata"); // </fielddata>
                    }
                }
                if (dfem.getEvents() != null) {
                    for (DataFormElementEventModel dfeem : dfem.getEvents()) {
                        serializer.startTag("", "event"); // <event>
                        serializer.attribute("", "type", dfeem.getEventType().name());
                        serializer.attribute("", "datatype", dfeem.getEventDataType().name());
                        if (dfeem.getDataList() != null) {
                            for (byte[] data : dfeem.getDataList()) {
                                serializer.startTag("", "eventdata"); // <eventdata>
                                serializer.attribute("", "data", new String(Base64.encodeBase64(data), "UTF-8"));
                                serializer.endTag("", "eventdata"); // </eventdata>
                            }
                        }
                        serializer.endTag("", "event");
                    }
                }
                serializer.endTag("", "field"); // </field>
            }
            serializer.endTag("", "form"); // </form>
            serializer.endDocument();
            return writer.toString();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static DataCollectorFormModel fromByteArray(byte[] inByte) {
        ObjectInputStream objIn;
        DataCollectorFormModel dcfm = null;
        try {
            objIn = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(inByte)));
            dcfm = (DataCollectorFormModel) objIn.readObject();
            objIn.close();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return dcfm;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baot = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(new BufferedOutputStream(baot));
        objOut.writeObject(this);
        objOut.close();
        notifyObservers();
        return baot.toByteArray();
    }

    public void removeDataField(DataFormElementModel element) {
        elementModels.remove(element);
    }

    public static DataCollectorFormModel fromString(String xml) throws IOException, ClassNotFoundException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            DataCollectorFormModel model = new DataCollectorFormModel();
            model.createDoc(document);
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param doc
     */
    private void createDoc(Document doc) {
        try {
            NodeList nl;
            nl = doc.getElementsByTagName("form");
            for (int i = 0; i < nl.getLength(); i++) {
                this.setTitle(((Element) nl.item(i)).getAttribute("title"));
                this.setDescription(((Element) nl.item(i)).getAttribute("description"));
                this.setVersion(Integer.parseInt(((Element) nl.item(i)).getAttribute("version")));
            }
            nl = doc.getElementsByTagName("field");
            for (int i = 0; i < nl.getLength(); i++) {
                DataFormElementModel dfe = new DataFormElementModel(this);
                dfe.setTitle(((Element) nl.item(i)).getAttribute("title"));
                dfe.setType(DataFormElementTypes.valueOf(((Element) nl.item(i)).getAttribute("type")));

                dfe.setCardinality(((Element) nl.item(i)).getAttribute("cardinality"));

                NodeList nlFieldChilds = nl.item(i).getChildNodes();
                ArrayList<DataFormElementEventModel> events = new ArrayList<DataFormElementEventModel>();

                for (int j = 0; j < nlFieldChilds.getLength(); j++) {
                    if (nlFieldChilds.item(j).getNodeType() == Element.ELEMENT_NODE) {
                        Element fieldChild = (Element) nlFieldChilds.item(j);
                        if (fieldChild.getNodeName().equalsIgnoreCase("fielddata")) {
                            String fieldData = ((Element) nlFieldChilds.item(j)).getAttribute("data");
                            dfe.addStoredData(Base64.decodeBase64(fieldData.getBytes()));

                        } else {
                            if (fieldChild.getNodeName().equalsIgnoreCase("event")) {
                                DataFormElementEventModel dfeem = new DataFormElementEventModel(dfe);
                                dfeem.setEventDataType(DataFormElementEventModel.DataFormElementEventDataTypes.valueOf(((Element) nlFieldChilds.item(j)).getAttribute("datatype")));
                                dfeem.setEventType(DataFormElementEventModel.DataFormElementEventTypes.valueOf(((Element) nlFieldChilds.item(j)).getAttribute("type")));
                                NodeList eventChilds = fieldChild.getChildNodes();
                                for (int k = 0; k < eventChilds.getLength(); k++) {
                                    if (eventChilds.item(k).getNodeType() == Element.ELEMENT_NODE) {
                                        if (eventChilds.item(k).getNodeName().equalsIgnoreCase("eventdata")) {
                                            String eventData = ((Element) eventChilds.item(k)).getAttribute("data");
                                            dfeem.addStoredData(Base64.decodeBase64(eventData.getBytes(Encoding.UTF_8.toString())));
                                        }
                                    }
                                }
                                events.add(dfeem);
                            }
                        }
                    }
                    dfe.setEvents(events);
                }
                this.getElementModels().add(dfe);
            }
            setChanged();
            notifyObservers();
        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }
    }

    public boolean isValid() {
        boolean valid;
        valid = true;
        for (DataFormElementModel element : elementModels) {
            if (element.isValid() != true) {
                valid = false;
            }
        }
        return valid;
    }

    public void moveElementUp(DataFormElementModel dfem) {
        int oldIndex = elementModels.indexOf(dfem);
        if (oldIndex > 0) {
            elementModels.remove(dfem);
            elementModels.add(oldIndex - 1, dfem);
        }
    }

    public void moveElementDown(DataFormElementModel dfem) {
        int oldIndex = elementModels.indexOf(dfem);
        if (oldIndex < elementModels.size() - 1) {
            elementModels.remove(dfem);
            elementModels.add(oldIndex + 1, dfem);
        }
    }

    public void update(Observable observable, Object data) {
        setChanged();
        notifyObservers();
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setDfElement(DataFormElementModel newDFEM, int elementPos) {
        elementModels.remove(elementPos);
        newDFEM.markChanged();
        elementModels.add(elementPos, newDFEM);
        setChanged();
        notifyObservers();
    }

    public void clear() {
        elementModels.clear();
        title = "";
        description = "";
    }
}
