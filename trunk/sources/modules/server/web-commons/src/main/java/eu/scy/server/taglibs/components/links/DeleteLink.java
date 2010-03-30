package eu.scy.server.taglibs.components.links;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 30.mar.2010
 * Time: 07:07:28
 * To change this template use File | Settings | File Templates.
 */
public class DeleteLink extends TagSupport {

    private String href;
    private String title;
    private String confirmText;

    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().write("<a href=\"javascript:if(confirm('" + getConfirmText() + "')){ location.href = '" + getHref() + "';} \">" + getTitle() + "</a>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getConfirmText() {
        return confirmText;
    }

    public void setConfirmText(String confirmText) {
        this.confirmText = confirmText;
    }
}
