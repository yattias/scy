package eu.scy.eloimporter.contentextractors;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import roolo.elo.api.IContent;
import roolo.elo.content.BasicContent;

public class XMLExtractor implements IContentExtractor {

	public IContent getContent(File file) {
		SAXBuilder builder = new SAXBuilder();
		StringWriter writer = new StringWriter();
		try {
			Document doc = builder.build(file);
			Format format = Format.getPrettyFormat();
			format.setOmitEncoding(true);
			format.setOmitDeclaration(true);

			XMLOutputter outputter = new XMLOutputter(format);
			outputter.output(doc, writer);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new BasicContent(writer.toString().trim());
	}
}
