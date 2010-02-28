package eu.scy.core.startup;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.ContextLoader;
import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.persistence.hibernate.RoleDAOHibernate;
import eu.scy.core.persistence.hibernate.ProjectDAOHibernate;
import eu.scy.core.persistence.UserDAO;
import eu.scy.core.Constants;
import eu.scy.core.model.Project;
import eu.scy.core.model.User;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.aug.2008
 * Time: 13:12:09
 * To change this template use File | Settings | File Templates.
 */
public class ConfigureDefaultSCYSettings implements ServletContextListener {

    private static Logger log = Logger.getLogger(ConfigureDefaultSCYSettings.class);
    private ServletContext context;


    public void contextInitialized(ServletContextEvent event) {

        context = event.getServletContext();

        if (log.isDebugEnabled()) {
            log.debug("contextInitialized...");
        }

        String daoType = context.getInitParameter(Constants.DAO_TYPE);

        // if daoType is not specified, use DAO as default
        if (daoType == null) {
            log.warn("No 'daoType' Context Parameter supplied in web.xml, using default (hibernate)");
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
        if (log.isDebugEnabled()) {
            log.debug("daoType: " + daoType);
            log.debug("populating drop-downs...");
        }

        setupContext(context);

        // output the retrieved values for the Init and Context Parameters
        if (log.isDebugEnabled()) {
            log.debug("Initialization complete [OK]");
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
                if (log.isDebugEnabled()) {
                    log.debug("Spring context files: " + configLocations);
                }

                ctx.setConfigLocations(files);
                ctx.setServletContext(context);
                ctx.refresh();
            }

            setupUsersAndRoles(ctx);

            // get list of possible roles
//	        context.setAttribute(Constants.AVAILABLE_ROLES, mgr.getAllRoles());
        } catch (Exception e) {
            log.error("Error populating drop-downs failed!", e);

        }
    }

    private static void setupUsersAndRoles(XmlWebApplicationContext ctx) {
        try {
            // SEQUENCE IS IMPORTANT HERE!
            setupDefaultProject(ctx);
            setupDefaultRoles(ctx);

        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    private static void setupDefaultRoles(XmlWebApplicationContext ctx) {
        RoleDAOHibernate roleDAO = (RoleDAOHibernate) ctx.getBean("roleDAO");

        if (roleDAO.getRole(Constants.ROLE_GLOBAL_ADMINISTRATOR) == null) roleDAO.saveRole(Constants.ROLE_GLOBAL_ADMINISTRATOR);


        if (roleDAO.getRole(Constants.ROLE_PROJECT_ADMINISTRATOR) == null)
            roleDAO.saveRole(Constants.ROLE_PROJECT_ADMINISTRATOR);
        if (roleDAO.getRole(Constants.ROLE_TEACHER) == null) roleDAO.saveRole(Constants.ROLE_TEACHER);
        if (roleDAO.getRole(Constants.ROLE_STUDENT) == null) roleDAO.saveRole(Constants.ROLE_STUDENT);
    }

    private static void setupDefaultProject(XmlWebApplicationContext ctx) {
        Project defaultProject = (Project) ctx.getBean("defaultProject");
        ProjectDAOHibernate projectDAO = (ProjectDAOHibernate) ctx.getBean("projectDAO");
        if (defaultProject != null) {
            log.info("Checking for default projejct... if it does not exist - I will most certainly create it!");
            if (projectDAO.findProjectsByName(defaultProject.getName()) == null || projectDAO.findProjectsByName(defaultProject.getName()).size() == 0) {
                log.info("DefaultProject not found.... creating one");
                projectDAO.createProject(defaultProject);
            }

        }

        User defaultGlobalAdmin = (User) ctx.getBean("defaultGlobalAdmin");
        User lukeSkywalker = (User) ctx.getBean("lukeSkywalker");


        if (defaultGlobalAdmin != null) {
            setupUser(defaultGlobalAdmin, ctx);
            setupUser(lukeSkywalker, ctx);

        }

    }


    private static void setupUser(User userToBeSetup, XmlWebApplicationContext ctx) {
        UserDAO userDAO = (UserDAO) ctx.getBean("userDAO");
        User theUser = userDAO.getUserByUsername(userToBeSetup.getUserName());
            if (theUser == null) {
                log.info("Adding user " + userToBeSetup.getUserName() + " - " + userToBeSetup.getName() + " - " + userToBeSetup.getLastName());

                userToBeSetup = userDAO.addUser(userToBeSetup.getProject(), userToBeSetup.getGroup(), userToBeSetup);
            }

    }
}
