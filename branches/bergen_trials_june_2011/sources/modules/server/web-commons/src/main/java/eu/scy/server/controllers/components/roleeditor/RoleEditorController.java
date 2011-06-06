package eu.scy.server.controllers.components.roleeditor;

import eu.scy.core.UserService;
import eu.scy.core.model.SCYGrantedAuthority;
import eu.scy.core.model.User;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.aug.2010
 * Time: 12:42:07
 * To change this template use File | Settings | File Templates.
 */
public class RoleEditorController  extends AbstractController {

    private UserService userService;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView modelAndView = new ModelAndView();

        String successView = request.getParameter("successView");

        String role = request.getParameter("role");
        String username = request.getParameter("username");
        logger.info("username: " + username);
        User user = getUserService().getUser(username);
        if(user == null) logger.warn("USER IS NULL");
        if(role == null) logger.warn ("ROLE IS NULL");
        if(user.getUserDetails().hasGrantedAuthority(role)) {
            SCYGrantedAuthority[] authories = user.getUserDetails().getAuthorities();
            for (int i = 0; i < authories.length; i++) {
                SCYGrantedAuthority authory = authories[i];
                if(authory.getAuthority().equals(role)) user.getUserDetails().getGrantedAuthorities().remove(authory);
            }
        } else {
            List <SCYGrantedAuthority> grantedAuthorities=  getUserService().getGrantedAuthorities();
            for (int i = 0; i < grantedAuthorities.size(); i++) {
                SCYGrantedAuthority scyGrantedAuthority = grantedAuthorities.get(i);
                if(scyGrantedAuthority.getAuthority().equals(role)) user.getUserDetails().getGrantedAuthorities().add(scyGrantedAuthority);
            }
        }

        getUserService().save(user);



        modelAndView.setViewName("forward:" + successView);
        return modelAndView;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
