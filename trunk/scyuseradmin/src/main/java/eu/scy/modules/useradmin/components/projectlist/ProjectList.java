package eu.scy.modules.useradmin.components.projectlist;

import org.apache.tapestry.ioc.annotations.Inject;

import java.util.List;

import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.persistence.hibernate.ProjectDAOHibernate;
import eu.scy.core.model.Project;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.aug.2008
 * Time: 15:29:53
 * To change this template use File | Settings | File Templates.
 */
public class ProjectList {

    private Project project;

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
}
