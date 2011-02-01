package eu.scy.scyplanner.components.application;

import eu.scy.core.model.pedagogicalplan.LearningGoal;
import eu.scy.core.model.pedagogicalplan.Mission;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.scyplanner.action.AbstractSCYPlannerAction;
import eu.scy.scyplanner.application.SCYPlannerApplicationManager;
import eu.scy.scyplanner.application.Strings;
import eu.scy.scyplanner.components.frontpage.StartUpInformationItem;
import eu.scy.scyplanner.components.frontpage.StartUpMenuItem;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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

    public DefaultPedagogicalPlanInformationPanel(final JList missionList, final JList scenarioList) {
        super(new BorderLayout());
        setBackground(SCYPlannerApplicationManager.getAlternativeBackgroundColor());

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridLayout(0, 1));

        StartUpInformationItem missionInfo = new StartUpInformationItem(Strings.getString("Mission"), Strings.getString("Not selected"), null);
        panel.add(missionInfo);

        StartUpInformationItem scenarioInfo = new StartUpInformationItem(Strings.getString("Scenario"), Strings.getString("Not selected"), null);
        panel.add(scenarioInfo);

        add(BorderLayout.NORTH, panel);

        addMissionListSelectionListener(missionList, scenarioList, missionInfo);
        addScenarioListSelectionListener(missionList, scenarioList, scenarioInfo);
    }

    private void addMissionListSelectionListener(final JList missionList, final JList scenarioList, final StartUpInformationItem label) {
        missionList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (missionList.isSelectionEmpty()) {
                        action.setMission(null);
                        label.setDescription(Strings.getString("Not selected"));
                        replaceAllElements(missionList, SCYPlannerApplicationManager.getApplicationManager().getPedagogicalPlanService().getMissions());
                        replaceAllElements(scenarioList, SCYPlannerApplicationManager.getApplicationManager().getPedagogicalPlanService().getScenarios());
                    } else {
                        Mission mission = (Mission) missionList.getModel().getElementAt(missionList.getSelectedIndex());
                        action.setMission(mission);
                        label.setTitle(mission.getName());
                        StringBuffer buffer = new StringBuffer("<i>").append(mission.getDescription()).append("</i>");
                        buffer.append("<br/><br/>").append(Strings.getString("Learning goals:")).append("<br/>");
                        Iterator<LearningGoal> learningGoals = mission.getLearningGoals().iterator();
                        buffer.append("<ol>");
                        while (learningGoals.hasNext()) {
                            buffer.append("<li>").append(learningGoals.next().getName()).append("</li>");
                        }
                        buffer.append("</ol>");
                        label.setDescription(buffer.toString());
                        if (scenarioList.isSelectionEmpty()) {
                            replaceAllElements(scenarioList, SCYPlannerApplicationManager.getApplicationManager().getPedagogicalPlanService().getCompatibleScenarios(mission));
                        }
                    }
                }
            }
        });
    }

    private void addScenarioListSelectionListener(final JList missionList, final JList scenarioList, final StartUpInformationItem label) {
        scenarioList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (scenarioList.isSelectionEmpty()) {
                        action.setScenario(null);
                        label.setDescription(Strings.getString("Not selected"));
                        replaceAllElements(scenarioList, SCYPlannerApplicationManager.getApplicationManager().getPedagogicalPlanService().getScenarios());
                        replaceAllElements(missionList, SCYPlannerApplicationManager.getApplicationManager().getPedagogicalPlanService().getMissions());
                    } else {
                        Scenario scenario = (Scenario) scenarioList.getModel().getElementAt(scenarioList.getSelectedIndex());
                        action.setScenario(scenario);
                        label.setTitle(scenario.getName());
                        label.setDescription(scenario.getDescription());
                        if (missionList.isSelectionEmpty()) {
                            replaceAllElements(missionList, SCYPlannerApplicationManager.getApplicationManager().getPedagogicalPlanService().getCompatibleMissions(scenario));
                        }
                    }
                }
            }
        });
    }

    private void replaceAllElements(JList list, List elements) {
        list.clearSelection();
        ListSelectionListener[] listeners = list.getListSelectionListeners();
        for (ListSelectionListener listener : listeners) {
            list.removeListSelectionListener(listener);
        }
        DefaultListModel model = (DefaultListModel) list.getModel();
        model.removeAllElements();

        for (Object object : elements) {
            model.addElement(object);
        }

        for (ListSelectionListener listener : listeners) {
            list.addListSelectionListener(listener);
        }
    }

    private class CreateNewPedagogicalPlanFromDefaultPedagogicalPlan extends AbstractSCYPlannerAction {
        private Mission mission = null;
        private Scenario scenario = null;
        private StartUpMenuItem item = null;

        private CreateNewPedagogicalPlanFromDefaultPedagogicalPlan() {
            super(Strings.getString("Create New Pedagogical Plan"), null, Strings.getString("Copies the default selected pedagogical plan that is available for the selected combination of mission and scenario. You can use the copy directly without any changes or fine-tune it according to your needs"));
        }

        @Override
        protected void doActionPerformed(ActionEvent actionEvent) {
            // System.out.println("Searching for plan for: " + mission + " and " + scenario);
            long start = System.currentTimeMillis();
            PedagogicalPlan plan = SCYPlannerApplicationManager.getApplicationManager().getPedagogicalPlanService().getPedagogicalPlan(mission, scenario);
            // System.out.println("USED : " + (System.currentTimeMillis() - start) + " to load pedagogical plan!");
            if (plan != null) {
                PedagogicalPlanPanel panel = new PedagogicalPlanPanel(plan);

                SCYPlannerApplicationManager.getApplicationManager().getScyPlannerFrame().setContent("Pedagogical plan", panel);
            }

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
            return scenario != null && mission != null;
        }
    }
}
