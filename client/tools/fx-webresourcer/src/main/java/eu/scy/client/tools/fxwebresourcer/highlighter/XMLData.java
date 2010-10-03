package eu.scy.client.tools.fxwebresourcer.highlighter;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class XMLData {
    private Document doc;
    private String title = "no title";
    private List<String> quotes;
    private List<String> comments;
    private List<String> sources;
    //private List<String>
    //private String comments = "";
    //private String sources = "";
    private SAXBuilder sb;

    public XMLData(String input) throws JDOMException, IOException {
            try {
                sb = new SAXBuilder();
                //read what's inside of <![CDATA[ - should that not be <![PCDATA[ ?
                //ugly dirty hack:
                int start = input.indexOf("<annotations>");
                if(start == -1) {
                    //whoops. not the kind of xml i expect
                    return;
                }
                int stop = input.indexOf("</annotations>");
                String piece = input.substring(start+13, stop);
                //System.out.println(piece);
                //doc = sb.build(new StringReader(root.getChild("annotations").getText()));

                doc = sb.build(new StringReader(piece));
                Element root = doc.getRootElement();
                title = root.getChild("title")
                                .getText();
                //System.out.println(title);
                List<Element> items = root.getChild("quotes").getChildren("quote");
                quotes = new ArrayList<String>();
                if(items.size() == 0) {
                    quotes.add("No Quotes found.");
                }
                else {
                    for(Element item:items) {
                        if(!item.getText().equals("")) {
                            quotes.add(item.getText());
                        }
                    }
                }
                items = root.getChild("comments").getChildren("comment");
                comments = new ArrayList<String>();
                if(items.size() == 0) {
                    comments.add("No comment found. Click here to change!");
                }
                else {
                    for(Element item:items) {
                        if(!item.getText().equals("")) {
                            comments.add(item.getText());
                        }
                    }
                }
                items = root.getChild("sources").getChildren("source");
                sources = new ArrayList<String>();
                if(items.size() == 0) {
                    sources.add("No sources found.");
                }
                else {
                    for(Element item:items) {
                        if(!item.getText().equals("")) {
                            sources.add(item.getText());
                        }
                    }
                }
                //System.out.println(bullets);
                /*
                comments = root.getChild("comments")
                                .getText();
                //System.out.println(comments);
                sources = root.getChild("sources")
                                .getText();
                //System.out.println(sources);
                 */
            }
            catch(Exception e) {
                //System.out.println("phew.. something went wrong:");
                //e.printStackTrace();
            }

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getQuotes() {
        if(quotes == null) {
                    quotes = new ArrayList<String>();
                    quotes.add("Error while parsing XML! The ELO seems to be broken.");
        }
        return quotes;
    }

    public void setQuotes(List<String> quotes) {
        this.quotes = quotes;
    }

    public List<String> getComments() {
        if(comments == null) {
                    comments = new ArrayList<String>();
                    comments.add("Error while parsing XML! The ELO seems to be broken.");
        }
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public List<String> getSources() {
        if(sources == null) {
                    sources = new ArrayList<String>();
                    sources.add("Error while parsing XML! The ELO seems to be broken.");
        }
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

}