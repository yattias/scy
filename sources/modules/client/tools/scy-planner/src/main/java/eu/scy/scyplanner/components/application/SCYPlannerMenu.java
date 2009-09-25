package eu.scy.scyplanner.components.application;

import eu.scy.scyplanner.action.demo.OpenSCYPlannerDemo;
import eu.scy.scyplanner.application.SCYPlannerActionManager;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 24.sep.2009
 * Time: 13:05:09
 */
public class SCYPlannerMenu extends JMenuBar {
    public SCYPlannerMenu() {
        super();

        JMenu menu = new JMenu("Menu");
        addItemToMenu(menu, SCYPlannerActionManager.getActionManager().getAction(SCYPlannerActionManager.OPEN_CREATE_NEW_PEDAGOGICAL_PLAN_PANEL_ACTION));
        addItemToMenu(menu, SCYPlannerActionManager.getActionManager().getAction(SCYPlannerActionManager.OPEN_SCY_PLANNER_DEMO_ACTION));

        add(menu);
    }

    private void addItemToMenu(JMenu menu, Action action) {
        JMenuItem item = new JMenuItem(action);
        menu.add(item);
    }
}