package eu.scy.listeners;

import eu.scy.core.model.SCYProject;
import eu.scy.core.model.User;
//import eu.scy.core.model.User;
//import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.impl.SCYProjectImpl;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.impl.SCYUserDetails;
import eu.scy.core.persistence.hibernate.ProjectDAOHibernate;
import eu.scy.core.persistence.UserDAO;
import eu.scy.framework.Constants;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.ContextLoader;



/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 25.nov.2008
 * Time: 09:47:36
 * To change this template use File | Settings | File Templates.
 */
public class ConfigureDefaultScySettings implements ServletContextListener {
    private static Logger log = Logger.getLogger("ConfigureDefaultScySettings.class");
    private ServletContext context;


    public void contextInitialized(ServletContextEvent event) {

        context = event.getServletContext();

        if (log.isLoggable(Level.FINEST)) {
            log.fine("contextInitialized...");
        }

        String daoType = context.getInitParameter(Constants.DAO_TYPE);

        // if daoType is not specified, use DAO as default
        if (daoType == null) {
            log.log(Level.WARNING, "No 'daoType' Context Parameter supplied in web.xml, using default (hibernate)");
            daoType = Constants.DAO_TYPE_HIBERNATE;
        }

        // Orion starts Servlets before Listeners, so check if the config
        // object already exists
        Map config = (HashMap) context.getAttribute(Constants.CONFIG);

        if (config == null) {
            config = new HashMap();
        }

        // Create a config object to hold all the app config values
        config.put(Constants.DAO_TYPE, daoType);
        context.setAttribute(Constants.CONFIG, config);

        // output the retrieved values for the Init and Context Parameters
        if (log.isLoggable(Level.FINEST)) {
            log.fine("daoType: " + daoType);
            log.fine("populating drop-downs...");
        }

        setupContext(context);

        // output the retrieved values for the Init and Context Parameters
        if (log.isLoggable(Level.FINEST)) {
            log.finest("Initialization complete [OK]");
        }
    }

    public void contextDestroyed(ServletContextEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public static void setupContext(ServletContext context) {
        try {

            XmlWebApplicationContext ctx = (XmlWebApplicationContext) context.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            if (ctx == null) {
                // if the context is null, it's likely because the listeners
                // aren't initialized in the same order they're specified in
                // web.xml.  This happens in Tomcat 5.
                ctx = new XmlWebApplicationContext();
                // get the config locations from context parameters
                String configLocations =
                        context.getInitParameter(ContextLoader.CONFIG_LOCATION_PARAM);
                String[] files = configLocations.split(",");
                for (int i = 0; i < files.length; i++) {
                    files[i] = files[i].trim();
                }
                if (log.isLoggable(Level.FINEST)) {
                    log.finest("Spring context files: " + configLocations);
                }

                ctx.setConfigLocations(files);
                ctx.setServletContext(context);
                ctx.refresh();
            }

            setupUsersAndRoles(ctx);

            // get list of possible roles
//	        context.setAttribute(Constants.AVAILABLE_ROLES, mgr.getAllRoles());
        } catch (Exception e) {
            log.warning("Error populating drop-downs failed!");

        }
    }

    private static void setupUsersAndRoles(XmlWebApplicationContext ctx) {
        try {
            // SEQUENCE IS IMPORTANT HERE!
            setupDefaultProject(ctx);
            //setupDefaultRoles(ctx);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private static void setupDefaultRoles(XmlWebApplicationContext ctx) {
        RoleDAOHibernate roleDAO = (RoleDAOHibernate) ctx.getBean("roleDAO");

        if (roleDAO.getRole(Constants.ROLE_GLOBAL_ADMINISTRATOR) == null) roleDAO.saveRole(Constants.ROLE_GLOBAL_ADMINISTRATOR);


        if (roleDAO.getRole(Constants.ROLE_PROJECT_ADMINISTRATOR) == null)
            roleDAO.saveRole(Constants.ROLE_PROJECT_ADMINISTRATOR);
        if (roleDAO.getRole(Constants.ROLE_TEACHER) == null) roleDAO.saveRole(Constants.ROLE_TEACHER);
        if (roleDAO.getRole(Constants.ROLE_STUDENT) == null) roleDAO.saveRole(Constants.ROLE_STUDENT);
    } */

    private static void setupDefaultProject(XmlWebApplicationContext ctx) {
        log.info("*** **** **** **** SETTING UP DEFAULT PROJECT!");
        SCYProject defaultProject = new SCYProjectImpl();
        defaultProject.setName("SCY-WARS");
        ProjectDAOHibernate projectDAO = (ProjectDAOHibernate) ctx.getBean("projectDAO");
        if (defaultProject != null) {
            log.info("Checking for default projejct... if it does not exist - I will most certainly create it!");
            if (projectDAO.findProjectsByName(defaultProject.getName()) == null || projectDAO.findProjectsByName(defaultProject.getName()).size() == 0) {
                log.info("DefaultProject not found.... creating one");
                projectDAO.createProject(defaultProject);
            }

        }

        //UserDAO userDAO = (UserDAO) ctx.getBean("userDAO");
        /*User defaultGlobalAdmin = new SCYUserImpl();
        SCYUserDetails vaderDetails = new SCYUserDetails();
        vaderDetails.setUsername("scy");
        vaderDetails.setPassword("scy");
        vaderDetails.setFirstname("Darth");
        vaderDetails.setLastname("Vader");
        vaderDetails.setSignupdate(new Date());
        vaderDetails.setGender(Gender.MALE);
        vaderDetails.setBirthday(new Date());
        vaderDetails.setNumberOfLogins(0);
        vaderDetails.setAccountQuestion("Who are you");
        vaderDetails.setAccountAnswer("vader");
        defaultGlobalAdmin.setUserDetails(vaderDetails);
        */
/*
        User lukeSkywalkekr = new SCYUserImpl();
        lukeSkywalkekr.setUserName("luke");
        lukeSkywalkekr.setPassword("luke");
        lukeSkywalkekr.setFirstName("Luke");
        lukeSkywalkekr.setLastName("SCYWalker");
  */
        //User defaultGlobalAdmin = (User) ctx.getBean("defaultGlobalAdmin");
        //User lukeSkywalker = (User) ctx.getBean("lukeSkywalker");


        /*if (defaultGlobalAdmin != null) {
            setupUser(defaultGlobalAdmin, ctx);
            //setupUser(lukeSkywalkekr, ctx);

        } */

    }


    private static void setupUser(User userToBeSetup, XmlWebApplicationContext ctx) {
        /*log.info("SETTING UP USER");
        UserDAO userDAO = (UserDAO) ctx.getBean("userDAO");
        User theUser = userDAO.getUserByUsername(userToBeSetup.getUserDetails().getUsername());
            if (theUser == null) {
                log.info("Adding user " + userToBeSetup.getUserDetails().getUsername() + " - " + userToBeSetup.getUserDetails().getUsername() + " - " + userToBeSetup.getUserDetails().getEmailAddress());
                if(userToBeSetup.getId() == null) {
                    userDAO.save(userToBeSetup.getUserDetails());
                }
                userToBeSetup = userDAO.addUser(null, null, userToBeSetup);
            } else {
                log.info("User " + userToBeSetup.getUserDetails().getUsername() + " already exists");
            }
          */
    }


}
