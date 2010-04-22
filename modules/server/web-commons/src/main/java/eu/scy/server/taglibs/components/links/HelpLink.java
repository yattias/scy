package eu.scy.server.taglibs.components.links;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 30.mar.2010
 * Time: 08:45:31
 */
public class HelpLink extends TagSupport {

    private String helpId;
    private String helpUrl = "/webapp/components/links/help.html";

    public int doEndTag() throws JspException {
        try {
            helpUrl = helpUrl + "?id=" + getHelpId();
            pageContext.getOut().write("<a href=\"javascript:loadDialog('" + helpUrl + "', '" + "" + "');\">" + "(?)" + "</a>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    public String getHelpId() {
        return helpId;
    }

    public void setHelpId(String helpId) {
        this.helpId = helpId;
    }

}
