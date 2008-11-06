package eu.scy.pages;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import eu.scy.core.model.Project;
import eu.scy.core.model.User;
import eu.scy.core.model.ScyBase;
import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.core.persistence.UserDAO;
import eu.scy.core.persistence.hibernate.UserDAOHibernate;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 11:02:49
 * To change this template use File | Settings | File Templates.
 */
public class TapestryContextAware {

     @ApplicationState(create=false)
    private Project currentProject;
    private ScyBase model;

    @Inject
    private UserDAOHibernate userDAOHibernate;

    public UserDAO getUserDAOHibernate() {
        return userDAOHibernate;
    }

    public void setUserDAOHibernate(UserDAOHibernate userDAOHibernate) {
        this.userDAOHibernate = userDAOHibernate;
    }

    public ScyBase getModel() {
        return model;
    }

    public void setModel(ScyBase model) {
        this.model = model;
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    public String getCurrentUsersUserName() {
        if(SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
            return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        }
        return "NO SECURITY CONTEXT!!";

    }

    public User getCurrentUser() {
        return getUserDAOHibernate().getUserByUsername(getCurrentUsersUserName());
    }

    @BeginRender
    void checkForCurrentProject() {
        if(getCurrentProject() == null) {
            System.out.println("*************************************** CURRENT PROJECT NOT SET");
        } else {
            System.out.println("CURERENT IS : " + getCurrentProject());
        }
    }

}
