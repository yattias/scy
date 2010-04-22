package eu.scy.server.taglibs;

import eu.scy.core.model.ScyBase;

import javax.servlet.jsp.JspException;
import java.lang.reflect.Method;
/**
 * Created by IntelliJ IDEA.
 * User: Lars
 * Date: 08.apr.2010
 * Time: 08:47:42
 */
public class AjaxNumberField  extends AjaxBaseComponent{

     public int doEndTag() throws JspException {
        try {
            double id = Math.random() ;
            pageContext.getOut().write("<form id=\"ajaxNumberFieldForm" + id + "\" method=\"post\" action=\"/webapp/components/ajaxNumberField.html\">");
            pageContext.getOut().write("<input type=\"hidden\" name=\"number\" id=\"ajaxNumberHiddenField" + id +"\" value=\"" + getModel().getClass().getName() + "\" />");
            pageContext.getOut().write("<span id=\"ajaxNumberField" + id +"\" onChange=\"document.getElementById('ajaxNumberHiddenField" + id +"').value = this.value;postForm('ajaxNumberFieldForm" + id + "');\"  dojoType=\"dijit.InlineEditBox\" editor=\"dijit.form.NumberTextBox\" value=\""+ executeGetter(getModel(), getProperty()) + "\" ></span>");

            pageContext.getOut().write("<input type=\"hidden\" name=\"clazz\" value=\"" + getModel().getClass().getName() + "\" />");
            pageContext.getOut().write("<input type=\"hidden\" name=\"id\" value=\"" + ((ScyBase)getModel()).getId() + "\" />");
            pageContext.getOut().write("<input type=\"hidden\" name=\"property\" value=\"" + getProperty() + "\" />");
            pageContext.getOut().write("</form>");
        } catch(Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    private Integer executeGetter(Object object, String property) {
        try {
            String firstLetter = property.substring(0,1);
            firstLetter = firstLetter.toUpperCase();

            property = firstLetter + property.substring(1, property.length());

            Method method = object.getClass().getMethod("get" + property);

            return (Integer) method.invoke(object, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("NOOO");
    }
}
