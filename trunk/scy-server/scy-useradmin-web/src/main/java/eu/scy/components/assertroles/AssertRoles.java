package eu.scy.components.assertroles;

import eu.scy.pages.TapestryContextAware;

import java.util.List;
import java.util.LinkedList;

import org.apache.tapestry5.annotations.Parameter;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 29.okt.2008
 * Time: 06:17:27
 * To change this template use File | Settings | File Templates.
 */
public class AssertRoles extends TapestryContextAware {

    @Parameter
    private String roles;

    private List legalRoles = new LinkedList();

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Boolean getRenderBody() {
        String legalRoles [] = roles.split(",");
        for (int i = 0; i < legalRoles.length; i++) {
            String legalRole = legalRoles[i];
            if(getUserDAOHibernate().getIsUserInRole(legalRole,  getCurrentUser())) {
                return true;
            }
        }

        return false;
    }
}
