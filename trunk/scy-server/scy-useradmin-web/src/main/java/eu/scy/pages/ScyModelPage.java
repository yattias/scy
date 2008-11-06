package eu.scy.pages;

import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.core.model.ScyBase;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.okt.2008
 * Time: 06:07:34
 * To change this template use File | Settings | File Templates.
 */
public abstract class ScyModelPage extends TapestryContextAware {

    private String modelId;
    private ScyBase model;

    public abstract void loadModel();

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public ScyBase getModel() {
        return model;
    }

    public void setModel(ScyBase model) {
        this.model = model;
    }

    public void onActivate(String id) {
        System.out.println("** ** ** ** ** ** MODEL ID: " + id);
        modelId = id;
        if (id != null) {
            loadModel();
        }

    }

    public String onPassivate() {
        return modelId;
    }


}
