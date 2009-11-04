package eu.scy.client.tools.scydynamics.logging;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;

public class SystemOutActionLogger implements IActionLogger {
	
	private OutputFormat format;
	private XMLWriter writer;

	public SystemOutActionLogger() {
		this.format = OutputFormat.createPrettyPrint();
		try {
			writer = new XMLWriter (System.out, format);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void log(String username, String source, IAction action) {
		// logging to the console
		//System.out.println(new ActionXMLTransformer(action).getActionAsString());
		try {
			writer.write(new ActionXMLTransformer(action).getActionAsElement());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}