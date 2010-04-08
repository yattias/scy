package eu.scy.server.taglibs;

import eu.scy.core.model.ScyBase;

import javax.servlet.jsp.JspException;
import java.lang.reflect.Method;
/**
 * Created by IntelliJ IDEA.
 * User: Lars
 * Date: 08.apr.2010
 * Time: 08:47:42
 * To change this template use File | Settings | File Templates.
 */
public class AjaxNumberField  extends AjaxBaseComponent{
    private int initialValue = 0;

    public void setInitialValue(int value){
        this.initialValue = value;
    }

    public int getInitialValue(){
        return this.initialValue;
    }

     public int doEndTag() throws JspException {
        try {
            double id = Math.random() ;
            pageContext.getOut().write("<form id=\"ajaxNumberFieldForm" + id + "\" method=\"post\" action=\"/webapp/components/ajaxTextField.html\">");



            pageContext.getOut().write("<input id=\"q05\" type=\"text\" dojoType=\"dijit.form.NumberTextBox\" name=\"elevation\" value=\""+ getInitialValue() + "\" >");
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
