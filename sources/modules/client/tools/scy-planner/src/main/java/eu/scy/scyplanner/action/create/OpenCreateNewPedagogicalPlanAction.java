package eu.scy.scyplanner.action.create;

import eu.scy.scyplanner.action.AbstractSCYPlannerAction;
import eu.scy.scyplanner.application.SCYPlannerApplicationManager;
import eu.scy.scyplanner.components.application.NewPedagogicalPlanPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 25.sep.2009
 * Time: 10:38:09
 */
public class OpenCreateNewPedagogicalPlanAction extends AbstractSCYPlannerAction {
    public OpenCreateNewPedagogicalPlanAction() {
        super("Create New Pedagogical Plan", null, "Select mission and scenario to compose a new default pedagogical plan to be fine-tuned by you");        
    }

    @Override
    protected void doActionPerformed(ActionEvent actionEvent) {
        SCYPlannerApplicationManager.getApplicationManager().getScyPlannerFrame().setContent("Pedagogical Plan", new NewPedagogicalPlanPanel());
    }
}
