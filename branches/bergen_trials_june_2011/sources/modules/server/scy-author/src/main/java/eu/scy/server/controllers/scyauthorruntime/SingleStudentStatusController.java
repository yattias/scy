package eu.scy.server.controllers.scyauthorruntime;

import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.server.controllers.BaseController;
import info.collide.sqlspaces.client.TupleSpace;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.feb.2011
 * Time: 12:28:23
 * To change this template use File | Settings | File Templates.
 */
public class SingleStudentStatusController extends BaseController {


    private UserService userService;
    private TupleSpace tupleSpace;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String userName = request.getParameter("user");
        String missionURI = request.getParameter("missionURI");

        User user = getUserService().getUser(userName);
        modelAndView.addObject("user", user);
        try {
            modelAndView.addObject("missionURI", URLEncoder.encode(missionURI, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        logger.info("USER: " + userName + " MISSION: " + missionURI);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public TupleSpace getTupleSpace() {
        return tupleSpace;
    }

    public void setTupleSpace(TupleSpace tupleSpace) {
        this.tupleSpace = tupleSpace;
    }
}
