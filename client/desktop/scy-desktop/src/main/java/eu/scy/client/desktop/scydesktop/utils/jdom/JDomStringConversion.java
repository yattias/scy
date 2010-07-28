/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.utils.jdom;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

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
			logger.log(Level.WARNING, "problems converting jdom status to string", e);
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
			logger.log(Level.WARNING, "problems converting string status to jdom", e);
			return null;
		}
	}

}
