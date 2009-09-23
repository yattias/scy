package eu.scy.scyplanner.components.application;

import eu.scy.scyplanner.components.titled.TitledList;
import eu.scy.scyplanner.components.titled.TitledPanel;
import eu.scy.core.model.pedagogicalplan.Mission;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.core.model.impl.pedagogicalplan.MissionImpl;
import eu.scy.core.model.impl.pedagogicalplan.ScenarioImpl;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 23.sep.2009
 * Time: 12:38:30
 */
public class SCYPlannerFrame extends JFrame {
    private final static int DEFAULT_BORDER_SIZE = 7;

    public SCYPlannerFrame() throws HeadlessException {
        super("SCYPlanner");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 748);
        setLocationRelativeTo(null);

        TitledList missionList = new TitledList("Available missions", createMissionListModel(), BorderFactory.createEmptyBorder(0, 0, DEFAULT_BORDER_SIZE, 0));
        TitledList scenarioList = new TitledList("Available scenarios", creatScenarioListModel());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(createMissionScenarioPanel(missionList, scenarioList), BorderLayout.WEST);
        getContentPane().add(CreateDefaultPedagogicalPlanPanel(missionList.getList(), scenarioList.getList()), BorderLayout.CENTER);
    }

    private JPanel createMissionScenarioPanel(TitledList missionList, TitledList scenarioList) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setPreferredSize(new Dimension(250, 100));
        panel.setBorder(BorderFactory.createEmptyBorder(DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE));

        panel.add(missionList);
        panel.add(scenarioList);

        return panel;
    }

    private JPanel CreateDefaultPedagogicalPlanPanel(JList missionList, JList scenarioList) {
        DefaultPedagogicalPlanInformationPanel panel = new DefaultPedagogicalPlanInformationPanel("Default pedagogical plan", BorderFactory.createEmptyBorder(DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE), missionList, scenarioList);

        return panel;
    }

    private DefaultListModel createMissionListModel() {
        DefaultListModel model = new DefaultListModel();

        model.addElement(createMission("Mission 1"));
        model.addElement(createMission("Mission 2"));
        model.addElement(createMission("Mission 3"));
        model.addElement(createMission("Mission 4"));

        return model;
    }

    private Mission createMission(String name) {
        Mission mission = new MissionImpl();
        mission.setName(name);

        return mission;
    }

    private DefaultListModel creatScenarioListModel() {
        DefaultListModel model = new DefaultListModel();
        model.addElement(createScenario("Design Challenge"));
        model.addElement(createScenario("Inquiry Learning"));
        model.addElement(createScenario("Problem Resolution"));
        model.addElement(createScenario("Close a Case"));
        model.addElement(createScenario("Decision Console"));
        model.addElement(createScenario("Grasp a Model"));
        model.addElement(createScenario("Designing an Experimental Procedure"));
        model.addElement(createScenario("The Big Project"));
        model.addElement(createScenario("Collaborative Controversies"));
        model.addElement(createScenario("Co-Learn"));

        return model;
    }

    private Scenario createScenario(String name) {
        Scenario scenario = new ScenarioImpl();
        scenario.setName(name);

        return scenario;
    }
}