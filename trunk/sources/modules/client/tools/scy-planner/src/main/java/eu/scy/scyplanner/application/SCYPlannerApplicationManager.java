package eu.scy.scyplanner.application;

import eu.scy.scyplanner.components.application.SCYPlannerFrame;
import eu.scy.scyplanner.components.titled.TitledPanel;

import javax.swing.border.Border;
import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 23.sep.2009
 * Time: 23:29:42
 */
public class SCYPlannerApplicationManager {
    public final static Color LINK_COLOR = Color.LIGHT_GRAY;
    public final static int DEFAULT_BORDER_SIZE = 7;

    private final static SCYPlannerApplicationManager applicationManager = new SCYPlannerApplicationManager();
    private SCYPlannerFrame scyPlannerFrame = null;

    private SCYPlannerApplicationManager() {
    }

    public static SCYPlannerApplicationManager getApplicationManager() {
        return applicationManager;
    }

    public SCYPlannerFrame getScyPlannerFrame() {
        return scyPlannerFrame;
    }

    public void setScyPlannerFrame(SCYPlannerFrame scyPlannerFrame) {
        this.scyPlannerFrame = scyPlannerFrame;
    }

    public void showWaitCursor(Component component) {
        setCursor(component, new Cursor(Cursor.WAIT_CURSOR));
    }

    public void showDefaultCursor() {
        showDefaultCursor(getScyPlannerFrame());
        Window[] windows = getScyPlannerFrame().getOwnedWindows();
        for (Window window : windows) {
            showDefaultCursor(window);
        }
    }

    public void showDefaultCursor(Component component) {
        setCursor(component, new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public Border createDefaultBorder() {
        return BorderFactory.createEmptyBorder(DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE);
    }

    public JPanel createDefaultBorderForTitledPanel(TitledPanel panel) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(panel);
        p.setBorder(createDefaultBorder());

        return p;
    }

    public JPanel createDefaultBorderForTitledPanel(TitledPanel panel, Color background) {
        JPanel p = createDefaultBorderForTitledPanel(panel);
        p.setBackground(background);
        
        return p;
    }

    private void setCursor(Component component, Cursor cursor) {
        if (component != null) {
            component.setCursor(cursor);
        }
    }
}