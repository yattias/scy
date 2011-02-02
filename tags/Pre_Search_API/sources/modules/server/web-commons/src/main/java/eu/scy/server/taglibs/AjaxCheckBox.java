package eu.scy.server.taglibs;

import eu.scy.core.model.ScyBase;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.mar.2010
 * Time: 12:59:43
 * To change this template use File | Settings | File Templates.
 */
public class AjaxCheckBox extends AjaxBaseComponent{

    private String checked;

    public int doEndTag() throws JspException {
        try {
            double id = Math.random() ;
            pageContext.getOut().write("<form id=\"checkboxForm" + id + "\" method=\"post\" action=\"/webapp/components/ajaxCheckBox.html\">");
            pageContext.getOut().write("<input id=\"ajaxCheckBox" + id + "\" name=\"ajaxCheckBoxValue\" value=\"true\" dojoType=\"dijit.form.CheckBox\" onChange=\"postForm('checkboxForm"+ id + "');\" "  +  getChecked() + " >");
            pageContext.getOut().write("<input type=\"hidden\" name=\"clazz\" value=\"" + getModel().getClass().getName() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"id\" value=\"" + ((ScyBase)getModel()).getId() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"property\" value=\"" + getProperty() + "\">");
            pageContext.getOut().write("</form>");
        } catch(Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    private Boolean executeGetter(Object object, String property) {
        
        if(property == null) return false;
        try {
            String firstLetter = property.substring(0,1);
            firstLetter = firstLetter.toUpperCase();

            property = firstLetter + property.substring(1, property.length());

            Method method = object.getClass().getMethod("get" + property);

            Boolean returnValue =  (Boolean) method.invoke(object, null);
            // System.out.println(method.getName() + " " + returnValue);
            return returnValue;
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        throw new RuntimeException("NOOO");
    }

    public String getChecked() {
        if(executeGetter(getModel(), getProperty())) return "checked";
        return "";
    }
}
