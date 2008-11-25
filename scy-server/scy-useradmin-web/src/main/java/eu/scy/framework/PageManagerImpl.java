package eu.scy.framework;

import eu.scy.core.model.Group;
import eu.scy.core.model.ScyBase;
import eu.scy.core.model.impl.GroupImpl;
import eu.scy.webapp.pages.GroupOverview;
import eu.scy.webapp.pages.ScyModelPage;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.internal.services.PagePool;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.nov.2008
 * Time: 13:30:02
 * To change this template use File | Settings | File Templates.
 */
public class PageManagerImpl implements PageManager {

    private Map map = new HashMap();

    @InjectPage
    private GroupOverview groupOverview;



    public GroupOverview getGroupOverview() {
        return groupOverview;
    }

    public void setGroupOverview(GroupOverview groupOverview) {
        this.groupOverview = groupOverview;
    }

    public PageManagerImpl() {
        map.put(Group.class, "GroupOverview");
        map.put(GroupImpl.class, "GroupOverview");



    }

    public String getPageIdForObject(Object object) {
        return (String) map.get(object.getClass());
    }
}
