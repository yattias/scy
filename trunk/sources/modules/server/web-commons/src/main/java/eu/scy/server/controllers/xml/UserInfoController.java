package eu.scy.server.controllers.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.okt.2010
 * Time: 15:37:20
 * To change this template use File | Settings | File Templates.
 */
public class UserInfoController extends XMLStreamerController {

    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        return getCurrentUser(request);
    }

}
