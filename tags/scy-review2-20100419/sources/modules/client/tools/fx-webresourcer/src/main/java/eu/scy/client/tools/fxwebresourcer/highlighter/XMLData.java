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
    private String title = "empty";
    private List<String> bullets;
    private String comments = "";
    private String sources = "";
    private SAXBuilder sb;

    public XMLData(String input) throws JDOMException, IOException {

        //System.out.println(input);
            sb = new SAXBuilder();
            //read first data block:
            //doc = sb.build(new StringReader("<data>"+input+"</data>"));
            //Element root = doc.getRootElement();
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
            List<Element> items = root.getChild("summary").getChildren("bullet");
            bullets = new ArrayList<String>();
            for(Element item:items) {
                bullets.add(item.getText());

            }
            //System.out.println(bullets);
            comments = root.getChild("comments")
                            .getText();
            //System.out.println(comments);
            sources = root.getChild("sources")
                            .getText();
            //System.out.println(sources);

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getBullets() {
        return bullets;
    }

    public void setBullets(List<String> bullet) {
        this.bullets = bullet;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSources() {
        return sources;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }

}
