package eu.scy.scyplanner.components.application;

import eu.scy.scyplanner.components.titled.TitledPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 23.sep.2009
 * Time: 15:24:36
 */
public class DefaultPedagogicalPlanInformationPanel extends TitledPanel {
    public DefaultPedagogicalPlanInformationPanel(String title, JList missionList, JList scenarioList) {
        super(title);

        setLayout(new BorderLayout());

        JLabel mission = new JLabel();
        add(BorderLayout.NORTH, mission);

        JLabel scenario = new JLabel();
        add(BorderLayout.SOUTH, scenario);

        addMissionListSelectionListener(missionList, mission);
        addScenarioListSelectionListener(scenarioList, scenario);
    }

    private void addMissionListSelectionListener(final JList list, final JLabel label) {
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    label.setText(String.valueOf(list.getModel().getElementAt(list.getSelectedIndex())));
                }
            }
        });
    }

    private void addScenarioListSelectionListener(final JList list, final JLabel label) {
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    label.setText(String.valueOf(list.getModel().getElementAt(list.getSelectedIndex())));
                }
            }
        });
    }
}
