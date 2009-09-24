package eu.scy.scyplanner.components.application;

import eu.scy.scyplanner.action.demo.OpenSCYPlannerDemo;
import eu.scy.scyplanner.action.demo.CloseSCYPlannerDemo;

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
        addItemToMenu(menu, new OpenSCYPlannerDemo());
        addItemToMenu(menu, new CloseSCYPlannerDemo());

        add(menu);
    }

    private void addItemToMenu(JMenu menu, Action action) {
        JMenuItem item = new JMenuItem(action);
        menu.add(item);
    }
}