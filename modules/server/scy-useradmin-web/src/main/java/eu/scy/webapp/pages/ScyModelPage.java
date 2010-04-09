package eu.scy.webapp.pages;

import eu.scy.core.model.ScyBase;
import eu.scy.core.model.impl.ScyBaseObject;

import java.util.logging.Logger;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Persist;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.okt.2008
 * Time: 06:07:34
 * To change this template use File | Settings | File Templates.
 */
public abstract class  ScyModelPage extends TapestryContextAware {


    @Property
    @Persist("entity")
    private ScyBaseObject pageModel;

    public void setModel(ScyBaseObject model) {
        System.out.println("SETTING MODEL:" + model.getClass() + " " + model);
        this.pageModel = model;
    }

    public ScyBaseObject getModel() {
        System.out.println("GETTING MODEL: " + pageModel);
        return pageModel;
    }
    


}
