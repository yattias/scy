package eu.scy.scyplanner.components.application;

import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.scyplanner.application.SCYPlannerApplicationManager;
import eu.scy.scyplanner.application.Strings;
import eu.scy.scyplanner.components.table.Table;
import eu.scy.scyplanner.components.text.SCYPlannerTextArea;
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
public class AnchorELOOverviewPanel extends SCYPlannerOverviewPanel {
    
    private AnchorELO anchorELO = null;

    public AnchorELOOverviewPanel(AnchorELO anchorELO) {
        this.anchorELO = anchorELO;
        setBackground(SCYPlannerApplicationManager.getAlternativeBackgroundColor());
        setLayout(new BorderLayout());

        add(BorderLayout.NORTH, new JLabel(anchorELO.toString()));
        add(BorderLayout.CENTER, new SCYPlannerTextArea(anchorELO.getDescription()));
    }

}