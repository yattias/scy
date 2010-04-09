package eu.scy.scyplanner.components.application;

import eu.scy.scyplanner.application.SCYPlannerActionManager;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 25.sep.2009
 * Time: 00:27:35
 */
public class SCYPlannerToolBar extends JToolBar {
    public SCYPlannerToolBar() {
        add(SCYPlannerActionManager.getActionManager().getAction(SCYPlannerActionManager.OPEN_CREATE_NEW_PEDAGOGICAL_PLAN_PANEL_ACTION));
        addSeparator();
        add(SCYPlannerActionManager.getActionManager().getAction(SCYPlannerActionManager.OPEN_SCY_PLANNER_DEMO_ACTION));
    }
}
