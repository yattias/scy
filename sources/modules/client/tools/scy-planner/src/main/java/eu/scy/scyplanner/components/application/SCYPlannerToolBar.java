package eu.scy.scyplanner.components.application;

import eu.scy.scyplanner.action.demo.OpenSCYPlannerDemo;
import eu.scy.scyplanner.action.demo.CloseSCYPlannerDemo;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 25.sep.2009
 * Time: 00:27:35
 */
public class SCYPlannerToolBar extends JToolBar {
    public SCYPlannerToolBar() {
        add(new OpenSCYPlannerDemo());
        add(new CloseSCYPlannerDemo());
    }
}
