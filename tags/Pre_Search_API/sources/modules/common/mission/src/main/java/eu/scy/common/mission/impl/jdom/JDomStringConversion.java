/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.common.mission.impl.jdom;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author sikkenj
 */
public class JDomStringConversion {
//	@SuppressWarnings( { "unused" })
	private static final Logger logger = Logger.getLogger(JDomStringConversion.class.getName());

	private transient SAXBuilder builder = new SAXBuilder(false);
	private XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

	public String xmlToString(Element element)
	{
		StringWriter stringWriter = new StringWriter();
		try
		{
			xmlOutputter.output(element, stringWriter);
		}
		catch (IOException e)
		{
			logger.warn("problems converting jdom xml to string");
         throw new IllegalArgumentException("A problem occured during converting jdom xml to string.\n" + e.getMessage());
		}
		return stringWriter.toString();
	}

	public Element stringToXml(String string)
	{
		StringReader stringReader = new StringReader(string);
		try
		{
			Document doc = builder.build(stringReader);
			return doc.getRootElement();
		}
		catch (Exception e)
		{
			logger.warn("problems converting xml string to jdom");
         throw new IllegalArgumentException("A problem occured during converting xml string to jdom.\n" + e.getMessage());
//			return null;
		}
	}

}
