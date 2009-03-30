package eu.scy.colemo.client;

import eu.scy.colemo.client.actions.BaseAction;
import eu.scy.colemo.client.actions.SetConceptProperties;
import eu.scy.colemo.client.actions.DeleteConcept;
import eu.scy.colemo.client.actions.FlipLink;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 30.mar.2009
 * Time: 06:59:19
 * To change this template use File | Settings | File Templates.
 */
public class ActionController {

    private static ActionController deaultInstance = null;

    private List actions = new ArrayList();

    private ActionController() {
        SetConceptProperties setConceptProperties = new SetConceptProperties("Properties");
        add(setConceptProperties);
        DeleteConcept deleteConcept = new DeleteConcept("Delete");
        add(deleteConcept);
        add(new FlipLink());
    }

    public static ActionController getDefaultInstance() {
        if(deaultInstance == null) deaultInstance = new ActionController();
        return deaultInstance;
    }

    public void add(BaseAction action) {
        if(!actions.contains(action)) actions.add(action);
    }

    public List getActions(Object model) {
        System.out.println("getting actions for " + model);
        List returnList = new LinkedList();
        for (int i = 0; i < actions.size(); i++) {
            BaseAction baseAction = (BaseAction) actions.get(i);
            if(baseAction.getOperateson().equals(model.getClass())) returnList.add(baseAction);
        }

        return returnList;
    }



}
