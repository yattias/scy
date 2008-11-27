package eu.scy.webapp.components.colemo;

import eu.scy.webapp.pages.TapestryContextAware;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.nov.2008
 * Time: 06:16:33
 * To change this template use File | Settings | File Templates.
 */
public class Colemo extends TapestryContextAware {

    public String getUsername() {
        return "henrik@enovate.no";//getCurrentUser().getUserName();
    }

    public String getPassword() {
        return "heehaa";
        //return getCurrentUser().getPassword();
    }

    
}
