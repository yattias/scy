package eu.scy.server.taglibs;

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
public class AjaxCheckBox extends TagSupport {
    
    private String modelClass;
    private String modelId;
    private String property;
    private Object model;
    private String checked;

    public int doEndTag() throws JspException {
        try {
            System.out.println("MODEL: " + getModel() + " property: " + getProperty());


            double id = Math.random() ;


            pageContext.getOut().write("Class: " +getModelClass() + " id: " + getModelId() + " property : " +getProperty());

            pageContext.getOut().write("<form id=\"checkboxForm" + id + " method=\"post\" action=\"/webapp/components/ajaxCheckBox.html\">");
            pageContext.getOut().write("<input id=\"" + id + "\" name=\"" + id +  "\" value=\"\" dojoType=\"dijit.form.CheckBox\" onChange=\"postForm('checkboxForm"+ id + "')\""  +  getChecked() + " >");
            pageContext.getOut().write("</form>");
        } catch(Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    private Boolean executeGetter(Object object, String property) {
        return true;
        /*try {
            Method method = object.getClass().getMethod("get" + property);
            return (Boolean) method.invoke(object, null);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } */
    }

    public String getModelClass() {
        return modelClass;
    }

    public void setModelClass(String modelClass) {
        this.modelClass = modelClass;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public String getChecked() {
        if(executeGetter(getModel(), getProperty())) return "checked";
        return "";
    }
}
