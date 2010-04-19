package eu.scy.scyplanner.components.application;

import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.scyplanner.application.SCYPlannerApplicationManager;
import eu.scy.scyplanner.application.Strings;
import eu.scy.scyplanner.components.table.Table;
import eu.scy.scyplanner.components.titled.TitledPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 09.des.2009
 * Time: 11:02:22
 */
public abstract class SCYPlannerOverviewPanel extends JPanel {
    private final static String EMPTY_STRING = "";
    private final static String YES = "Yes";
    private final static String NO = "No";

    private LearningActivitySpace las = null;

    protected JPanel createDummyPanel(JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(SCYPlannerApplicationManager.getApplicationManager().createDefaultBorder());
        panel.add(BorderLayout.CENTER, component);

        return panel;
    }

    protected class TablePanel extends TitledPanel {
        protected TablePanel(String title, Vector data, Vector columns) {
            super(title);
            setOpaque(false);
            Table table = new Table();
            table.setModel(new DefaultTableModel(data, columns));

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setPreferredSize(new Dimension(200, 125));
            add(BorderLayout.CENTER, scrollPane);
        }
    }

    protected class MissingDataPanel extends TitledPanel {
        protected MissingDataPanel(String title, String message) {
            super(title);
            setOpaque(false);
            add(BorderLayout.NORTH, new JLabel(message));
        }
    }

    protected String booleanText(boolean value) {
        if (value == false) {
            return NO;
        }

        return YES;
    }
}