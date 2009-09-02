package eu.scy.webapp.pages;

import eu.scy.core.persistence.ProjectDAO;
import eu.scy.core.persistence.GroupDAO;
import eu.scy.core.model.*;
import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.core.model.impl.SCYGroupImpl;
import eu.scy.core.model.impl.SCYProjectImpl;
import eu.scy.webapp.pages.projectmanagement.ProjectEditor;

import java.util.List;
import java.util.logging.Logger;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;

/**
 * Start page of application scy-useradmin-web.
 */
public class Index extends ScyModelPage {

    private static Logger log = Logger.getLogger("Index.class");

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private GroupDAO groupDAO;

    private SCYProject scyProject;
    private UserRole userRole;

    private SCYGroup group;


    @InjectPage
    private GroupOverview groupOverview;

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }


    public SCYProject getScyProject() {
        return scyProject;
    }

    public void setScyProject(SCYProject scyProject) {
        this.scyProject = scyProject;
    }

    public List<SCYProject> getProjects() {
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

    public List<SCYGroup> getGroups() {
        return getGroupDAO().getGroupsForProject(getCurrentProject());
    }

    public SCYGroup getGroup() {
        return group;
    }

    public void setGroup(SCYGroup group) {
        this.group = group;
    }


    Object onActionFromOpenGroup(SCYGroupImpl group) {
        log.info("GID: " + group);
        groupOverview.setModel(group);
        return groupOverview;
    }

    @InjectPage
    private Index _index;

    void onActionFromOpenProject(SCYProjectImpl project) {
        log.info("Selecting project: " + project.getName());
        System.out.println("SELECTING PROJECT : " + project.getName());
        setModel(project);
        //_index.setModel(project);
        //_index.setCurrentProject(project);
        //return _index;
    }

    @InjectPage
    private ProjectEditor _projectEditor;

    public Object onActionFromCreateNewProject() {
        log.info("Creating new proejct!");
        return _projectEditor;
    }

    public Object getOpenGroupContext() {
        return getGroup();
    }




@Persist
    private String _activePanel;

    public String getActivePanel()
    {
        return _activePanel;
    }

    public void setActivePanel(String activePanel)
    {
        _activePanel = activePanel;
    }

    @OnEvent(component = "tabset", value = "action")
    public void onChange(String choosenPanelId)
    {
        _activePanel = choosenPanelId;
    }
    

}
