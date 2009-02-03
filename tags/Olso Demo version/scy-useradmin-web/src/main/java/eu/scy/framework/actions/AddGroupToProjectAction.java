package eu.scy.framework.actions;

import eu.scy.framework.BaseAction;
import eu.scy.core.model.Project;
import eu.scy.core.model.Group;
import eu.scy.core.model.impl.GroupImpl;

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
        Project project = (Project) model;
        log.info("Adding group to " + project.getName());
        Group group = new GroupImpl();
        getActionManager().getProjectDAOHibernate().addGroupToProject(project, group);

        project.addGroup(group);
        group.setName(project.getName());
        group.setProject(project);

        getActionManager().getProjectDAOHibernate().save(project);
        return group;
    }
}
