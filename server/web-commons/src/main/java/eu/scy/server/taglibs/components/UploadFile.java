package eu.scy.server.taglibs.components;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 29.mar.2010
 * Time: 05:39:50
 * To change this template use File | Settings | File Templates.
 */
public class UploadFile extends TagSupport {

    private String listener;

    public int doEndTag() throws JspException {
        try {
            double id = Math.random();

            pageContext.getOut().write("<form method=\"post\" action=\"/webapp/components/fileupload/fileupload.html\" enctype=\"multipart/form-data\" accept-charset=\"UTF-8\">");
            pageContext.getOut().write("<input type=\"file\" name=\"file\"/>");
            pageContext.getOut().write("<input type=\"text\" name=\"listener\" value=\"" + getListener() + "\"/>");
            pageContext.getOut().write("<input type=\"submit\"/>");
            pageContext.getOut().write("</form>");
            


        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    public String getListener() {
        return listener;
    }

    public void setListener(String listener) {
        this.listener = listener;
    }
}
