package eu.scy.modules.useradmin.pages.projectmanagement;

import eu.scy.modules.useradmin.pages.SCYBasePage;
import eu.scy.core.persistence.hibernate.ProjectDAOHibernate;
import eu.scy.core.model.Project;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 08.aug.2008
 * Time: 14:11:27
 * To change this template use File | Settings | File Templates.
 */
public class EditProject extends SCYBasePage {

    @Inject
    private ProjectDAOHibernate projectDAOHibernate;

    @Persist
    private Project project;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @BeginRender
    public void showModel() {
        System.out.println("MODEL IS " + getModel());

    }

    public Object onActivate(String projectId) {
        System.out.println("ACtivating with projectttttt: " + projectId);
        setProject(projectDAOHibernate.getProject(projectId));
        return null;
    }

    public Object onSuccess() {
        System.out.println("SAVING PROJECT!!!!!");
        projectDAOHibernate.save(getProject());
        return ProjectManagement.class;
    }
}
