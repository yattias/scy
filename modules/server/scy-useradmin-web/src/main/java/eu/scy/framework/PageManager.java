package eu.scy.framework;

import eu.scy.webapp.pages.ScyModelPage;
import eu.scy.webapp.pages.GroupOverview;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.nov.2008
 * Time: 13:29:32
 * To change this template use File | Settings | File Templates.
 */
public interface PageManager {


    public String getPageIdForObject(Object object);

    GroupOverview getGroupOverview();
}
