package eu.scy.server.taglibs;

import eu.scy.core.model.ScyBase;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.servlet.jsp.JspException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.mar.2010
 * Time: 12:59:43
 */
public class AjaxDatePicker extends AjaxBaseComponent {
    private final static SimpleDateFormat dayMontYearformat = new SimpleDateFormat("dd.MM.yyyy");

    public int doEndTag() throws JspException {
        try {
            double id = Math.random();
            pageContext.getOut().write("<form id=\"datePickerForm" + id + "\" method=\"post\" action=\"/webapp/components/ajaxDatePicker.html\">");

            pageContext.getOut().write("<input type=\"hidden\" name=\"date\" id=\"dateValue" + id + "\" value=\"" + executeGetter(getModel(), getProperty()) + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"clazz\" value=\"" + getModel().getClass().getName() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"id\" value=\"" + ((ScyBase) getModel()).getId() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"property\" value=\"" + getProperty() + "\">");
            pageContext.getOut().write("<span  id=\"date" + id + "\" value=\"" + executeGetter(getModel(), getProperty()) + "\" onChange=\"document.getElementById('dateValue" + id + "').value=this.value;postForm('datePickerForm" + id + "');\" editor=\"dijit.form.DateTextBox\" dojoType=\"dijit.InlineEditBox\" required=\"true\" />");
            pageContext.getOut().write("</form>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    private String executeGetter(Object object, String property) {
        if (property == null) return null;
        try {
            String firstLetter = property.substring(0, 1);
            firstLetter = firstLetter.toUpperCase();

            property = firstLetter + property.substring(1, property.length());

            Method method = object.getClass().getMethod("get" + property);

            Date returnValue = (Date) method.invoke(object, null);
            // System.out.println(method.getName() + " " + returnValue);

            try {
                return dayMontYearformat.format(returnValue);
            } catch (NullPointerException npe) {
                return "Edit";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("NOOO");
    }
}
