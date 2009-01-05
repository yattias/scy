package eu.scy.framework.actions;

import eu.scy.framework.BaseAction;
import eu.scy.core.model.Group;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.jan.2009
 * Time: 06:12:12
 * To change this template use File | Settings | File Templates.
 */
public class EditGroup extends BaseAction {

    public EditGroup() {
        super.setName("Edit Group");
    }

    public Class getOperatesOn() {
        return Group.class;
    }

    protected Object doAction(Object model) {
        Group group = (Group) model;
        return group;
    }
}
