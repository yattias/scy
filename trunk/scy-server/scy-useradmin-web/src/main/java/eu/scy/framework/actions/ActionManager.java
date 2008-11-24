package eu.scy.framework.actions;

import eu.scy.framework.BaseAction;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.nov.2008
 * Time: 21:22:58
 * To change this template use File | Settings | File Templates.
 */
public interface ActionManager {
    public List<BaseAction> getActions(Object userObject);

    public BaseAction getActionById(String id);
}
