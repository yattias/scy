package eu.scy.scyplanner.action.demo;

import eu.scy.scyplanner.action.AbstractSCYPlannerAction;
import eu.scy.scyplanner.application.SCYPlannerApplicationManager;
import eu.scy.scyplanner.components.demo.SCYPlannerDemo;

import java.awt.event.ActionEvent;

import javax.swing.Action;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 23.sep.2009
 * Time: 23:25:55
 */
public class OpenSCYPlannerDemo extends AbstractSCYPlannerAction {
    public OpenSCYPlannerDemo() {
        super("Open SCYPlanner demo", null);
        putValue(Action.SHORT_DESCRIPTION, "Dummy-data that demonstrates how the SCYPlanner might work in the future");
    }

    @Override
    protected void doActionPerformed(ActionEvent actionEvent) {        
        SCYPlannerApplicationManager.getApplicationManager().getScyPlannerFrame().setContent("SCYPlanner demo", new SCYPlannerDemo());
    }
}
