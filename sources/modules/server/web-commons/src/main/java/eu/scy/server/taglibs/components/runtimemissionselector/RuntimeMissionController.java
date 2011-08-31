package eu.scy.server.taglibs.components.runtimemissionselector;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.roolo.RuntimeELOService;
import roolo.search.ISearchResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

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
            String user = getCurrentUserName((HttpServletRequest) pageContext.getRequest());
            List <ISearchResult> runtimeELOs = getRuntimeELOService().getRuntimeElosForUser(user);
            Locale locale = new Locale("no");
            if (runtimeELOs.size() > 0) {
                pageContext.getOut().write("<table width=\"100%\">");
                for (int i = 0; i < runtimeELOs.size(); i++) {
                    ISearchResult runtimeSearchResult = runtimeELOs.get(i);
                    //MissionRuntimeElo missionRuntimeElo = new MissionRuntimeElo(((ScyElo) runtimeELOs.get(i)).getElo(), runtimeELOService);
                    //if (missionRuntimeElo.getUserRunningMission().equals(user)) {
                        //String uri = missionRuntimeElo.getUri().toString();
                        //uri = URLEncoder.encode(uri, "UTF-8");
                        //List<Locale> languages = missionRuntimeElo.getContent().getLanguages();
                        //Locale locale = null;
                        //if (!languages.isEmpty()) {
                        //    locale = languages.get(0);
                        //}
                        pageContext.getOut().write("<tr><td>");
                        pageContext.getOut().write("<a href=\"/webapp/app/student/StudentIndex.html?eloURI=" + URLEncoder.encode(runtimeSearchResult.getUri().toString(), "UTF-8") + "\">");

                        pageContext.getOut().write("BOOOO");//runtimeSearchResult.getTitle(locale));
                        pageContext.getOut().write("</a>");
                        pageContext.getOut().write("</td><td>");
                        pageContext.getOut().write("<a href=\"scy-lab.jnlp?username=" + user + "&mission=" + runtimeSearchResult.getUri().toString());
                        if (locale != null) {
                            pageContext.getOut().write("&locale=" + locale.getLanguage());
                        }
                        pageContext.getOut().write("\">Start SCYLab</a></td></tr>");
                    //}

                }
                pageContext.getOut().write("</table>");
                //pageContext.getOut().write("RUNTIME MISSION CONTROLLER");
            } else {
                pageContext.getOut().write("<strong>You have no assigned missions. Please contact your teacher and ask him or her to assign a mission to your user</strong>");
            }

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
