package eu.scy.scyplanner.components.application;

import eu.scy.scyplanner.components.frontpage.StartUpInformationItem;
import eu.scy.scyplanner.components.frontpage.StartUpMenuItem;
import eu.scy.scyplanner.application.SCYPlannerApplicationManager;
import eu.scy.scyplanner.action.AbstractSCYPlannerAction;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.core.model.pedagogicalplan.Mission;
import eu.scy.core.model.pedagogicalplan.LearningGoal;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 23.sep.2009
 * Time: 15:24:36
 */
public class DefaultPedagogicalPlanInformationPanel extends JPanel {
    private final static String LINE_BREAK = System.getProperty("line.separator");
    private final static String ONE_SPACE_STRING = " ";
    private final static String COLON_ONE_SPACE_STRING = ": ";
    private CreateNewPedagogicalPlanFromDefaultPedagogicalPlan action = new CreateNewPedagogicalPlanFromDefaultPedagogicalPlan();

    public DefaultPedagogicalPlanInformationPanel(JList missionList, JList scenarioList) {
        super(new BorderLayout());
        setBackground(SCYPlannerApplicationManager.getAlternativeBackgroundColor());

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridLayout(0, 1));

        StartUpInformationItem missionInfo = new StartUpInformationItem("Mission", "Not selected", null);
        panel.add(missionInfo);

        StartUpInformationItem scenarioInfo = new StartUpInformationItem("Scenario", "Not selected", null);
        panel.add(scenarioInfo);

        add(BorderLayout.NORTH, panel);

        addMissionListSelectionListener(missionList, missionInfo);
        addScenarioListSelectionListener(scenarioList, scenarioInfo);
    }

    private void addMissionListSelectionListener(final JList list, final StartUpInformationItem label) {
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Mission mission = (Mission) list.getModel().getElementAt(list.getSelectedIndex());
                    action.setMission(mission);
                    label.setTitle(mission.getName());
                    StringBuffer buffer = new StringBuffer("<i>").append(mission.getDescription()).append("</i>");
                    buffer.append("<br/><br/>Learning goals:<br/>");
                    Iterator<LearningGoal> learningGoals = mission.getLearningGoals().iterator();
                    buffer.append("<ol>");
                    while (learningGoals.hasNext()) {
                        buffer.append("<li>").append(learningGoals.next().getName()).append("</li>");
                    }
                    buffer.append("</ol>");
                    label.setDescription(buffer.toString());
                }
            }
        });
    }

    private void addScenarioListSelectionListener(final JList list, final StartUpInformationItem label) {
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Scenario scenario = (Scenario) list.getModel().getElementAt(list.getSelectedIndex());
                    action.setScenario(scenario);
                    label.setTitle(scenario.getName());
                    label.setDescription(scenario.getDescription());
                }
            }
        });
    }

    private class CreateNewPedagogicalPlanFromDefaultPedagogicalPlan extends AbstractSCYPlannerAction {
        private Mission mission = null;
        private Scenario scenario = null;
        private StartUpMenuItem item = null;

        private CreateNewPedagogicalPlanFromDefaultPedagogicalPlan() {
            super("Create New Pedagogical Plan", null, "Copies the default selected pedagogical plan that is available for the selected combination of mission and scenario. You can use the copy directly without any changes or fine-tune it according to your needs");
        }

        @Override
        protected void doActionPerformed(ActionEvent actionEvent) {
            PedagogicalPlanPanel panel = new PedagogicalPlanPanel(mission, scenario);
            SCYPlannerApplicationManager.getApplicationManager().getScyPlannerFrame().setContent("Pedagogical plan", panel);
        }

        public Mission getMission() {
            return mission;
        }

        public void setMission(Mission mission) {
            this.mission = mission;

            if (isEnabled()) {
                addToPanel();
            }
        }

        public Scenario getScenario() {
            return scenario;
        }

        public void setScenario(Scenario scenario) {
            this.scenario = scenario;

            if (isEnabled()) {
                addToPanel();
            }
        }

        private void addToPanel() {
            if (item != null) {
                remove(item);
            }
            item = new StartUpMenuItem(action, null);
            item.setBorder(SCYPlannerApplicationManager.getApplicationManager().createDefaultBorder());
            add(BorderLayout.SOUTH, item);
        }

        @Override
        public boolean isEnabled() {
            return scenario!= null && mission != null;
        }
    }
}
