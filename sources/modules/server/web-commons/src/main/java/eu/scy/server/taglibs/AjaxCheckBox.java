package eu.scy.server.taglibs;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.mar.2010
 * Time: 12:59:43
 * To change this template use File | Settings | File Templates.
 */
public class AjaxCheckBox extends TagSupport {
    
    private String modelClass;
    private String modelId;
    private String property;

     public int doEndTag() throws JspException {
        try {
            pageContext.getOut().write("Class: " +getModelClass() + " id: " + getModelId() + " property : " +getProperty());
            pageContext.getOut().write("<a href=\"/webapp/components/ajaxCheckBox.html\">clique me</a>");
            pageContext.getOut().write("<form method=\"post\" action=\"/webapp/components/ajaxCheckBox.html\">");
            pageContext.getOut().write("<input type=\"checkbox\">");
            pageContext.getOut().write("</form>");
        } catch(Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    public String getModelClass() {
        return modelClass;
    }

    public void setModelClass(String modelClass) {
        this.modelClass = modelClass;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
