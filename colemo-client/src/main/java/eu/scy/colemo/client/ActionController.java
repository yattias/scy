package eu.scy.colemo.client;

import eu.scy.colemo.client.actions.*;

import javax.swing.*;
import java.util.List;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 19.nov.2008
 * Time: 05:56:59
 * To change this template use File | Settings | File Templates.
 */
public class ActionController {
    private Logger log = Logger.getLogger("ActionController.class");

    public static ActionController defaultInstance;

    private List actions = new LinkedList();

    public static ActionController getDefaultInstance() {
        if(defaultInstance == null) defaultInstance= new ActionController();
        return defaultInstance;
    }

    private ActionController() {
        registerAction(new eu.scy.colemo.client.actions.DeleteConcept(), "Delete");
        registerAction(new eu.scy.colemo.client.actions.SetConceptProperties(), "Set Properties");
    }

    private void registerAction(BaseAction action, String title) {
        action.setTitle(title);
        actions.add(action);
    }

    public List<Action> getActions(Object userObject) {
        log.info("Getting actions for: " + userObject.getClass().getName());
        List returnList = new LinkedList();
        for (int i = 0; i < actions.size(); i++) {
            BaseAction baseAction = (eu.scy.colemo.client.actions.BaseAction) actions.get(i);
            if(baseAction.getOperateson().equals(userObject.getClass())) returnList.add(baseAction);
        }
        
        return returnList;
    }
}
