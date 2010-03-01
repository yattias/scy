/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxvideo;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author pg
 */
@XmlRootElement(name="document")
@XmlType(
    propOrder = { "title", "subtitle", "URI"}
)
public class XMLVideoData {
    private String title;
    private String subtitle;
    private String URI;

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the subtitle
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * @param subtitle the subtitle to set
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    /**
     * @return the URI
     */
    public String getURI() {
        return URI;
    }

    /**
     * @param URI the URI to set
     */
    public void setURI(String URI) {
        this.URI = URI;
    }


}
