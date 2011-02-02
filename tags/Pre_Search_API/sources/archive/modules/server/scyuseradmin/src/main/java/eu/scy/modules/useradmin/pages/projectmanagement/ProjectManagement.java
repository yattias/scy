package eu.scy.modules.useradmin.pages.projectmanagement;

import eu.scy.core.model.Project;
import eu.scy.core.persistence.hibernate.ProjectDAOHibernate;
import eu.scy.modules.useradmin.pages.SCYBasePage;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.aug.2008
 * Time: 15:49:03
 * To change this template use File | Settings | File Templates.
 */
public class ProjectManagement extends SCYBasePage {

    @Inject
    private ProjectDAOHibernate projectDAOHibernate;

    public ProjectDAOHibernate getProjectDAOHibernate() {
        return projectDAOHibernate;
    }

    public void setProjectDAOHibernate(ProjectDAOHibernate projectDAOHibernate) {
        this.projectDAOHibernate = projectDAOHibernate;
    }

    public void onActivateFromSetProject(String id) {
        Project p = getProjectDAOHibernate().getProject(id);
        setModel(p);
        setCurrentProject(p);
    }



}
