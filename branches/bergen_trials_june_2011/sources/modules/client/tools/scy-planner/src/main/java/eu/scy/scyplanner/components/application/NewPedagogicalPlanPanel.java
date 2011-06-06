package eu.scy.scyplanner.components.application;

import eu.scy.scyplanner.application.Strings;
import eu.scy.scyplanner.components.titled.TitledList;
import eu.scy.scyplanner.components.titled.TitledPanel;
import eu.scy.scyplanner.application.SCYPlannerApplicationManager;
import eu.scy.core.model.pedagogicalplan.*;
import eu.scy.core.model.impl.pedagogicalplan.MissionImpl;
import eu.scy.core.model.impl.pedagogicalplan.ScenarioImpl;
import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceImpl;
import eu.scy.core.model.impl.pedagogicalplan.LearningGoalImpl;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 25.sep.2009
 * Time: 10:01:04
 */
public class NewPedagogicalPlanPanel extends JPanel {
    public NewPedagogicalPlanPanel() {
        super(new BorderLayout());

        TitledList missionList = new TitledList(Strings.getString("Available missions"), createMissionListModel(), true, BorderFactory.createEmptyBorder(0, 0, SCYPlannerApplicationManager.getDefaultBorderSize(), 0));
        TitledList scenarioList = new TitledList(Strings.getString("Available scenarios"), createScenarioListModel(), true);
        add(createMissionScenarioPanel(missionList, scenarioList), BorderLayout.WEST);

        add(SCYPlannerApplicationManager.getApplicationManager().createDefaultBorderForTitledPanel(new TitledPanel("Default pedagogical plan", new BorderLayout(), new DefaultPedagogicalPlanInformationPanel(missionList.getList(), scenarioList.getList()), BorderLayout.CENTER)));
    }

    private JPanel createMissionScenarioPanel(TitledList missionList, TitledList scenarioList) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setPreferredSize(new Dimension(250, 100));
        panel.setBorder(SCYPlannerApplicationManager.getApplicationManager().createDefaultBorder());

        panel.add(missionList);
        panel.add(scenarioList);

        return panel;
    }

    private DefaultListModel createMissionListModel() {
        DefaultListModel model = new DefaultListModel();

        List <Mission> missions = SCYPlannerApplicationManager.getApplicationManager().getPedagogicalPlanService().getMissions();
        for (int i = 0; i < missions.size(); i++) {
            Mission mission = missions.get(i);
            model.addElement(mission);
        }

        return model;
    }

    private DefaultListModel createScenarioListModel() {
        DefaultListModel model = new DefaultListModel();
        List <Scenario> scenarios = SCYPlannerApplicationManager.getApplicationManager().getPedagogicalPlanService().getScenarios();
        for (Scenario scenario: scenarios) {
            model.addElement(scenario);
        }
        return model;
    }


}
