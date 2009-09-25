package eu.scy.scyplanner.components.application;

import eu.scy.scyplanner.components.titled.TitledList;
import eu.scy.scyplanner.components.titled.TitledPanel;
import eu.scy.scyplanner.application.SCYPlannerApplicationManager;
import eu.scy.core.model.pedagogicalplan.Mission;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.core.model.impl.pedagogicalplan.MissionImpl;
import eu.scy.core.model.impl.pedagogicalplan.ScenarioImpl;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 25.sep.2009
 * Time: 10:01:04
 */
public class NewPedagogicalPlanPanel extends JPanel {
    public NewPedagogicalPlanPanel() {
        super(new BorderLayout());

        TitledList missionList = new TitledList("Available missions", createMissionListModel(), BorderFactory.createEmptyBorder(0, 0, SCYPlannerApplicationManager.getDefaultBorderSize(), 0));
        TitledList scenarioList = new TitledList("Available scenarios", createScenarioListModel());
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

        model.addElement(createMission("Mission 1", "Building a CO2 neutral house"));
        model.addElement(createMission("Mission 2", "Building a CO2 neutral bike"));
        model.addElement(createMission("Mission 3", "Building a CO2 neutral car"));
        model.addElement(createMission("Mission 4", "Building a CO2 neutral aeroplane"));

        return model;
    }

    private Mission createMission(String name, String description) {
        Mission mission = new MissionImpl();
        mission.setName(name);
        mission.setDescription(description);

        return mission;
    }

    private DefaultListModel createScenarioListModel() {
        DefaultListModel model = new DefaultListModel();
        model.addElement(createScenario("Design Challenge", "A desing never seen before"));
        model.addElement(createScenario("Inquiry Learning", "Inquire and become a better learner"));
        model.addElement(createScenario("Problem Resolution", "A problem is never too large"));
        model.addElement(createScenario("Close a Case", "Get done with the case"));
        model.addElement(createScenario("Decision Console", "A decision console, not a Wii console"));
        model.addElement(createScenario("Grasp a Model", "Do you get it?"));
        model.addElement(createScenario("Designing an Experimental Procedure", "Experiment and "));
        model.addElement(createScenario("The Big Project", "The largest project ever seen"));
        model.addElement(createScenario("Collaborative Controversies", "Enjoyable controversies for better learning"));
        model.addElement(createScenario("Co-Learn", "Controversies is old fashioned, collaboration is in!"));

        return model;
    }

    private Scenario createScenario(String name, String description) {
        Scenario scenario = new ScenarioImpl();
        scenario.setName(name);
        scenario.setDescription(description);

        return scenario;
    }
}
