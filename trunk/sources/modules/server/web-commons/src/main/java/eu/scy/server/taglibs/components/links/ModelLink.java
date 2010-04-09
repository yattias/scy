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

    public int doEndTag() throws JspException {
        try {
                
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    private String getConvertedParameterValue(Object model) {
        if (model instanceof ScyBase) {
            String convertedParameter = model.getClass().getName() + "_" + ((ScyBase) model).getId();
        }

        throw new RuntimeException("Cannot convert " + model + " to link parameter");
    }

    private String getParameter(String parameterName, Object object) {
        return parameterName + "=" + getConvertedParameterValue(object);
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public Object getParameters() {
        return parameters;
    }

    public void setParameters(Object parameters) {
        System.out.println("PARAMETERS: " + parameters);
        System.out.println("PARAMERTER TYP: " + parameters.getClass().getName());
        this.parameters = parameters;
    }
}
