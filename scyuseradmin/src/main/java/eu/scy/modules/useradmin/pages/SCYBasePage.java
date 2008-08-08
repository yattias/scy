package eu.scy.modules.useradmin.pages;

import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.aug.2008
 * Time: 14:27:08
 * To change this template use File | Settings | File Templates.
 */
public class SCYBasePage {

    //protected Logger log = Logger.getLogger(getClass());

    public String getCurrentUsersUserName() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

}
