/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxwebresourcer.highlighter;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author pg
 */

@XmlRootElement(name="document")
@XmlType(
    propOrder = { "title", "bullet", "comments", "sources" }
)
public class HighlighterXMLData {
//TODO: change xml to new version
//    @XmlElement(name = "title")

    private String title;
    @XmlElementWrapper(name = "summary")
    @XmlElement(name = "bullet")
    public List<String> bullet;
//    @XmlElement(name = "comments")
    private String comments;
    private String sources;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
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

    public String toString() {
        String text = new String();
        text += "<document>\r\n";
        text += "<title>"+this.title+"</title>\r\n";
        text += "<summary>\r\n";
        for(String item: bullet) {
            text += "<bullet>"+item+"</bullet>\r\n";
        }
        text +="</summary>\r\n";
        text +="<comments>"+this.comments.replaceAll("[\r\n]+", "LINEBREAKLINEBREAKISAFORBIDDENWORD")+"</comments>\r\n";
        text +="<sources>"+this.sources+"</sources>\r\n";
        text +="</document>";

        return text;
    }
}
