/**
 * 
 */
package eu.scy.commons.whack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.QName;
import org.xmpp.packet.PacketExtension;

import eu.scy.common.packetextension.SCYPacketTransformer;

/**
 * @author giemza
 *
 */
public class WhacketExtension extends PacketExtension {

	private static Map<String, SCYPacketTransformer> transformers = new HashMap<String, SCYPacketTransformer>();

	public static void registerExtension(SCYPacketTransformer transformer) {
		transformers.put(transformer.getNamespace(), transformer);
		registeredExtensions.put(QName.get(transformer.getElementname(), transformer.getNamespace()), WhacketExtension.class);
	}

	private Map<String, Integer> pathCounter;
	
	public WhacketExtension(Element element) {
		super(element);
	}

	public WhacketExtension(String name, String namespace) {
		super(name, namespace);
	}
	
	public WhacketExtension(SCYPacketTransformer transformer) {
		super(transformer.getElementname(), transformer.getNamespace());
		
		initElement(transformer);
	}
	
	public WhacketExtension(String name, String namespace, Object pojo) {
		super(name, namespace);
		
		SCYPacketTransformer transformer = transformers.get(namespace);
		transformer.setObject(pojo);
		
		initElement(transformer);
	}

	/**
	 * @param transformer
	 */
	private void initElement(SCYPacketTransformer transformer) {
		try {
			//TODO implement transformer.getElement() to avoid double parsing
			element = (Element) DocumentHelper.parseText(transformer.toXML()).getRootElement().detach();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Object getPojo() {
		pathCounter = new HashMap<String, Integer>();
		String namespace = element.getNamespace().getText();
		SCYPacketTransformer transformer = transformers.get(namespace).newInstance();
		transformer.resetParser();
		
		// detach nested element from root extension element x
		element = element.element(transformer.getName());
		
		parseElement(element, transformer, "/" + element.getName());
		
		return transformer.getObject();
	}
	
	private void parseElement(Element element, SCYPacketTransformer transformer, String path) {
		Integer count = pathCounter.get(path);
		if (count == null) {
			count = 0;
		} else {
			count++;
			path = path + "[" + count + "]";
		}
		pathCounter.put(path, count);
		for (Iterator<Attribute> iter = element.attributeIterator(); iter.hasNext();) {
			Attribute attribute = iter.next();
			String attribPath = path + "@" + attribute.getName();
			transformer.mapXMLNodeToObject(attribPath, attribute.getValue());
		}
		if (element.getTextTrim() != null && element.getTextTrim().length() > 0) {
			transformer.mapXMLNodeToObject(path, element.getTextTrim());
		}
		for (Iterator<Element> iter = element.elementIterator(); iter.hasNext();) {
			Element e = iter.next();
			parseElement(e, transformer, path + "/" + e.getName());
		}
	}
	
}
