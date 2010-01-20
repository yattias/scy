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
public class LASOverviewPanel extends JPanel {
    private final static String EMPTY_STRING = "";
    private final static String YES = "Yes";
    private final static String NO = "No";
    
    private LearningActivitySpace las = null;

    public LASOverviewPanel(LearningActivitySpace las) {
        this.las = las;
        setBackground(SCYPlannerApplicationManager.getAlternativeBackgroundColor());
        setLayout(new BorderLayout());

        JPanel p = new JPanel(new GridLayout(0,1));
        p.setOpaque(false);
        p.add(createDummyPanel(createActivityPanel(las)));
        p.add(createDummyPanel(createAssessmentPanel(las)));
        p.add(createDummyPanel(createProducedAnchorELO(las)));
        add(BorderLayout.NORTH, p);
    }

    private JPanel createActivityPanel(LearningActivitySpace las) {
        List<Activity> activities = las.getActivities();

        Vector data = new Vector();
        for (Activity activity : activities) {
            Vector activityData = new Vector();
            activityData.add(activity.getName());
            activityData.add(activity.getAnchorELO());
            activityData.add(activity.getLearningActivitySpaceToolConfigurations().size());
            activityData.add(activity.getWorkArrangementType());
            activityData.add(activity.getTeacherRoleType());
            data.add(activityData);
        }

        Vector columns = new Vector();
        columns.add(Strings.getString("Name"));
        columns.add(Strings.getString("AnchorELO"));
        columns.add(Strings.getString("Tool#"));
        columns.add(Strings.getString("Work arrangement"));
        columns.add(Strings.getString("Teacher role"));

        return new TablePanel(Strings.getString("Activities"), data, columns);
    }

    private JComponent createAssessmentPanel(LearningActivitySpace las) {
        if (las.getAssessment() != null) {
            Vector data = new Vector();
            Vector assessmentData = new Vector();
            assessmentData.add(las.getAssessment().getName());
            assessmentData.add(las.getAssessment().getDescription());
            assessmentData.add(las.getAssessment().getAssessmentStrategy());

            data.add(assessmentData);

            Vector columns = new Vector();
            columns.add(Strings.getString("Name"));
            columns.add(Strings.getString("Description"));
            columns.add(Strings.getString("Strategy"));

            return new TablePanel("Assessment", data, columns);
        } else {
            return new MissingDataPanel("Assessment", "Assessment not defined");
        }
    }

    private JComponent createProducedAnchorELO(LearningActivitySpace las) {
        if (las.getProduces() != null) {
            Iterator<AnchorELO> elements = las.getProduces().iterator();
            Vector data = new Vector();
            while (elements.hasNext()) {
                AnchorELO element = elements.next();
                Vector activityData = new Vector();
                activityData.add(element.getName());
                activityData.add(element.getAssessment());
                activityData.add(booleanText(element.getObligatoryInPortfolio()));
                data.add(activityData);
            }

            Vector columns = new Vector();
            columns.add(Strings.getString("Name"));
            columns.add(Strings.getString("Assessment"));
            columns.add(Strings.getString("In portfolio?"));

            return new TablePanel(Strings.getString("Anchor ELOs"), data, columns);
        } else {
            return new MissingDataPanel(Strings.getString("Anchor ELOs"), "Strings.getString(Anchor ELOs not specified");
        }
    }

    private JPanel createDummyPanel(JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(SCYPlannerApplicationManager.getApplicationManager().createDefaultBorder());
        panel.add(BorderLayout.CENTER, component);

        return panel;
    }

    private class TablePanel extends TitledPanel {
        private TablePanel(String title, Vector data, Vector columns) {
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

    private class MissingDataPanel extends TitledPanel {
        private MissingDataPanel(String title, String message) {
            super(title);
            setOpaque(false);
            add(BorderLayout.NORTH, new JLabel(message));
        }
    }

    private String booleanText(boolean value) {
        if (value == false) {
            return NO;
        }

        return YES;
    }
}