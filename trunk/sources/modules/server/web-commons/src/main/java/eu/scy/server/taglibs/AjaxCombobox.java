package eu.scy.server.taglibs;

import eu.scy.core.model.ScyBase;

import javax.servlet.jsp.JspException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mar.2010
 * Time: 10:11:59
 */
public class AjaxCombobox extends AjaxBaseComponent {


    private List comboBoxValues;

    public int doEndTag() throws JspException {
        try {
            double id = Math.random();
            pageContext.getOut().write("<form id=\"ajaxComboboxForm" + id + "\" method=\"post\" action=\"/webapp/components/ajaxCombobox.html\" >");
            pageContext.getOut().write("<select name=\"value\" value=\"" + executeGetter(getModel(), getProperty()) + "\"onchange=\"postForm('ajaxComboboxForm" + id + "');\">");
            for (int i = 0; i < comboBoxValues.size(); i++) {                                                
                Object o = comboBoxValues.get(i);
                pageContext.getOut().write("<option value=\""+o+"\">" + o + "</option>");
            }
            pageContext.getOut().write("</select>");
            pageContext.getOut().write("<input type=\"hidden\" name=\"clazz\" value=\"" + getModel().getClass().getName() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"id\" value=\"" + ((ScyBase) getModel()).getId() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"property\" value=\"" + getProperty() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"setterClass\" value=\"" + executeGetter(getModel(), getProperty()).getClass().getName() + "\">");
            pageContext.getOut().write("</form>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    public List getComboBoxValues() {
        return comboBoxValues;
    }

    public void setComboBoxValues(List comboBoxValues) {
        this.comboBoxValues = comboBoxValues;
    }

    private Object executeGetter(Object object, String property) {
        try {
            String firstLetter = property.substring(0, 1);
            firstLetter = firstLetter.toUpperCase();

            property = firstLetter + property.substring(1, property.length());

            Method method = object.getClass().getMethod("get" + property);

            Object returnValue = method.invoke(object, null);
            return returnValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("NOOO");
    }


}
