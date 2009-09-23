package eu.scy.scyplanner.action.demo;

import eu.scy.scyplanner.action.AbstractSCYPlannerAction;
import eu.scy.scyplanner.application.ApplicationManager;
import eu.scy.scyplanner.components.demo.SCYPlannerDemo;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 23.sep.2009
 * Time: 23:25:55
 */
public class OpenSCYPlannerDemo extends AbstractSCYPlannerAction {
    private SCYPlannerDemo demo = null;

    public OpenSCYPlannerDemo() {
        super("Open SCYPlanner demo", null);
    }

    @Override
    protected void doActionPerformed(ActionEvent actionEvent) {
        if (demo == null) {
            demo = new SCYPlannerDemo();
        }
        ApplicationManager.getApplicationManager().getScyPlannerFrame().setContent(demo);
    }
}
