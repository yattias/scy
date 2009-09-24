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
public class CloseSCYPlannerDemo extends AbstractSCYPlannerAction {

    public CloseSCYPlannerDemo() {
        super("Close SCYPlanner demo", null);
    }

    @Override
    protected void doActionPerformed(ActionEvent actionEvent) {
        ApplicationManager.getApplicationManager().getScyPlannerFrame().setDefaultScreen();
    }
}