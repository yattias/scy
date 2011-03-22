package eu.scy.server.controllers.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceImpl;
import eu.scy.core.model.impl.pedagogicalplan.PedagogicalPlanImpl;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 06.okt.2010
 * Time: 05:18:06
 * To change this template use File | Settings | File Templates.
 */
public abstract class XMLStreamerController extends AbstractController {
    protected UserService userService;

    protected int getXStreamMode() {
        return XStream.XPATH_RELATIVE_REFERENCES;
    }

    protected void addAliases(XStream xStream) {
        xStream.alias("pedagogicalPlan", PedagogicalPlan.class);
        xStream.alias("pedagogicalPlan", PedagogicalPlanImpl.class);
        xStream.alias("las", LearningActivitySpaceImpl.class);
        xStream.alias("las", LearningActivitySpace.class);
    }

    protected void omitFields(XStream xStream ) {
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        XStream xstream = new XStream(new DomDriver());
        httpServletResponse.setContentType("text/xml");
        xstream.setMode(getXStreamMode());
        omitFields(xstream);
        addAliases(xstream);

        Object objectToStream = getObjectToStream(request, httpServletResponse);
        xstream.toXML(objectToStream, httpServletResponse.getWriter());

        return null;
    }

    protected abstract Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse);

    public User getCurrentUser(HttpServletRequest request) {
       return getUserService().getUser(getCurrentUserName(request));
   }

    public String getCurrentUserName(HttpServletRequest request) {
      org.springframework.security.userdetails.User user = (org.springframework.security.userdetails.User) request.getSession().getAttribute("CURRENT_USER");
      return user.getUsername();
  }

    public UserService getUserService() {
       return userService;
   }

    public void setUserService(UserService userService) {
       this.userService = userService;
   }
}