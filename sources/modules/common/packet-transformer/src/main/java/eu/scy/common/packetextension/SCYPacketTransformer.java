package eu.scy.common.packetextension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public abstract class SCYPacketTransformer {
	
	private static final String elementname = "x";

	public SCYPacketTransformer(String name) {
		this(name, null);
		resetParser();
	}

	public SCYPacketTransformer(String name, Object object) {
		this.name = name;
		setObject(object);
	}

	public abstract void setObject(Object object);

	private String name;

	public abstract void mapXMLNodeToObject(String xPath, String value);

	public abstract String[] getXPaths();

	public abstract String getValueForXPath(String xPath);

	public abstract Object getObject();

	public abstract void resetParser();

	public abstract void startNode(String path);

	public abstract void endNode(String path);
	
	public abstract SCYPacketTransformer newInstance();

	public String toXML() throws ParserConfigurationException,
			TransformerFactoryConfigurationError, TransformerException,
			IOException {
		Document doc = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().newDocument();
		Element root = doc.createElementNS(getNamespace(), getElementname());
		doc.appendChild(root);
		
		for (String x : getXPaths()) {
			String v = getValueForXPath(x);
			if(v != null) {
				appendValue(doc, root, x, v);
			}
		}

		ByteArrayOutputStream output = new ByteArrayOutputStream();

		Transformer trans = TransformerFactory.newInstance().newTransformer();
		Properties properties = new Properties();
		properties.setProperty(OutputKeys.ENCODING, "UTF-8");
		properties.setProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		trans.setOutputProperties(properties);
		trans.transform(new DOMSource(doc), new StreamResult(output));
		String res = output.toString("UTF-8");
		output.flush();
		output.close();
		return res;
	}

	private void appendValue(Document doc, Element el, String x, String v) {
		x = x.substring(1);
		String[] paths = x.split("/");
		if (paths.length == 1) {
			if(paths[0].contains("@")) {
				String[] element = paths[0].split("@");
				Element child = fetchOrCreateElement(doc, el, element[0]);
				child.setAttribute(element[1], v);
			} else {
				Element child = fetchOrCreateElement(doc, el, paths[0]);
				child.setTextContent(v);
			}
		} else {
			String s = paths[0];
			Element item = fetchOrCreateElement(doc, el, s);
			appendValue(doc, item, x.substring(s.length()), v);
		}
	}

	private Element fetchOrCreateElement(Document doc, Element parentElement, String nodeName) {
		Integer index = parseIndex(nodeName);
		Element item = null;
		// TODO: hasChildNodes always returns false!? Check!
		if(/*parentElement.hasChildNodes() && */index != null) {
			NodeList childNodes = parentElement.getChildNodes();
			int c = 0;
			for (int i = 0; i < childNodes.getLength(); i++) {
				if (childNodes.item(i).getNodeName().equals(nodeName.substring(0, nodeName.indexOf('[')))) {
					if (c == index) {
						item = (Element) childNodes.item(i);
					} else {
						c++;
					}
				}
			}
			if (item == null) {
				item = doc.createElement(nodeName.substring(0, nodeName.indexOf('[')));
			}
		} else if (parentElement.hasChildNodes()) {
			NodeList nodeList = parentElement.getElementsByTagName(nodeName);
			if(nodeList.getLength() == 0) {
				item = doc.createElement(nodeName);
			} else {
				Node node = nodeList.item(0);
				if(node != null) {
					item = (Element) node;
				}
			}
		} else {
			item = doc.createElement(nodeName);
		}
		parentElement.appendChild(item);
		return item;
	}

	protected Integer parseIndex(String s) {
		if(s.contains("[") && s.contains("]")) {
			return Integer.parseInt(s.substring(s.indexOf('[') + 1, s.indexOf(']')));
		} else {
			return null;
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public String getNamespace() {
		return "jabber:" + elementname + ":" + getName();
	}
	
	public String getElementname() {
		return elementname;
	}

}
