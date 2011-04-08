package eu.scy.server.taglibs.editors;

import eu.scy.core.model.SCYGrantedAuthority;
import eu.scy.core.model.impl.SCYUserImpl;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.aug.2010
 * Time: 06:01:28
 * To change this template use File | Settings | File Templates.
 */
public class EditUserRoles extends TagSupport {

    private SCYUserImpl scyUser;
    private List availableAuthorities;
    private String successView;

    public int doEndTag() throws JspException {

        scyUser.getUserDetails().getAuthorities();


        try {
            pageContext.getOut().write("<table>");
            for (int i = 0; i < availableAuthorities.size(); i++) {
                SCYGrantedAuthority o = (SCYGrantedAuthority) availableAuthorities.get(i);
                pageContext.getOut().write("<tr><td width=\"5%\">");
                pageContext.getOut().write("<a href=\"/webapp/components/roleeditor/RoleEditorContoller.html?username=" + getUser().getUserDetails().getUsername() +"&role=" + o.getAuthority() +"&successView=" + getSuccessView() + "\">x</a>");
                pageContext.getOut().write("</td><td>");
                if(userHasRole(o)) pageContext.getOut().write("<strong>" +  o.getAuthority() + "</strong>");
                else pageContext.getOut().write(o.getAuthority());

                pageContext.getOut().write("</td></tr>");
            }
            pageContext.getOut().write("</table>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    private boolean userHasRole(SCYGrantedAuthority grantedAuthority) {
        return getUser().getUserDetails().hasGrantedAuthority(grantedAuthority.getAuthority());
    }

    public SCYUserImpl getUser() {
        return scyUser;
    }

    public void setUser(SCYUserImpl scyUser) {
        this.scyUser = scyUser;
    }

    public List getAvailableAuthorities() {
        return availableAuthorities;
    }

    public void setAvailableAuthorities(List availableAuthorities) {
        this.availableAuthorities = availableAuthorities;
    }

    public String getSuccessView() {
        return successView;
    }

    public void setSuccessView(String successView) {
        this.successView = successView;
    }
}
