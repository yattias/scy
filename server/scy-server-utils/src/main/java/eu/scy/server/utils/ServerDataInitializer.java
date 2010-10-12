package eu.scy.server.utils;

import eu.scy.core.UserService;
import eu.scy.core.model.User;
import org.springframework.beans.factory.InitializingBean;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.des.2009
 * Time: 10:23:07
 * Initializes server with necessary data - like obligatory users for datasync etc
 */
public class ServerDataInitializer implements InitializingBean {


    private static Logger log = Logger.getLogger("ServerDataInitializer.class");

    private UserService userService;

    /**
     * Checks if the obligatory user for datasync service is in the database. If not, the user is created.
     */

    public void checkForDataSyncServiceUser() {
        User datasyncUser = getUserService().getUser("datasynclistener");
        if(datasyncUser == null) {
            log.warning("DATA SYNC LISTENER USER IS NOT DEFINED - DOING IT NOW!");
            User dataSyncUser = createUser("datasynclistener", "datasync");
        }
    }

    private User createUser(String username, String password) {
        return getUserService().createUser(username, password, "ROLE_STUDENT");
    }

    private void generateDummyUsers() {
        addUserIfNotExists("Henrik", "von Schlanbusch", "hillary", "clinton");
        addUserIfNotExists("Adam", "Gizzy", "adie", "aaad");

        addUserIfNotExists("Luke", "ScyWalker", "luke", "scy");
        addUserIfNotExists("Darth", "Vader", "digital", "face");
        addUserIfNotExists("Storm", "Trooper", "printer", "masquerade");
    }

    private void addUserIfNotExists(String firstName, String lastName, String userName, String password) {
        log.info("ADding user if not exists: " + firstName + " " + lastName + " " + userName + " shhhhh " + password);
        if (getUserService().getUser(userName) == null) {
            getUserService().createUser(userName, password, "ROLE_STUDENT");
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        //checkForDataSyncServiceUser();
        createDefaultUsers();

    }

    private void createDefaultUsers() {
        generateDummyUsers();       

    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
