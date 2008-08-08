package eu.scy.modules.useradmin.pages;

import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.apache.log4j.Logger;
import org.apache.tapestry.annotations.ApplicationState;
import org.apache.tapestry.annotations.SetupRender;
import org.apache.tapestry.annotations.BeginRender;
import eu.scy.core.model.Project;
import eu.scy.modules.useradmin.components.projectlist.ProjectList;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.aug.2008
 * Time: 14:27:08
 * To change this template use File | Settings | File Templates.
 */
public class SCYBasePage {

    @ApplicationState (create=false)
    private Project currentProject;
    private Object model;

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }


    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    public String getCurrentUsersUserName() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    @BeginRender
    void checkForCurrentProject() {
        System.out.println("BEGIN RENDER");
        if(getCurrentProject() == null) {
            System.out.println("*************************************** CURRENT PROJECT NOT SET");
            throw new RedirectException
        } else {
            System.out.println("CURERENT IS : " + getCurrentProject());
        }
    }

   

}
