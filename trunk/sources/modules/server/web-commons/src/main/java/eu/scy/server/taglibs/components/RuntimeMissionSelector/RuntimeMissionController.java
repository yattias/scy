package eu.scy.server.taglibs.components.runtimemissionselector;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.roolo.RuntimeELOService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.des.2010
 * Time: 07:00:13
 * To change this template use File | Settings | File Templates.
 */
public class RuntimeMissionController extends TagSupport {

    private RuntimeELOService runtimeELOService;
    private UserService userService;


    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().write("<div id=\"runtimeMissionController\">\n");
            List runtimeELOs = getRuntimeELOService().getRuntimeElosForUser(getCurrentUserName((HttpServletRequest) pageContext.getRequest()));
            for (int i = 0; i < runtimeELOs.size(); i++) {
                MissionRuntimeElo missionRuntimeElo = new MissionRuntimeElo (((ScyElo) runtimeELOs.get(i)).getElo(), runtimeELOService);
                if(missionRuntimeElo.getMissionRunning().equals(getCurrentUserName((HttpServletRequest) pageContext.getRequest()))) {
                    String uri = missionRuntimeElo.getUri().toString();
                    uri = URLEncoder.encode(uri, "UTF-8");
                    pageContext.getOut().write("<a href=\"/webapp/components/runtimemissionselector/RuntimeMissionSelector.html?eloURI=" + uri + "\">");
                    pageContext.getOut().write(missionRuntimeElo.getTitle());
                    pageContext.getOut().write("</a>");
                }

            }
            //pageContext.getOut().write("RUNTIME MISSION CONTROLLER");
            pageContext.getOut().write("</div>");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return EVAL_PAGE;
    }

    public RuntimeELOService getRuntimeELOService() {
        return runtimeELOService;
    }

    public void setRuntimeELOService(RuntimeELOService runtimeELOService) {
        this.runtimeELOService = runtimeELOService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public User getCurrentUser(HttpServletRequest request) {
       return getUserService().getUser(getCurrentUserName(request));
   }

    public String getCurrentUserName(HttpServletRequest request) {
      org.springframework.security.userdetails.User user = (org.springframework.security.userdetails.User) request.getSession().getAttribute("CURRENT_USER");
      return user.getUsername();
  }

}
