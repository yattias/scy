package eu.scy.server.taglibs.components.runtimemissionselector;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.model.transfer.SearchResultTransfer;
import eu.scy.core.roolo.RuntimeELOService;
import roolo.search.ISearchResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.UnsupportedEncodingException;
import java.net.URI;
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
            List<SearchResultTransfer> runtimeELOs = getRuntimeELOService().getSearchResultTransfers(getRuntimeELOService().getRuntimeElosForUser(user), pageContext.getRequest().getLocale());

            if (runtimeELOs.size() > 0) {
                pageContext.getOut().write("<table width=\"100%\">");
                for (int i = 0; i < runtimeELOs.size(); i++) {
                    SearchResultTransfer runtimeSearchResult = runtimeELOs.get(i);
                    URI uri = new URI(runtimeSearchResult.getUri());

                    String locale = "en";

                    if (runtimeSearchResult.getLocale() != null) {
                        locale = runtimeSearchResult.getLocale().getLanguage();
                        System.out.println("RUNTIME LANGUAGE: " + locale);
                    }


                    pageContext.getOut().write("<tr><td>");
                    pageContext.getOut().write("<a href=\"/webapp/app/student/StudentIndex.html?eloURI=" + URLEncoder.encode(runtimeSearchResult.getUri().toString(), "UTF-8") + "\">");
                    System.out.println("SEARCH RESULT TITLE: " + runtimeSearchResult.getTitle());
                    pageContext.getOut().write(runtimeSearchResult.getTitle());

                    pageContext.getOut().write("</a>");
                    pageContext.getOut().write("</td></tr>");

                }
                pageContext.getOut().write("</table>");
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
