package eu.scy.server.taglibs;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.nov.2010
 * Time: 12:44:56
 * To change this template use File | Settings | File Templates.
 */
public class BaseAjaxELOComponent extends TagSupport {

    private String eloURI;
    private String property;

    public String getEloURI() {
        return eloURI;
    }

    public void setEloURI(String eloURI) {
        this.eloURI = eloURI;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
