package eu.scy.actionlogging;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;

/**
 * This IActionLogger writes the action log as in XML to file,
 * specified by a filename.
 * @param  filename the filename of the logfile to be created
 * @author lars
 *
 */
public class FileLogger implements IActionLogger {

    private File logFile;
    private OutputStream out;
    private BufferedOutputStream bout;
    protected final static int BUF_SIZE = 512;
    private OutputStreamWriter write;
    private Transformer trans;

    public FileLogger(String filename) {
        logFile = new File(filename);
        try {
            trans = TransformerFactory.newInstance().newTransformer();
            Properties properties = new Properties();
            properties.setProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            properties.setProperty(OutputKeys.INDENT, "yes");
            properties.setProperty(OutputKeys.ENCODING, "utf-8");
            properties.setProperty("{http://xml.apache.org/xslt}indent-amount",
                    "4");
            trans.setOutputProperties(properties);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            e.printStackTrace();
        }
        openDocument();
    }

    protected void openDocument() {
        try {
            out = new FileOutputStream(logFile);
            bout = new BufferedOutputStream(out, BUF_SIZE);
            write = new OutputStreamWriter(bout, "UTF-8");
            write.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
            write.write("<actions>\r\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            write.write("\r\n</actions>");
            write.flush();
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush() {
        try {
            write.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void log(IAction action) {
        try {
            write.write(new ActionXMLTransformer(action).getActionAsString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Deprecated
    public void log(String username, String source, IAction action) {
        log(action);
    }

}
