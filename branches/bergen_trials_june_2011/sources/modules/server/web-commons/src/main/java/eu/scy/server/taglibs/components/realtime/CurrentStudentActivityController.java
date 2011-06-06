package eu.scy.server.taglibs.components.realtime;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 30.mar.2010
 * Time: 08:33:42
 * To change this template use File | Settings | File Templates.
 */
public class CurrentStudentActivityController extends TagSupport {

    private String username;
    private String missionURI;

    public int doEndTag() throws JspException {
        double id = Math.random() ;
        try {

            pageContext.getRequest().getParameter("username");

               pageContext.getOut().write("<span id=\"activityStatus" + id + "\"><i>Initializing...</i></span>");
               pageContext.getOut().write("<script type=\"text/javascript\" language=\"javascript\">" +
                       "setInterval(\"updateActivityStatus('activityStatus" + id + "', '" + getUsername() + "', '" + getMissionURI() + "');\", 5000);" +
                       "</script>");
           } catch (Exception e) {
               e.printStackTrace();
           }
           return EVAL_PAGE;
       }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMissionURI() {
        return missionURI;
    }

    public void setMissionURI(String missionURI) {
        this.missionURI = missionURI;
    }
}
