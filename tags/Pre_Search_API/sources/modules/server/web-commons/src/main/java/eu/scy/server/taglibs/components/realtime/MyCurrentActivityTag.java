package eu.scy.server.taglibs.components.realtime;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.jun.2010
 * Time: 08:40:36
 * To change this template use File | Settings | File Templates.
 */
public class MyCurrentActivityTag extends TagSupport {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int doEndTag() throws JspException {
        double id = Math.random();
        try {
            //pageContext.getOut().write("<h1>Current activity " + getUsername() + "</h1>");
            pageContext.getOut().write("<div id=\"currentActivityContainer\"></div>");
            pageContext.getOut().write("<script type=\"text/javascript\">" +
                    "setInterval('loadPage(\"/webapp/components/realtime/myCurrentActivity.html?username="+ getUsername() + "\", \"currentActivityContainer\")', 2000);" +
                   

                    "</script>");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }


}
