package eu.scy.scyplanner.components.application;

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

        model.addElement(createMission("Mission 1", "Building a CO2 neutral house", new String[] {"Students gain awareness on environmental (and political issues) concerning emissions of CO2", "Students identify key issues that are important when designing a CO2-friendly house"}));
        model.addElement(createMission("Mission 2", "Building a CO2 neutral bike", new String[] {"Goal 1", "Goal 2"}));
        model.addElement(createMission("Mission 3", "Building a CO2 neutral car", new String[] {"Goal 3", "Goal 4"}));
        model.addElement(createMission("Mission 4", "Building a CO2 neutral aeroplane", new String[] {"Goal 5", "Goal 6"}));

        return model;
    }

    private Mission createMission(String name, String description, String[] learningGoals) {
        Mission mission = new MissionImpl();
        mission.setName(name);
        mission.setDescription(description);

        for (String learningGoal : learningGoals) {
            LearningGoal goal = new LearningGoalImpl();
            goal.setName(learningGoal);
            mission.addLearningGoal(goal);
        }

        return mission;
    }

    private DefaultListModel createScenarioListModel() {
        DefaultListModel model = new DefaultListModel();
        System.out.println("GETTING SCENIS!");
        List <Scenario> scenarios = SCYPlannerApplicationManager.getApplicationManager().getPedagogicalPlanService().getScenarios();
        for (Scenario scenario: scenarios) {
            model.addElement(scenario);
            System.out.println("adding " + scenario.getName() + " " + scenario.getDescription());
        }
/*
        model.addElement(createScenario("Design Challenge", "A desing never seen before", new LearningActivitySpaceImpl(), "Orientation"));
        model.addElement(createScenario("Inquiry Learning", "Inquire and become a better learner", new LearningActivitySpaceImpl(), "Conceptualisation"));
        model.addElement(createScenario("Problem Resolution", "A problem is never too large", new LearningActivitySpaceImpl(), "Design"));
        model.addElement(createScenario("Close a Case", "Get done with the case", new LearningActivitySpaceImpl(), "Conceptualisation"));
        model.addElement(createScenario("Decision Console", "A decision console, not a Wii console", new LearningActivitySpaceImpl(), "Orientation"));
        model.addElement(createScenario("Grasp a Model", "Do you get it?", new LearningActivitySpaceImpl(), "Design"));
        model.addElement(createScenario("Designing an Experimental Procedure", "Experiment and ", new LearningActivitySpaceImpl(), "Conceptualisation"));
        model.addElement(createScenario("The Big Project", "The largest project ever seen", new LearningActivitySpaceImpl(), "Design"));
        model.addElement(createScenario("Collaborative Controversies", "Enjoyable controversies for better learning", new LearningActivitySpaceImpl(), "Conceptualisation"));
        model.addElement(createScenario("Co-Learn", "Controversies is old fashioned, collaboration is in!", new LearningActivitySpaceImpl(), "Orientation"));
  */
        return model;
    }

    private Scenario createScenario(String name, String description, LearningActivitySpace las, String lasName) {
        Scenario scenario = new ScenarioImpl();
        scenario.setName(name);
        scenario.setDescription(description);

        las.setName(lasName);
        scenario.setLearningActivitySpace(las);

        return scenario;
    }
}
