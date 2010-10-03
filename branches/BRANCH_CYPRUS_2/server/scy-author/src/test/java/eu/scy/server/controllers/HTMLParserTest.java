package eu.scy.server.controllers;

import junit.framework.TestCase;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.mai.2010
 * Time: 11:16:23
 * To change this template use File | Settings | File Templates.
 */
public class HTMLParserTest extends TestCase {

    public void testExtractIcon() {

        //String u1 = "enovate.no";
        String u2 = "ba.no";

        //String result1 = parseUrl(u1);
        //System.out.println("RESULT: " + result1);
        //assertTrue(result1.equals("favicon.ico"));

        String result2 = parseUrl(u2);
        System.out.println("result: " + result2);

    }

    private String parseUrl(String inputUrl) {
        try {
            System.out.println("PARSING: " + inputUrl);
            if(!inputUrl.startsWith("http://")) inputUrl = "http://" + inputUrl;
            URL url = new URL(inputUrl);
            URLConnection connection = url.openConnection();
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            XMLReader parser = XMLReaderFactory.createXMLReader("org.ccil.cowan.tagsoup.Parser");
            HTMLParser htmlDoc = new HTMLParser();
            parser.setContentHandler(htmlDoc);
            parser.parse(new InputSource(is));
            return htmlDoc.getFaviconurl();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        throw new RuntimeException("Parse error");
    }

}
