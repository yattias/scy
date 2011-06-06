package eu.scy.server.taglibs;

import eu.scy.core.model.ScyBase;
import eu.scy.core.model.impl.SCYUserDetails;

import javax.servlet.jsp.JspException;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mar.2010
 * Time: 06:57:28
 * To change this template use File | Settings | File Templates.
 */
public class AjaxTextField extends AjaxBaseComponent{
    private Boolean isMultiLine = false;

    public void setIsMultiLine(Boolean isMultiLine){
        this.isMultiLine = isMultiLine;
    }

    public Boolean getIsMultiline(){
        return this.isMultiLine;
    }

     public int doEndTag() throws JspException {
        try {
            double id = Math.random() ;
            pageContext.getOut().write("<form id=\"ajaxTextFieldForm" + id + "\" method=\"post\" action=\"/webapp/components/ajaxTextField.html\">");
            if(getIsMultiline()){
                pageContext.getOut().write("<span dojoType=\"dijit.InlineEditBox\" onchange=\"document.getElementById('ajaxTextField" + id +"').value = this.value;postForm('ajaxTextFieldForm" + id + "');\"  autoSave='false' editor=\"dijit.form.Textarea\" >" + executeGetter(getModel(), getProperty()) + "</span>");
            } else {
               pageContext.getOut().write("<span dojoType=\"dijit.InlineEditBox\" onchange=\"document.getElementById('ajaxTextField" + id +"').value = this.value;postForm('ajaxTextFieldForm" + id + "');\" autoSave='true'  >" + executeGetter(getModel(), getProperty()) + "</span>");
            }


            pageContext.getOut().write("<input type=\"hidden\" id=\"ajaxTextField" + id + "\" name=\"value\" value=\"\" + executeGetter(getModel(), getProperty()) + \"\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"clazz\" value=\"" + getModel().getClass().getName() + "\">");
            if(getModel() instanceof ScyBase) {
                pageContext.getOut().write("<input type=\"hidden\" name=\"id\" value=\"" + ((ScyBase)getModel()).getId() + "\">");
            } else if(getModel() instanceof SCYUserDetails) {
                pageContext.getOut().write("<input type=\"hidden\" name=\"id\" value=\"" + ((SCYUserDetails)getModel()).getId() + "\">");
            }

            pageContext.getOut().write("<input type=\"hidden\" name=\"property\" value=\"" + getProperty() + "\">");
            pageContext.getOut().write("</form>");
        } catch(Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    private String executeGetter(Object object, String property) {
        try {
            String firstLetter = property.substring(0,1);
            firstLetter = firstLetter.toUpperCase();

            property = firstLetter + property.substring(1, property.length());

            Method method = object.getClass().getMethod("get" + property);

            String returnValue = (String) method.invoke(object, null);

            if (returnValue == null) {
                returnValue = "Edit";
            }

            return returnValue;
        } catch (Exception e) {
            e.printStackTrace();  
        }
        throw new RuntimeException("NOOO - " + getClassName(object) + " property: " + property);
    }

    private String getClassName(Object object) {
        if(object != null) return object.getClass().getName();
        return "Object is null!";
    }
}
