package eu.scy.modules.useradmin.components.links;

import eu.scy.modules.useradmin.pages.SCYBasePage;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.sep.2008
 * Time: 12:37:23
 * To change this template use File | Settings | File Templates.
 */
public class ActionLink extends SCYBasePage {

    public String getLink() {
        if(getCurrentProject() == null) {
            return "CANNOT SEE ME";
        }
        return "see me";
    }

}
