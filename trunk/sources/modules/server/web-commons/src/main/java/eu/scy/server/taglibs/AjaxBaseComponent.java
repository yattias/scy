package eu.scy.server.taglibs;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mar.2010
 * Time: 06:57:50
 * To change this template use File | Settings | File Templates.
 */
public class AjaxBaseComponent extends TagSupport {
    private String modelClass;
    private String modelId;
    private String property;
    private Object model;

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
}
