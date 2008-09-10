package eu.scy.modules.useradmin.components.border;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.aug.2008
 * Time: 13:01:15
 * To change this template use File | Settings | File Templates.
 */

import eu.scy.modules.useradmin.pages.SCYBasePage;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.Asset;

public class Layout extends SCYBasePage {


    public String getCurrentProjectName() {
        if(getCurrentProject() == null) return "No project set";
        else return getCurrentProject().getName();
    }

    @Inject
    @Path("context:graphics/scy_home.png")
    private Asset scyHomeIcon;

}
