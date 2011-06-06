package eu.scy.server.locale;

import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.model.UserDetails;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.mai.2011
 * Time: 11:57:42
 * To change this template use File | Settings | File Templates.
 */
public class SCYLocaleResolver extends SessionLocaleResolver implements LocaleResolver {

    private UserService userService;

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        User user = getCurrentUser(request);
        String localeString = "en";
        if(user != null) {
            UserDetails userDetails = user.getUserDetails();
            if(userDetails.getLocale() != null) localeString = userDetails.getLocale();

        }
        Locale locale = new Locale(localeString);
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public String getCurrentUserName(HttpServletRequest request) {
        org.springframework.security.userdetails.User user = (org.springframework.security.userdetails.User) request.getSession().getAttribute("CURRENT_USER");
        if(user!= null) {
            return user.getUsername();
        }
        return null;

    }

    public User getCurrentUser(HttpServletRequest request) {
        String currentUserName = getCurrentUserName(request);
        if(currentUserName != null) {
            return getUserService().getUser(currentUserName);
        }
        return null;

    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
