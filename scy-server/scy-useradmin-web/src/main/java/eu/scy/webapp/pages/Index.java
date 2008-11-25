package eu.scy.webapp.pages;

import eu.scy.core.persistence.ProjectDAO;
import eu.scy.core.persistence.GroupDAO;
import eu.scy.core.model.*;
import eu.scy.core.model.impl.ScyBaseObject;
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

    private Project scyProject;
    private UserRole userRole;

    private Group group;


    @InjectPage
    private GroupOverview groupOverview;

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

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

    public GroupDAO getGroupDAO() {
        return groupDAO;
    }

    public void setGroupDAO(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    public List<Group> getGroups() {
        return getGroupDAO().getGroupsForProject(getCurrentProject());
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }


    Object onActionFromOpenGroup(String groupId) {
        Group group = getGroupDAO().getGroup(groupId);
        log.info("Loading group :" + group.getName());
        groupOverview.setModelId(groupId);
        return groupOverview;
    }

    @InjectPage
    private ProjectEditor _projectEditor;

    public Object onActionFromCreateNewProject() {
        log.info("Creating new proejct!");
        return _projectEditor;
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
