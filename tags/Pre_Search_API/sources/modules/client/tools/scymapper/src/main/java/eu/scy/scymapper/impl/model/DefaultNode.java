package eu.scy.scymapper.impl.model;

import eu.scy.core.model.pedagogicalplan.BaseObject;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2009
 * Time: 12:13:39
 * To change this template use File | Settings | File Templates.
 */
public class DefaultNode extends NodeModel{

    private Object object;

    @Override
    public String getLabel() {
        if(getObject() instanceof BaseObject) {
            return ((BaseObject)getObject()).getName();
        }
        return super.getLabel();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
