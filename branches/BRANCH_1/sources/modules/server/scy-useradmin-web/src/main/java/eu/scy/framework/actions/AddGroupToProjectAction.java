package eu.scy.framework.actions;

import eu.scy.framework.BaseAction;
import eu.scy.core.model.SCYProject;
import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.impl.SCYGroupImpl;
import eu.scy.core.model.impl.SCYProjectImpl;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.nov.2008
 * Time: 21:20:12
 * To change this template use File | Settings | File Templates.
 */
public class AddGroupToProjectAction extends BaseAction {

    public AddGroupToProjectAction() {
        super.setName("Add SCYGroup to SCYProject");
    }

    public Class getOperatesOn() {
        return SCYProjectImpl.class;
    }

    protected Object doAction(Object model) {
        SCYProjectImpl project = (SCYProjectImpl) model;
        System.out.println("Adding group to " + project.getName());
        SCYGroupImpl group = new SCYGroupImpl();
        group = (SCYGroupImpl) getActionManager().getUserDAOHibernate().save(group);
        getActionManager().getProjectDAOHibernate().addGroupToProject(project, group);

        project.addGroup(group);
        group.setName(project.getName());
        group.setProject(project);

        getActionManager().getProjectDAOHibernate().save(project);
        return group;
    }
}
