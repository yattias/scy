package eu.scy.actionlogging;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;

/**
 * This IActionLogger logs to the console (useful for testing and debugging.
 * Makes use of the helper class ActionXMLTransformer.
 * @see ActionXMLTransformer
 * @author lars
 *
 */
public class SystemOutActionLogger implements IActionLogger {

    private OutputFormat format;
    private XMLWriter writer;

    public SystemOutActionLogger() {
        this.format = OutputFormat.createPrettyPrint();
        try {
            writer = new XMLWriter(System.out, format);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void log(IAction action) {
        // logging to the console
        try {
            writer.write(new ActionXMLTransformer(action).getActionAsElement());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
