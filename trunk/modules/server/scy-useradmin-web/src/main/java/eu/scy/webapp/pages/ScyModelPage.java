package eu.scy.webapp.pages;

import eu.scy.core.model.ScyBase;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.okt.2008
 * Time: 06:07:34
 * To change this template use File | Settings | File Templates.
 */
public abstract class  ScyModelPage extends TapestryContextAware {



    private String modelId;
    private ScyBase model;

    public abstract void loadModel();

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        log.info("Set model id"  + modelId + "("  + getClass().getName() + ")");
        this.modelId = modelId;
    }

    public ScyBase getModel() {
        return model;
    }

    public void setModel(ScyBase model) {
        log.info("Set model:" + model + "("  + getClass().getName() + ")");
        this.model = model;
    }

    public void onActivate(String id) {
        log.info("** ** ** ** ** ** MODEL ID: " + id);
        modelId = id;
        if (id != null) {
            loadModel();
        }

    }

    public String onPassivate() {
        return modelId;
    }


}
