package eu.scy.server.taglibs.components;

import eu.scy.core.model.impl.ScyBaseObject;

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
    private String model;

    public int doEndTag() throws JspException {
        try {
            double id = Math.random();

            pageContext.getOut().write("<form method=\"post\" action=\"/webapp/components/fileupload/fileupload.html\" enctype=\"multipart/form-data\" accept-charset=\"UTF-8\">");
            pageContext.getOut().write("<input type=\"file\" name=\"file\"/>");
            pageContext.getOut().write("<input type=\"hidden\" name=\"listener\" value=\"" + getListener() + "\"/>");
            if(getEncodedModel() != null) pageContext.getOut().write("<input type=\"hidden\" name=\"model\" value=\"" + getEncodedModel() + "\"/>");
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    private String getEncodedModel() {
        if(getModel() == null) return null;
        return getModel();
    }
}
