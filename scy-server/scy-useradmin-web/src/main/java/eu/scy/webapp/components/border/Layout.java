package eu.scy.webapp.components.border;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.internal.services.PagePool;
import eu.scy.webapp.pages.TapestryContextAware;
import eu.scy.webapp.pages.ScyModelPage;
import eu.scy.webapp.pages.GroupOverview;
import eu.scy.webapp.pages.PageLocator;
import eu.scy.core.persistence.UserSessionDAO;
import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.core.model.ScyBase;
import eu.scy.framework.ActionManager;
import eu.scy.framework.BaseAction;
import eu.scy.framework.PageManager;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 10:55:54
 * To change this template use File | Settings | File Templates.
 */
public class Layout extends TapestryContextAware {

    private static Logger log = Logger.getLogger("Layout.class");


    private BaseAction baseAction;

    public BaseAction getBaseAction() {
        return baseAction;
    }

    public void setBaseAction(BaseAction baseAction) {
        this.baseAction = baseAction;
    }

    @Inject
    private ActionManager actionManager;

    @Inject
    private PageManager pageManager;

    @Inject
    private UserSessionDAO userSessionDAO;

    public UserSessionDAO getUserSessionDAO() {
        return userSessionDAO;
    }

    public void setUserSessionDAO(UserSessionDAO userSessionDAO) {
        this.userSessionDAO = userSessionDAO;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public void setActionManager(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    public PageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(PageManager pageManager) {
        this.pageManager = pageManager;
    }

    public String getCurrentProjectName() {
        if (getCurrentProject() == null) return "No project set";
        else return getCurrentProject().getName();
    }

    @Parameter
    private Object userObject;

    public Object getUserObject() {
        return userObject;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
        log.info("Current userobject is: " + userObject);
    }

    @Inject
    @Path("context:graphics/scy_home.png")
    private Asset scyHomeIcon;

    public Asset getScyHomeIcon() {
        return scyHomeIcon;
    }

    @Inject
    @Path("context:graphics/scy_useradmin.png")
    private Asset scyUserAdminIcon;

    public Asset getScyUserAdminIcon() {
        return scyUserAdminIcon;
    }

    @Inject
    @Path("context:graphics/scy_projectmanagement.png")
    private Asset scyProjectManagementIcon;

    public Asset getScyProjectManagementIcon() {
        return scyProjectManagementIcon;
    }

    @Inject
    private RequestGlobals requestGlobals;

    public Object onActionFromLogout() {
        requestGlobals.getRequest().getSession(true).invalidate();
        return null;
    }

    public List getActions() {
        return getActionManager().getActions(getUserObject());
    }


    @InjectPage
    private PageLocator pageLocator;

    @Inject
  private ComponentSource compSource;



    public Object onActionFromActionMenuItem(ScyBaseObject scyBaseObject, String actionId)  {
        BaseAction action = getActionManager().getActionById(actionId);
        action.setActionManager(getActionManager());
        if(action != null) {
            Object theObject = action.actionPerformed(scyBaseObject);
            String pageId = getPageManager().getPageIdForObject(theObject);
            ScyModelPage comp = (ScyModelPage) compSource.getPage(pageId);
            comp.setModelId(((ScyBase) theObject).getId());
            return comp;            
        }
        return null;
    }


}
