package eu.scy.webapp.pages;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentSource;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;

import eu.scy.core.model.SCYProject;

import eu.scy.core.model.ScyBase;
import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.persistence.ProjectDAO;
import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.persistence.hibernate.ProjectDAOHibernate;
import eu.scy.framework.PageManager;

import java.util.logging.Logger;



/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 11:02:49
 * To change this template use File | Settings | File Templates.
 */
public class TapestryContextAware {

    protected static Logger log = Logger.getLogger("BASE.class");

    @ApplicationState(create = false)
    private SCYProject currentProject;
    private ScyBaseObject model;

    @Inject
    private UserDAOHibernate userDAOHibernate;
    
    @Inject
    private ProjectDAOHibernate projectDAOHibernate;

    @Inject
    private PageManager pageManager;

    public UserDAOHibernate getUserDAO() {
        return userDAOHibernate;
    }


    public ProjectDAO getProjectDAO() {
        return projectDAOHibernate;
    }

    public PageManager getPageManager() {
        return pageManager;
    }

    public SCYProject getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(SCYProject currentProject) {
        this.currentProject = currentProject;
    }

    public String getCurrentUsersUserName() {
        if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
            return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        }
        return "NO SECURITY CONTEXT!!";

    }

    public Object getCurrentUser() {
        return null;//getUserDAO().getUserByUsername(getCurrentUsersUserName());
    }

    @BeginRender
    void checkForCurrentProject() {
        if (getCurrentProject() == null) {
            log.info("*************************************** CURRENT PROJECT NOT SET");
        } else {
            log.info("CURERENT IS : " + getCurrentProject());
        }
    }


    @Inject
    private ComponentSource compSource;

    public Object getNextPage(Object selectedObject) {
        log.info("GETTING PAGE FOR OBJECT: " + selectedObject);
        String pageId = getPageManager().getPageIdForObject(selectedObject);
        ScyModelPage comp = (ScyModelPage) compSource.getPage(pageId);
        comp.setModel(((ScyBaseObject) selectedObject));
        return comp;
    }

}
