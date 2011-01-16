package eu.scy.client.tools.formauthor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import eu.scy.client.tools.formauthor.DataFormElementModel.DataFormElementTypes;

public class DataFormModel extends Observable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String description;
	private int version;
	private ArrayList<DataFormElementModel> dfElements = new ArrayList<DataFormElementModel>();

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
		getDfElements().add(new DataFormElementModel());
		setChanged();
		notifyObservers();
	}

	public ArrayList<DataFormElementModel> getDfElements() {
		return dfElements;
	}

	public String toXML() throws ParserConfigurationException {
		try {
			Document doc = createDoc();
			Source source = new DOMSource(doc);
			StringWriter stringWriter = new StringWriter();
			Result result = new StreamResult(stringWriter);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(source, result);
			return stringWriter.toString();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void toXML(String filename) {
		try {
			Document doc = createDoc();
			Source source = new DOMSource(doc);

			// Prepare the output file
			File file = new File(filename);
			Result result = new StreamResult(file);

			// Write the DOM document to the file
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			xformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @return
	 * @throws ParserConfigurationException
	 */
	private Document createDoc() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser;
		parser = factory.newDocumentBuilder();
		Document doc = parser.newDocument();

		Element root = doc.createElement("form");
		root.setAttribute("title", getTitle());
		root.setAttribute("description", getDescription());
		root.setAttribute("version", String.valueOf(getVersion()));

		for (DataFormElementModel dfem : getDfElements()) {
			Element elField = doc.createElement("field");
			elField.setAttribute("title", dfem.getTitle());
			elField.setAttribute("type", dfem.getType().name());
			elField.setAttribute("cardinality", dfem.getCardinality());
			if (dfem.getDataList() != null) {
				for (byte[] data : dfem.getDataList()) {
					Element elFieldData = doc.createElement("fielddata");
					elFieldData.setAttribute("data", new String(Base64.encodeBase64(data)));
					elField.appendChild(elFieldData);
				}
			}
			if (dfem.getEvents() != null) {
				for (DataFormElementEventModel dfeem : dfem.getEvents()) {
					Element elEvent = doc.createElement("event");
					elEvent.setAttribute("type", dfeem.getEventType().name());
					elEvent.setAttribute("datatype", dfeem.getEventDataType().name());
					if (dfeem.getDataList() != null) {
						for (byte[] data : dfeem.getDataList()) {
							Element elEventData = doc.createElement("eventdata");
							elEventData.setAttribute("data", new String(Base64.encodeBase64(data)));
							elEvent.appendChild(elEventData);
						}
						elField.appendChild(elEvent);
					}
				}
				root.appendChild(elField);
			}
		}
		doc.appendChild(root);
		return doc;
	}

	public void toFile(String filename) throws IOException {
		if (isValid() == true) {
			toXML(filename + ".xml");
			// ObjectOutputStream objOut = new ObjectOutputStream(
			// new BufferedOutputStream(new FileOutputStream(filename)));
			// objOut.writeObject(this);
			// objOut.close();
		} else {
			JOptionPane.showMessageDialog(null, Localizer.getString("ERROR_TITLE_FORM_NOT_VALID"), Localizer.getString("ERROR_MESSAGE_FORM_NOT_VALID"), JOptionPane.ERROR_MESSAGE);

		}
	}

	public void removeDataField(DataFormElementModel element) {
		dfElements.remove(element);
		setChanged();
		notifyObservers();
	}

	public void fromString(String xml) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(xml)));
			document.getDocumentElement().normalize();
			createDFMfromXML(document);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void fromFile(String filename) throws IOException, ClassNotFoundException {

		try {
			this.getDfElements().clear();

			FileInputStream fis = new FileInputStream(filename);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(fis);
			doc.getDocumentElement().normalize();

			createDFMfromXML(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param doc
	 */
	private void createDFMfromXML(Document doc) {
		NodeList nl;
		nl = doc.getElementsByTagName("form");
		for (int i = 0; i < nl.getLength(); i++) {
			this.setTitle(((Element) nl.item(i)).getAttribute("title"));
			this.setDescription(((Element) nl.item(i)).getAttribute("description"));
			this.setVersion(Integer.parseInt(((Element) nl.item(i)).getAttribute("version")));
		}
		nl = doc.getElementsByTagName("field");
		for (int i = 0; i < nl.getLength(); i++) {
			DataFormElementModel dfe = new DataFormElementModel();
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

					}

					// Element elFieldChild = (Element)
					// nlFieldChilds.item(j);
					// NodeList nlFieldData = elFieldChild
					// .getElementsByTagName("fielddata");
					// for (int k = 0; k < nlFieldData.getLength(); k++) {
					// if (nlFieldData.item(k).getNodeType() ==
					// Element.ELEMENT_NODE) {
					// String fieldData = ((Element) nlFieldData
					// .item(k)).getAttribute("data");
					//
					// dfe.addStoredData(Base64.decodeBase64(fieldData
					// .getBytes()));
					// }
					// }
					else {

						if (fieldChild.getNodeName().equalsIgnoreCase("event")) {
							DataFormElementEventModel dfeem = new DataFormElementEventModel(dfe);
							dfeem.setEventDataType(DataFormElementEventModel.DataFormElementEventDataTypes.valueOf(((Element) nlFieldChilds.item(j)).getAttribute("datatype")));
							dfeem.setEventType(DataFormElementEventModel.DataFormElementEventTypes.valueOf(((Element) nlFieldChilds.item(j)).getAttribute("type")));
							NodeList eventChilds = fieldChild.getChildNodes();
							for (int k = 0; k < eventChilds.getLength(); k++) {
								if (eventChilds.item(k).getNodeType() == Element.ELEMENT_NODE) {
									if (eventChilds.item(k).getNodeName().equalsIgnoreCase("eventdata")) {
										String eventData = ((Element) eventChilds.item(k)).getAttribute("data");
										dfeem.addStoredData(Base64.decodeBase64(eventData.getBytes()));
										// TODO WARUM GEHT decodeBase64 nur
										// mit
										// Bytes?
									}
								}

							}
							events.add(dfeem);

						}
					}
				}
				dfe.setEvents(events);

			}

			// dfe.addObserver(this);
			this.getDfElements().add(dfe);

		}
		setChanged();
		notifyObservers();
	}

	public boolean isValid() {
		boolean valid;
		valid = true;
		for (DataFormElementModel element : dfElements) {
			if (element.isValid() != true) {
				valid = false;
			}
		}
		return valid;
	}

	public void moveElementUp(DataFormElementModel dfem) {
		// TODO Auto-generated method stub
		int oldIndex = dfElements.indexOf(dfem);
		if (oldIndex > 0) {
			dfElements.remove(dfem);
			dfElements.add(oldIndex - 1, dfem);
			setChanged();
			notifyObservers();
		}
	}

	public void moveElementDown(DataFormElementModel dfem) {
		// TODO Auto-generated method stub
		int oldIndex = dfElements.indexOf(dfem);
		if (oldIndex < dfElements.size() - 1) {
			dfElements.remove(dfem);
			dfElements.add(oldIndex + 1, dfem);
			setChanged();
			notifyObservers();
		}
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getVersion() {
		return version;
	}

	public void EditEvents() {

	}

	public void clear() {
		dfElements = new ArrayList<DataFormElementModel>();
		title = "";
		description = "";
		setChanged();
		notifyObservers();
	}
}
