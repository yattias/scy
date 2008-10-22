package eu.scy.components.border;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.Asset;
import eu.scy.pages.TapestryContextAware;
import eu.scy.core.persistence.UserSessionDAO;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 10:55:54
 * To change this template use File | Settings | File Templates.
 */
public class Layout extends TapestryContextAware {


    @Inject
    private UserSessionDAO userSessionDAO;

    public UserSessionDAO getUserSessionDAO() {
        return userSessionDAO;
    }

    public void setUserSessionDAO(UserSessionDAO userSessionDAO) {
        this.userSessionDAO = userSessionDAO;
    }

    public String getCurrentProjectName() {
        if (getCurrentProject() == null) return "No project set";
        else return getCurrentProject().getName();
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


}
