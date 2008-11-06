package eu.scy.components.assertroles;

import eu.scy.pages.TapestryContextAware;
import eu.scy.core.model.User;
import eu.scy.core.model.UserRole;
import eu.scy.core.model.Role;

import java.util.List;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.apache.tapestry5.annotations.Parameter;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 29.okt.2008
 * Time: 06:17:27
 * To change this template use File | Settings | File Templates.
 */
public class AssertRoles extends TapestryContextAware {

    private static Logger log = Logger.getLogger("AssertRoles.class");

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

        User user = getCurrentUser();
        List linkedList = new LinkedList();
        List userRoles = user.getUserRoles();
        for (int i = 0; i < userRoles.size(); i++) {
            UserRole userRole = (UserRole) userRoles.get(i);
            if(!linkedList.contains(userRole.getRole())) linkedList.add(userRole.getRole());
        }

        String legalRoles [] = roles.split(",");
        for (int i = 0; i < legalRoles.length; i++) {
            String legalRole = legalRoles[i];
            for (int j = 0; j < linkedList.size(); j++) {
                Role role = (Role) linkedList.get(j);
                log.info("comparing " + role.getName() + " and " + legalRole);
                if(role.getName().equals(legalRole)) {
                    log.info("Found it!");
                    return true;
                }
            }
        }
        return false;
    }
}
