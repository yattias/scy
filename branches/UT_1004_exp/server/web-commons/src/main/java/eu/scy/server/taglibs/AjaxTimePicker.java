package eu.scy.server.taglibs;

import eu.scy.core.model.ScyBase;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: Lars
 * Date: 07.apr.2010
 * Time: 09:33:52
 * To change this template use File | Settings | File Templates.
 */
public class AjaxTimePicker extends AjaxBaseComponent{
    private String checked;

    public int doEndTag() throws JspException {
        try {
            double id = Math.random() ;
            pageContext.getOut().write("<form id=\"timePickerForm" + id + "\" method=\"post\" action=\"/webapp/components/ajaxTimePicker.html\">");
            pageContext.getOut().write("<input type=\"text\" name=\"date"+id+"\" id=\"date"+id+"\"  onChange=\"postForm('timePickerForm" + id + "');\" dojoType=\"dijit.form.TimeTextBox\" required=\"true\" />");
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
            System.out.println(method.getName() + " " + returnValue);
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