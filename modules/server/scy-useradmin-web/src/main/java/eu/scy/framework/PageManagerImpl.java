package eu.scy.framework;

import eu.scy.core.model.impl.SCYGroupImpl;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.impl.SCYProjectImpl;
import eu.scy.webapp.pages.GroupOverview;

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
        map.put(SCYGroupImpl.class, "GroupOverview");
        map.put(SCYUserImpl.class, "EditUserPage");
        map.put(SCYProjectImpl.class, "Index");


    }

    public String getPageIdForObject(Object object) {
        return (String) map.get(object.getClass());
    }
}
