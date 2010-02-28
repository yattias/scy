package eu.scy.listeners;

import eu.scy.core.persistence.UserDAO;
import eu.scy.core.persistence.UserSessionDAO;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationTrustResolver;
import org.springframework.security.AuthenticationTrustResolverImpl;
import org.springframework.security.context.HttpSessionContextIntegrationFilter;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.User;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 21.okt.2008
 * Time: 07:21:30
 * Listener that records each time a users logs onto the system and creates a new session. this listener only listens
 * to logons through the web authentication. If users are using web services - another listener will do the works.
 */
public class UserSessionListener implements ServletContextListener, HttpSessionAttributeListener {

    public static final String COUNT_KEY = "userCounter";
    public static final String USERS_KEY = "userNames";
    public static final String EVENT_KEY = HttpSessionContextIntegrationFilter.SPRING_SECURITY_CONTEXT_KEY;
    //private final transient Log log = LogFactory.getLog(UserSessionListener.class);
    private transient ServletContext servletContext;
    private int counter;
    private static Set users;


    public static Set getUsers() {
        return users;

    }

    public synchronized void contextInitialized(ServletContextEvent sce) {
        servletContext = sce.getServletContext();
        servletContext.setAttribute((COUNT_KEY), Integer.toString(counter));
    }

    public synchronized void contextDestroyed(ServletContextEvent event) {
        servletContext = null;
        users = null;
        counter = 0;
    }

    synchronized void incrementUserCounter() {
        counter =
                Integer.parseInt((String) servletContext.getAttribute(COUNT_KEY));
        counter++;
        servletContext.setAttribute(COUNT_KEY, Integer.toString(counter));

    }

    synchronized void decrementUserCounter() {
        int counter =
                Integer.parseInt((String) servletContext.getAttribute(COUNT_KEY));
        counter--;

        if (counter < 0) {
            counter = 0;
        }

        servletContext.setAttribute(COUNT_KEY, Integer.toString(counter));
    }

    synchronized void addUsername(Object user) {
        users = (Set) servletContext.getAttribute(USERS_KEY);

        if (users == null) {
            users = new HashSet();
        }

        users.add(user);
        servletContext.setAttribute(USERS_KEY, users);
        incrementUserCounter();
    }

    synchronized void removeUsername(Object user) {
        users = (Set) servletContext.getAttribute(USERS_KEY);

        if (users != null) {
            users.remove(user);
        }

        servletContext.setAttribute(USERS_KEY, users);
        decrementUserCounter();
    }

    /**
     * This method is designed to catch when user's login and record their name
     *
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeAdded(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void attributeAdded(HttpSessionBindingEvent event) {
        if (event.getName().equals(EVENT_KEY) && !isAnonymous()) {
            ServletContext servletContext = event.getSession().getServletContext();
            WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
            UserSessionDAO sessionDAO = (UserSessionDAO) wac.getBean("userSessionDAO");
            UserDAO userDAO = (UserDAO) wac.getBean("userDAO");
            if (sessionDAO == null) throw new RuntimeException("NO USER DAO AVAILABLE!!");
            SecurityContext securityContext = (SecurityContext) event.getValue();
            User user = (User) securityContext.getAuthentication().getPrincipal();
            if (user != null && user.getUsername() != null) {
                try {
                    throw new RuntimeException("NOT IMPLEMENTED YET");
                    //sessionDAO.loginUser(userDAO.getUserByUsername(user.getUsername()));
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                addUsername(user);
            }

        }
    }

    private boolean isAnonymous() {
        AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
        SecurityContext ctx = SecurityContextHolder.getContext();
        if (ctx != null) {
            Authentication auth = ctx.getAuthentication();
            return resolver.isAnonymous(auth);
        }
        return true;
    }

    /**
     * When user's logout, remove their name from the hashMap
     *
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeRemoved(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void attributeRemoved(HttpSessionBindingEvent event) {
        // test the spring framework
        if (event.getName().equals(EVENT_KEY) && !isAnonymous()) {

            ServletContext servletContext = event.getSession().getServletContext();
            WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
            UserDAO dao = (UserDAO) wac.getBean("userDAO");
            UserSessionDAO usd = (UserSessionDAO) wac.getBean("userSessionDAO");
            SecurityContext securityContext = (SecurityContext) event.getValue();
            Authentication auth = securityContext.getAuthentication();
            if (auth != null && (auth.getPrincipal() instanceof User)) {
                User user = (User) auth.getPrincipal();
                removeUsername(user);
                throw new RuntimeException("NOT IMPLEMENTED YET!!");
            }
        }
    }

    /**
     * Needed for Acegi Security 1.0, as it adds an anonymous user to the session and
     * then replaces it after authentication. http://forum.springframework.org/showthread.php?p=63593
     *
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeReplaced(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void attributeReplaced(HttpSessionBindingEvent event) {
        if (event.getName().equals(EVENT_KEY) && !isAnonymous()) {
            SecurityContext securityContext = (SecurityContext) event.getValue();
            if (securityContext.getAuthentication() != null) {
                User user = (User) securityContext.getAuthentication().getPrincipal();
                addUsername(user);
            }
        }
    }

}
