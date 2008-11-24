package eu.scy.framework.actions;

import eu.scy.framework.BaseAction;
import eu.scy.core.model.Project;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.nov.2008
 * Time: 21:20:12
 * To change this template use File | Settings | File Templates.
 */
public class AddGroupToProjectAction extends BaseAction {

    public AddGroupToProjectAction() {
        super.setName("Add Group to Project");
    }

    public Class getOperatesOn() {
        return Project.class;
    }

    protected Object doAction(Object model) {
        log.info("Adding group to project " + model);
        return null;
    }
}
