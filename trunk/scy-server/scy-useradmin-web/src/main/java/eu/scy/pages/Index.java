package eu.scy.pages;

import eu.scy.pages.TapestryContextAware;
import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.persistence.UserDAO;
import eu.scy.core.persistence.ProjectDAO;
import eu.scy.core.persistence.GroupDAO;
import eu.scy.core.model.*;
import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.core.model.impl.GroupImpl;

import java.util.Date;
import java.util.List;
import java.util.Collections;
import java.util.logging.Logger;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.Service;
import org.springframework.web.context.WebApplicationContext;

/**
 * Start page of application scy-useradmin-web.
 */
public class Index  extends ScyModelPage {

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private GroupDAO groupDAO;

    private Project scyProject;
    private UserRole userRole;

    private Group group;


    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void loadModel() {
        setModel((ScyBaseObject) getProjectDAO().getProject(getModelId()));
        setCurrentProject((Project) getModel());
    }

    public Project getScyProject() {
        return scyProject;
    }

    public void setScyProject(Project scyProject) {
        this.scyProject = scyProject;
    }

    public List<Project> getProjects() {
        return getProjectDAO().getAllProjects();
    }

    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public GroupDAO getGroupDAO() {
        return groupDAO;
    }

    public void setGroupDAO(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    public List <Group> getGroups() {
        return getGroupDAO().getGroupsForProject(getCurrentProject());
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    Object onActionFromDelete(String groupId) {
        System.out.println("GROUP: " + groupId);
        System.out.println("GROUP: " + groupId);
        System.out.println("GROUP: " + groupId);
        System.out.println("GROUP: " + groupId);
        System.out.println("GROUP: " + groupId);
        System.out.println("GROUP: " + groupId);
        System.out.println("GROUP: " + groupId);
        System.out.println("GROUP: " + groupId);
        System.out.println("GROUP: " + groupId);
        System.out.println("GROUP: " + groupId);
        return GroupOverview.class;
    }

}
