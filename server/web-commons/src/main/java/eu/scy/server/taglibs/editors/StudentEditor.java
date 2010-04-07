package eu.scy.server.taglibs.editors;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 26.mar.2010
 * Time: 15:13:40
 * To change this template use File | Settings | File Templates.
 */
public class StudentEditor extends TagSupport {

    private String url = "/webapp/editors/editStudent.html";
    private String username;

    public int doEndTag() throws JspException {
        try {
            double id = Math.random();
            pageContext.getOut().write("<a href=\"javascript:loadDialog('"+getUrl()+"', '" + getUsername() + "');\">" + getUsername() + "</a>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
