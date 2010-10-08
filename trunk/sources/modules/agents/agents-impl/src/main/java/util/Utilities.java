/*
 * Created on 23.09.2010
 * @author Jörg Kindermann
 *
 * utility functions for eloXML manipulation etc.
 */
package util;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;

public class Utilities {

	private Utilities() {
	}

	public static String findElementContent(List<String> xmlPath,
			Element element) {
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
						// if there are no further descendants, we return an
						// empty text
						Element child = (Element) iterator.next();
						text = findElementContent(xmlPath.subList(1, xmlPath
								.size()), child);
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

	public static String getEloText(IELO elo, List<String> path, Logger logger) {
		IContent content = elo.getContent();
		if (content == null) {
			logger.fatal("Content of elo is null");
			return "";
		}
		String text = "";

		String contentText = content.getXmlString();
		try {
			SAXBuilder builder = new SAXBuilder();
			StringReader stringReader = new StringReader(contentText);
			org.jdom.Document document = builder.build(stringReader);
			Element element = document.getRootElement();
			text = findElementContent(path, element);
		} catch (IOException e) {
			logger.fatal(e.toString());
		} catch (JDOMException e) {
			logger.fatal("Content of elo XML is malformed");
			e.printStackTrace();
		}
		logger.debug("Got text " + text);
		return text;
	}

	public static String getEloText(IELO elo, Logger logger) {
		IContent content = elo.getContent();
		if (content == null) {
			logger.fatal("Content of elo is null");
			return "";
		}
		String text = "";
		SAXBuilder builder = new SAXBuilder();
		try {
			Element rootElement = builder.build(
					new StringReader(content.getXml())).getRootElement();
			text = rootElement.getTextTrim();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.debug("Got text " + text);
		return text;
	}

	public static de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document convertTextToDocument(
			String text) {
		de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document doc = new de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document(
				"id");
		doc.setFeature(Features.TEXT, text);
		return doc;
	}

}
