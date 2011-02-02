package eu.scy.modules.useradmin.pages;

import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.apache.log4j.Logger;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.BeginRender;
import eu.scy.core.model.Project;
import eu.scy.core.model.SCYBaseObject;
import eu.scy.modules.useradmin.components.projectlist.ProjectList;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.aug.2008
 * Time: 14:27:08
 * To change this template use File | Settings | File Templates.
 */
public class SCYBasePage {

    @ApplicationState(create=false)
    private Project currentProject;
    private SCYBaseObject model;

    public SCYBaseObject getModel() {
        return model;
    }

    public void setModel(SCYBaseObject model) {
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

    @BeginRender
    void checkForCurrentProject() {
        if(getCurrentProject() == null) {
            System.out.println("*************************************** CURRENT PROJECT NOT SET");
        } else {
            System.out.println("CURERENT IS : " + getCurrentProject());
        }
    }

   

}
