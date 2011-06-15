package eu.scy.server.controllers.xml;

import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;

import java.io.Writer;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.jun.2011
 * Time: 03:52:30
 * To change this template use File | Settings | File Templates.
 */
public class SCYPrintWriter extends PrettyPrintWriter {

    private static Logger log = Logger.getLogger("SCYPrintWriter.class");

     public SCYPrintWriter(Writer writer) {
        super(writer);
    }

    protected void writeText(QuickWriter writer, String text) {

        log.info("TEXT: " + text);
        super.writeText(writer, text);


    }


}
