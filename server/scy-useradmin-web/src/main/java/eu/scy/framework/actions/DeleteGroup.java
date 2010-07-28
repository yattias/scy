package eu.scy.framework.actions;

import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.SCYProject;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.jan.2009
 * Time: 12:21:58
 * To change this template use File | Settings | File Templates.
 */
public class DeleteGroup extends DeleteAction {

    public Class getOperatesOn() {
        return SCYGroup.class;
    }

    protected Object doAction(Object model) {
        SCYGroup g = (SCYGroup) model;
        SCYProject p = g.getProject();
        getActionManager().getUserDAOHibernate().deleteGroup(g.getId());
        return p;
    }
}
