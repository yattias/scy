package eu.scy.server.controllers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.mai.2010
 * Time: 06:36:48
 * To change this template use File | Settings | File Templates.
 */
public class HTMLParser extends DefaultHandler {
    private boolean title = false;
    private boolean url = false;
    private boolean favicon = false;
    private String documentTitle;
    private String faviconurl = "";


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equalsIgnoreCase("title"))
            title = true;
        if (qName.equalsIgnoreCase("url"))
            url = true;
        if(qName.equalsIgnoreCase("link")) {
            String favIconName = "";
            // System.out.println("FOUND LINK " + qName);
            for(int counter = 0; counter < attributes.getLength(); counter++) {
                if(attributes.getLocalName(counter).equalsIgnoreCase("rel")) {
                    // System.out.println("LOCALNAME: " + attributes.getLocalName(counter) + " :::: " + attributes.getValue(counter));
                    favicon = true;
                    favIconName =attributes.getLocalName(counter);
                    if(attributes.getValue(favIconName).contains("short") ||
                            attributes.getValue(favIconName).contains("SHORT") ||
                            attributes.getValue(favIconName).contains("icon")){
                        // System.out.println("HREF: " + attributes.getValue("href"));
                        setFaviconurl(attributes.getValue("href"));
                    }
                }
            }
        }
    }


    public void characters(char[] ch, int start, int length) {
        if (title) {
            this.documentTitle = new String(ch, start, length);
            // System.out.println("Title: " + documentTitle);
            title = false;
        } else if (url) {
            // System.out.println("Url: " + new String(ch, start, length));
            url = false;
        } else if(favicon) {
                
        }
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getFaviconurl() {
        return faviconurl;
    }

    public void setFaviconurl(String faviconurl) {
        this.faviconurl = faviconurl;
    }
}


