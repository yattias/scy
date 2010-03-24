package eu.scy.server.taglibs;

import eu.scy.core.model.ScyBase;

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

     public int doEndTag() throws JspException {
        try {
            double id = Math.random() ;
            pageContext.getOut().write("<form id=\"ajaxTextFieldForm" + id + "\" method=\"post\" action=\"/webapp/components/ajaxTextField.html\">");
            pageContext.getOut().write("<input type=\"textfield\" name=\"value\" value=\"" + executeGetter(getModel(), getProperty()) + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"clazz\" value=\"" + getModel().getClass().getName() + "\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"id\" value=\"" + ((ScyBase)getModel()).getId() + "\">");
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
            return returnValue;
        } catch (Exception e) {
            e.printStackTrace();  
        }
        throw new RuntimeException("NOOO");
    }


}
