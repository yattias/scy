package eu.scy.scyplanner.components.application;

import eu.scy.scyplanner.application.Strings;
import eu.scy.scyplanner.components.frontpage.StartUpMenuItem;
import eu.scy.scyplanner.components.frontpage.StartUpInformationItem;
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
        JPanel panel = getPanel(new BorderLayout());

        JPanel infoPanel = getPanel(new GridLayout(0, 1));        
        infoPanel.add(new StartUpInformationItem(Strings.getString("Welcome to SCYPlanner - the teacher's best friend"), Strings.getString("Click on any action to continue"), null));
        panel.add(BorderLayout.NORTH, infoPanel);

        TitledPanel actionPanel = new TitledPanel(Strings.getString("Actions"), new GridLayout(0, 1));
        actionPanel.setBackground(SCYPlannerApplicationManager.getAlternativeBackgroundColor());
        actionPanel.add(new StartUpMenuItem(SCYPlannerActionManager.getActionManager().getAction(SCYPlannerActionManager.OPEN_CREATE_NEW_PEDAGOGICAL_PLAN_PANEL_ACTION), null));
        //actionPanel.add(new StartUpMenuItem(SCYPlannerActionManager.getActionManager().getAction(SCYPlannerActionManager.OPEN_SCY_PLANNER_DEMO_ACTION), null));
        panel.add(BorderLayout.CENTER, actionPanel);

        setLayout(new BorderLayout());
        setBackground(SCYPlannerApplicationManager.getAlternativeBackgroundColor());
        panel.setBorder(SCYPlannerApplicationManager.getApplicationManager().createDefaultBorder());
        add(BorderLayout.NORTH, panel);
    }

    private JPanel getPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(SCYPlannerApplicationManager.getAlternativeBackgroundColor());

        return panel;
    }
}
