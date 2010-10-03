package eu.scy.scyplanner.application;

import eu.scy.scyplanner.action.create.OpenCreateNewPedagogicalPlanAction;

import javax.swing.*;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 25.sep.2009
 * Time: 10:39:22
 */
public class SCYPlannerActionManager {
    public final static String OPEN_SCY_PLANNER_DEMO_ACTION = "OPEN_SCY_PLANNER_DEMO_ACTION";
    public final static String OPEN_CREATE_NEW_PEDAGOGICAL_PLAN_PANEL_ACTION = "OPEN_CREATE_NEW_PEDAGOGICAL_PLAN_PANEL_ACTION";

    private static SCYPlannerActionManager actionManager = null;
    private final static HashMap actionMap = new HashMap<Action, String>();

    private SCYPlannerActionManager() {
        actionMap.put(OPEN_CREATE_NEW_PEDAGOGICAL_PLAN_PANEL_ACTION, new OpenCreateNewPedagogicalPlanAction());
    }

    public static SCYPlannerActionManager getActionManager() {
        if (actionManager == null) {
            actionManager = new SCYPlannerActionManager();
        }
        return actionManager;
    }

    public Action getAction(String actionId) {
        return (Action) actionMap.get(actionId);
    }
}
