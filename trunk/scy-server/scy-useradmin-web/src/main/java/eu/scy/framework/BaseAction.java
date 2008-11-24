package eu.scy.framework;

import eu.scy.framework.actions.ActionManager;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.nov.2008
 * Time: 21:18:18
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseAction {

    protected static Logger log = Logger.getLogger("BaseACtion.class");

    private String name;
    private ActionManager actionManager;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract Class getOperatesOn() ;

    public Object actionPerformed(Object model) {
        return doAction(model);
    }

    protected abstract Object doAction(Object model) ;

    public String getActionId() {
        return getClass().getName();
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public void setActionManager(ActionManager actionManager) {
        this.actionManager = actionManager;
    }
}

