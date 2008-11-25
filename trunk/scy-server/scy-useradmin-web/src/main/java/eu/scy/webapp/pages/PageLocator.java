package eu.scy.webapp.pages;

import org.apache.tapestry5.annotations.InjectPage;
import eu.scy.core.model.ScyBase;
import eu.scy.core.model.impl.ScyBaseObject;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.nov.2008
 * Time: 15:05:53
 * To change this template use File | Settings | File Templates.
 */
public class PageLocator extends TapestryContextAware{

    @InjectPage
    private GroupOverview groupOverview;


    public ScyModelPage getPageForObject(Object object) {
        log.info("ACTIVATING WITH OBJECT: " + object + " AND ID: " + ((ScyBaseObject)object).getId());
        groupOverview.setModel((ScyBase) object);
        groupOverview.setModelId(((ScyBaseObject)object).getId());
        return groupOverview;
    }

}
