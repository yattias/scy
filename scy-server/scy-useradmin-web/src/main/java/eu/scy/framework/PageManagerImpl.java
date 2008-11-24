package eu.scy.framework;

import eu.scy.core.model.Group;
import eu.scy.core.model.impl.GroupImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.nov.2008
 * Time: 13:30:02
 * To change this template use File | Settings | File Templates.
 */
public class PageManagerImpl implements PageManager {

    private Map map = new HashMap();


    public PageManagerImpl() {
        map.put(Group.class, "GroupOverview");
        map.put(GroupImpl.class, "GroupOverview");



    }

    public String getPageIdForObject(Object object) {
        String pageId = (String) map.get(object.getClass());
        if(pageId == null) throw new NullPointerException("No registered page for class " + object.getClass());
        return pageId;

    }
}
