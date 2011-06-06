package eu.scy.server.taglibs;

import eu.scy.core.model.ScyBase;

import javax.servlet.jsp.JspException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.nov.2010
 * Time: 12:46:15
 * To change this template use File | Settings | File Templates.
 */
public class AjaxELOTextField extends BaseAjaxELOComponent {

    private Boolean isMultiLine = false;

    public Boolean getIsMultiline() {
        return isMultiLine;
    }

    public void setIsMultiline(Boolean multiLine) {
        isMultiLine = multiLine;
    }

    public int doEndTag() throws JspException {
        try {
            double id = Math.random() ;
            pageContext.getOut().write("<form id=\"ajaxTextFieldForm" + id + "\" method=\"post\" action=\"/webapp/components/ajaxELOTextField.html\">");
            if(getIsMultiline()){
                pageContext.getOut().write("<span dojoType=\"dijit.InlineEditBox\" onchange=\"document.getElementById('ajaxTextField" + id +"').value = this.value;postForm('ajaxTextFieldForm" + id + "');\"  autoSave='false' editor=\"dijit.form.Textarea\" >" + executeGetter(getEloURI(), getProperty()) + "</span>");
            } else {
               pageContext.getOut().write("<span dojoType=\"dijit.InlineEditBox\" onchange=\"document.getElementById('ajaxTextField" + id +"').value = this.value;postForm('ajaxTextFieldForm" + id + "');\" autoSave='true'  >" + executeGetter(getEloURI(), getProperty()) + "</span>");
            }


            pageContext.getOut().write("<input type=\"hidden\" id=\"ajaxTextField" + id + "\" name=\"value\" value=\"\" + executeGetter(getModel(), getProperty()) + \"\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"uri\" value=\"" + getEloURI() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"property\" value=\"" + getProperty() + "\">");
            pageContext.getOut().write("</form>");

        } catch (Exception e) {
            e.printStackTrace();
        }

         return EVAL_PAGE;
    }

}
