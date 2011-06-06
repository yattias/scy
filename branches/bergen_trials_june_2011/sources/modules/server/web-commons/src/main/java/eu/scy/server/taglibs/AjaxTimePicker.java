package eu.scy.server.taglibs;

import eu.scy.core.model.ScyBase;

import javax.servlet.jsp.JspException;
import java.lang.reflect.Method;
import java.sql.Time;

/**
 * Created by IntelliJ IDEA.
 * User: Lars
 * Date: 07.apr.2010
 * Time: 09:33:52
 */
public class AjaxTimePicker extends AjaxBaseComponent{

    public int doEndTag() throws JspException {
        try {
            double id = Math.random() ;
            pageContext.getOut().write("<form id=\"timePickerForm" + id + "\" method=\"post\" action=\"/webapp/components/ajaxTimePicker.html\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"time\" id=\"timeValue"+id+"\" value=\"T"+ executeGetter(getModel(), getProperty()) +"\" />");
            pageContext.getOut().write("<input type=\"hidden\" name=\"clazz\" value=\"" + getModel().getClass().getName() + "\"/>");
            pageContext.getOut().write("<input type=\"hidden\" name=\"id\" value=\"" + ((ScyBase)getModel()).getId() + "\"/>");
            pageContext.getOut().write("<input type=\"hidden\" name=\"property\" value=\"" + getProperty() + "\"/>");
            pageContext.getOut().write("<span dojoType=\"dijit.InlineEditBox\" id=\"timeSelector"+id+"\" onChange=\"document.getElementById('timeValue"+id+"').value='T' + this.value;postForm('timePickerForm" + id + "');\" editor=\"dijit.form.TimeTextBox\"  value=\""+ executeGetter(getModel(), getProperty()) +"\" required=\"true\" />");
            pageContext.getOut().write("</form>");
        } catch(Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    private String executeGetter(Object object, String property) {
        if(property == null) return null;
        try {
            String firstLetter = property.substring(0,1);
            firstLetter = firstLetter.toUpperCase();

            property = firstLetter + property.substring(1, property.length());

            Method method = object.getClass().getMethod("get" + property);

            Time returnValue =  (Time) method.invoke(object, null);
            // System.out.println(method.getName() + " " + returnValue);

            if (returnValue == null) {
                return "Edit";
            }

            return returnValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("NOOO");
    }
}
