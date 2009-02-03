package eu.scy.framework.actions;

import eu.scy.framework.BaseAction;
import eu.scy.core.model.Group;
import eu.scy.core.model.Project;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.jan.2009
 * Time: 12:21:58
 * To change this template use File | Settings | File Templates.
 */
public class DeleteGroup extends DeleteAction {

    public Class getOperatesOn() {
        return Group.class;
    }

    protected Object doAction(Object model) {
        Group g = (Group) model;
        Project p = g.getProject();
        getActionManager().getUserDAOHibernate().deleteGroup(g.getId());
        return p;
    }
}
