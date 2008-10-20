package eu.scy.pages;

import eu.scy.pages.TapestryContextAware;
import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.persistence.UserDAO;
import eu.scy.core.persistence.ProjectDAO;
import eu.scy.core.model.User;
import eu.scy.core.model.Project;
import eu.scy.core.model.impl.ScyBaseObject;

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
    private Project scyProject;

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

    


}
