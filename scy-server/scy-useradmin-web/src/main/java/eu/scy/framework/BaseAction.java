package eu.scy.framework;

import eu.scy.framework.ActionManager;
import eu.scy.core.model.Project;


import java.util.logging.Logger;

import net.sf.sail.webapp.domain.User;

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

    private Project project;
    private User user;
    private Object userObject;

    public Object getUserObject() {
        return userObject;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

