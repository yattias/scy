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
public class CurrentStudentActivity extends TagSupport {

    public int doEndTag() throws JspException {
           try {
               pageContext.getOut().write("<i>Offline</i>");
           } catch (Exception e) {
               e.printStackTrace();
           }
           return EVAL_PAGE;
       }


}
