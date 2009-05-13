package eu.scy.modules.useradmin.components.projectlist;

import eu.scy.core.model.Project;
import eu.scy.core.persistence.hibernate.ProjectDAOHibernate;
import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.persistence.UserDAO;
import eu.scy.modules.useradmin.pages.projectmanagement.EditProject;

import java.util.List;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.aug.2008
 * Time: 15:29:53
 * To change this template use File | Settings | File Templates.
 */
public class ProjectList {

    private Project project;

    @ApplicationState
    private Project currentProject;

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    @Inject
    private UserDAOHibernate userDAOHibernate;

    public UserDAO getUserDAO() {
        return userDAOHibernate;
    }

    public void setUserDAOHibernate(UserDAOHibernate userDAOHibernate) {
        this.userDAOHibernate = userDAOHibernate;
    }

    @Inject
    private ProjectDAOHibernate projectDAOHibernate;


    public ProjectDAOHibernate getProjectDAOHibernate() {
        return projectDAOHibernate;
    }

    public void setProjectDAOHibernate(ProjectDAOHibernate projectDAOHibernate) {
        this.projectDAOHibernate = projectDAOHibernate;
    }

    public List getProjects() {
        return getProjectDAOHibernate().getAllProjects();

    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Object onActionFromSelectProject(String projectId) {
        System.out.println("setting project" + projectId);
        setCurrentProject(getProjectDAOHibernate().getProject(projectId));
        return null;
    }

    public long getNumberOfGroups() {
        return getUserDAO().getNumberOfGroups(getProject());
    }

    public long getNumberOfUsers() {
        return getUserDAO().getNumberOfUsers(getProject());
    }


    @InjectPage
    private EditProject editProjectPage;

    /*Object onActionFromEdit(String projectId) {
        System.out.println("activating " + projectId);
        Project p = getProjectDAOHibernate().getProject(projectId);
        System.out.println("PROJECT IS " + p.getName());
        
        editProjectPage.setModel(project);
        return editProjectPage;

    } */
}
