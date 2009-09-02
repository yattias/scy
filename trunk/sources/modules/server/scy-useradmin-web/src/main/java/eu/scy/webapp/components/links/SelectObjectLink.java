package eu.scy.webapp.components.links;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.annotations.Parameter;
import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.core.model.ScyBase;
import eu.scy.framework.BaseAction;
import eu.scy.framework.ActionManager;
import eu.scy.webapp.pages.ScyModelPage;
import eu.scy.webapp.pages.TapestryContextAware;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 25.nov.2008
 * Time: 10:16:35
 * To change this template use File | Settings | File Templates.
 */
public class SelectObjectLink extends TapestryContextAware {


    @Inject
    private ComponentSource compSource;

    @Parameter
    private ScyBaseObject model;

    public ScyBaseObject getModel() {
        return model;
    }

    public void setModel(ScyBaseObject model) {
        this.model = model;
    }

    public Object onActionFromLink(ScyBaseObject scyBaseObject) {
        String pageId = getPageManager().getPageIdForObject(scyBaseObject);
        ScyModelPage comp = (ScyModelPage) compSource.getPage(pageId);
        comp.setModel((ScyBaseObject) scyBaseObject);
        return comp;
    }


}
