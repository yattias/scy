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

    public int doEndTag() throws JspException {
        double id = Math.random() ;
        try {
               pageContext.getOut().write("<span id=\"activityStatus" + id + "\"><i>Offline</i></span>");
               pageContext.getOut().write("<script type=\"text/javascript\" language=\"javascript\">" +
                       "setInterval(\"updateActivityStatus('activityStatus" + id + "');\", 5000);" +
                       "</script>");
           } catch (Exception e) {
               e.printStackTrace();
           }
           return EVAL_PAGE;
       }


}
