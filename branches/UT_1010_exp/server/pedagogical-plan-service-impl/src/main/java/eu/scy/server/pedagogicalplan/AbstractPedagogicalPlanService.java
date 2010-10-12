package eu.scy.server.pedagogicalplan;

import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.model.auth.SessionInfo;
import eu.scy.core.model.impl.auth.SessionInfoImpl;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.jan.2010
 * Time: 12:29:31
 * To change this template use File | Settings | File Templates.
 */
public class AbstractPedagogicalPlanService {

    private static Logger log = Logger.getLogger("AbstractPedagogicalPlanService.class");

    private UserService userService = null;

    public SessionInfo login(String username, String password) {
        log.info("logging in " + username +  " " + password);
        SessionInfo sessionInfo = new SessionInfoImpl();
        sessionInfo.setUserName(username);
        sessionInfo.setPassword(password);

        User user = getUserService().getUser(username);
        if(user == null || !user.getUserDetails().getPassword().equals(password)) {
            log.warning("Incorrect username or password");
            return null;
        }

        return sessionInfo;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
