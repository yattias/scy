package eu.scy.client.tools.fxwebresourcer.highlighter;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class XMLData {
    private Document doc;
    private String title;
    private List<String> bullets;
    private String comments;
    private String sources;
    private SAXBuilder sb;

    public XMLData(String input) throws JDOMException, IOException {
            sb = new SAXBuilder();
            //read first data block:
            doc = sb.build(new StringReader("<data>"+input+"</data>"));
            Element root = doc.getRootElement();
            //read what's inside of <![CDATA[ - should that not be <![PCDATA[ ?
            doc = sb.build(new StringReader(root.getChild("annotations").getText()));
            root = doc.getRootElement();
            title = root.getChild("title")
                            .getText();
            //System.out.println(title);
            bullets = root.getChildren("summary");
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
