package eu.scy.scyplanner.components.application;

import eu.scy.scyplanner.components.frontpage.StartupMenuItem;
import eu.scy.scyplanner.components.titled.TitledPanel;
import eu.scy.scyplanner.application.SCYPlannerActionManager;
import eu.scy.scyplanner.application.SCYPlannerApplicationManager;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 25.sep.2009
 * Time: 10:59:19
 */
public class WelcomePanel extends JPanel {
    public WelcomePanel() {
        TitledPanel panel = new TitledPanel("Actions", new GridLayout(0, 1));
        panel.setBackground(Color.WHITE);

        panel.add(new StartupMenuItem(SCYPlannerActionManager.getActionManager().getAction(SCYPlannerActionManager.OPEN_CREATE_NEW_PEDAGOGICAL_PLAN_PANEL_ACTION), null));
        panel.add(new StartupMenuItem(SCYPlannerActionManager.getActionManager().getAction(SCYPlannerActionManager.OPEN_SCY_PLANNER_DEMO_ACTION), null));

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        add(BorderLayout.NORTH, SCYPlannerApplicationManager.getApplicationManager().createDefaultBorderForTitledPanel(panel, Color.WHITE));
    }
}
