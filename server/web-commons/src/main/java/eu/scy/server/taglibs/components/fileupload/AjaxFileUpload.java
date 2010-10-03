package eu.scy.server.taglibs.components.fileupload;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.jun.2010
 * Time: 06:35:09
 * To change this template use File | Settings | File Templates.
 */
public class AjaxFileUpload extends BodyTagSupport {

    private String listenerClass;

    public String getListenerClass() {
        return listenerClass;
    }

    public void setListenerClass(String listenerClass) {
        this.listenerClass = listenerClass;
    }

    public int doEndTag() throws JspException {
        String url = "/webapp/components/fileupload/fileupload.html?listener=" + getListenerClass();
        try {
            pageContext.getOut().write("<iframe src=\"" + url + "\"></iframe>");
            pageContext.getOut().write("<a href=\"" + url + "\">url</a>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

}
