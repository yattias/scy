package eu.scy.server.taglibs;

import eu.scy.core.model.ScyBase;
import java.lang.reflect.Method;
import java.sql.Date;
import javax.servlet.jsp.JspException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.mar.2010
 * Time: 12:59:43
 */
public class AjaxDatePicker extends AjaxBaseComponent{

    public int doEndTag() throws JspException {
        try {
            double id = Math.random() ;
            pageContext.getOut().write("<form id=\"datePickerForm" + id + "\" method=\"post\" action=\"/webapp/components/ajaxDatePicker.html\">");
            pageContext.getOut().write("<input type=\"text\" name=\"date" + "\" id=\"date"+id+"\" value=\"" + executeGetter(getModel(), getProperty()) + "\" onChange=\"postForm('datePickerForm" + id + "');\" dojoType=\"dijit.form.DateTextBox\" required=\"true\" />");
            pageContext.getOut().write("<input type=\"hidden\" name=\"clazz\" value=\"" + getModel().getClass().getName() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"id\" value=\"" + ((ScyBase)getModel()).getId() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"property\" value=\"" + getProperty() + "\">");
            pageContext.getOut().write("</form>");
        } catch(Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    private Date executeGetter(Object object, String property) {
        if(property == null) return null;
        try {
            String firstLetter = property.substring(0,1);
            firstLetter = firstLetter.toUpperCase();

            property = firstLetter + property.substring(1, property.length());

            Method method = object.getClass().getMethod("get" + property);

            Date returnValue =  (Date) method.invoke(object, null);
            System.out.println(method.getName() + " " + returnValue);
            return returnValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("NOOO");
    }
}