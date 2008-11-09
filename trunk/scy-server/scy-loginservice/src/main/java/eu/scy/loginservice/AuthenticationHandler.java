package eu.scy.loginservice;

import eu.scy.core.persistence.UserSessionDAO;

import javax.jws.WebService;
import javax.jws.WebMethod;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.nov.2008
 * Time: 14:17:31
 * A web service used for tools that need to login to the scy framework
 */

@WebService
public class AuthenticationHandler {

    private static Logger log = Logger.getLogger("AuthenticationHandler.class");

    private UserSessionDAO userSessionManagerDAO;

    public void setUserSessionManagerDAO(UserSessionDAO userSessionManagerDAO) {
        this.userSessionManagerDAO = userSessionManagerDAO;
    }


    @WebMethod(operationName = "login" )
    public String login(String userName, String password) {
        log.info("Logging in : " + userName);
        //userSessionManagerDAO.loginUser();
        return "";

    }

}
