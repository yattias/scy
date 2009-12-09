package eu.scy.scyplanner.components.application;

import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;

import eu.scy.scyplanner.application.SCYPlannerApplicationManager;
import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 09.des.2009
 * Time: 11:02:22
 */
public class LASOverviewPanel extends JPanel {
    private LearningActivitySpace las = null;

    public LASOverviewPanel(LearningActivitySpace las) {
        this.las = las;
        setBackground(SCYPlannerApplicationManager.getAlternativeBackgroundColor());

        setLayout(new BorderLayout());

        add(new JLabel(las.toString()), BorderLayout.NORTH);
    }
}
