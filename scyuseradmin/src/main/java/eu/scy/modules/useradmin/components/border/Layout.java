package eu.scy.modules.useradmin.components.border;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.aug.2008
 * Time: 13:01:15
 * To change this template use File | Settings | File Templates.
 */

import eu.scy.modules.useradmin.pages.SCYBasePage;
import eu.scy.core.model.Project;

public class Layout extends SCYBasePage {


    public String getCurrentProjectName() {
        if(getCurrentProject() == null) return "No project set";
        else return getCurrentProject().getName();
    }
}
