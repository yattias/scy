package eu.scy.components.border;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.Asset;
import eu.scy.pages.TapestryContextAware;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 10:55:54
 * To change this template use File | Settings | File Templates.
 */
public class Layout extends TapestryContextAware {


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

        //Inject
        /*private UserSessionServiceImpl userSessionService;

        public Object onActionFromLogout() {
            userSessionService.logoutUser(requestGlobals.getHTTPServletRequest().getSession());
            return null;
        }
        */



}
