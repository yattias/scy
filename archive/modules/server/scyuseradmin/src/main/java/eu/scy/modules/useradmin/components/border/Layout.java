package eu.scy.modules.useradmin.components.border;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.aug.2008
 * Time: 13:01:15
 * To change this template use File | Settings | File Templates.
 */

import eu.scy.modules.useradmin.pages.SCYBasePage;
import eu.scy.modules.useradmin.pages.Start;
import eu.scy.core.service.impl.UserSessionServiceImpl;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.services.Session;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.log4j.Logger;

public class Layout extends SCYBasePage {

    private static Logger log = Logger.getLogger(Layout.class);

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

    @Inject
    private UserSessionServiceImpl userSessionService;

    public Object onActionFromLogout() {
        userSessionService.logoutUser(requestGlobals.getHTTPServletRequest().getSession());
        return null; 
    }


}
