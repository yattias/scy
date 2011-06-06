package eu.scy.server.taglibs.components.links;

import eu.scy.core.model.ScyBase;

import javax.servlet.jsp.JspException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.apr.2010
 * Time: 06:02:29
 * To change this template use File | Settings | File Templates.
 */
public class ModelLink extends AbstractLink {

    private Object model;
    private Object parameters;
    private String context;
    private String href;

    public int doEndTag() throws JspException {
        try {
             pageContext.getOut().write(START_TAG + getHref() + "?" + getParameter("model",getModel()) + "\">" + getBodyContent().getString() + "</a>");        
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    private String getConvertedParameterValue(Object model) {
        // System.out.println("converting: " +model.getClass().getName());
        if (model instanceof ScyBase) {
            String convertedParameter = model.getClass().getName() + "_" + ((ScyBase) model).getId();
            return convertedParameter;
        } else {
        throw new RuntimeException("Cannot convert " + model + " to link parameter, it is not instanceof ScyBase!");
        }
    }

    private String getParameter(String parameterName, Object object) {
        return parameterName + "=" + getConvertedParameterValue(object);
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        // System.out.println("MODEL: " + model);
        // System.out.println("MODEL TYPE: " + model.getClass().getName());
        this.model = model;
    }

    public Object getParameters() {
        return parameters;
    }

    public void setParameters(Object parameters) {
        // System.out.println("PARAMETERS: " + parameters);
        // System.out.println("PARAMERTER TYP: " + parameters.getClass().getName());
        this.parameters = parameters;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
