package eu.scy.scyplanner.application;

import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.logger.ActionLogger;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.scyplanner.components.application.SCYPlannerFrame;
import eu.scy.scyplanner.components.application.WindowMenu;
import eu.scy.scyplanner.components.titled.TitledPanel;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import org.jivesoftware.smack.XMPPConnection;

import javax.swing.border.Border;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 23.sep.2009
 * Time: 23:29:42
 */
public class SCYPlannerApplicationManager {
    private final static Color LINK_COLOR = Color.LIGHT_GRAY;
    private final static Color ALTERNATIVE_BACKGROUND_COLOR = Color.WHITE;
    private final static int DEFAULT_BORDER_SIZE = 7;

    private final static SCYPlannerApplicationManager applicationManager = new SCYPlannerApplicationManager();
    private SCYPlannerFrame scyPlannerFrame = null;
    private WindowMenu windowMenu = null;

    ToolBrokerAPI toolBrokerAPI = new ToolBrokerImpl();

    private PedagogicalPlanService  pedagogicalPlanService;

    private SCYPlannerApplicationManager() {

        toolBrokerAPI = new ToolBrokerImpl();
        XMPPConnection connection = toolBrokerAPI.getConnection("admin", "admin");
        IActionLogger actionLogger = toolBrokerAPI.getActionLogger();
        pedagogicalPlanService = toolBrokerAPI.getPedagogicalPlanService();
        List scenarios = pedagogicalPlanService.getScenarios();
        for (int i = 0; i < scenarios.size(); i++) {
            Scenario scenario = (Scenario) scenarios.get(i);
            System.out.println(scenario.getName());
            System.out.println(scenario.getLearningActivitySpace().getName());
            System.out.println("size: " + scenario.getLearningActivitySpace().getActivities().size());
        }

    }

    public static SCYPlannerApplicationManager getApplicationManager() {
        return applicationManager;
    }

    public SCYPlannerFrame getScyPlannerFrame() {
        return scyPlannerFrame;
    }

    public ToolBrokerAPI getToolBrokerAPI() {
        return toolBrokerAPI;
    }

    public void setToolBrokerAPI(ToolBrokerAPI toolBrokerAPI) {
        this.toolBrokerAPI = toolBrokerAPI;
    }

    public void setScyPlannerFrame(SCYPlannerFrame scyPlannerFrame) {
        this.scyPlannerFrame = scyPlannerFrame;
    }

    public WindowMenu getWindowMenu() {
        return windowMenu;
    }

    public void setWindowMenu(WindowMenu windowMenu) {
        this.windowMenu = windowMenu;
    }

    public void showWaitCursor(Component component) {
        setCursor(component, new Cursor(Cursor.WAIT_CURSOR));
    }

    public static Color getLinkColor() {
        return LINK_COLOR;
    }

    public static Color getAlternativeBackgroundColor() {
        return ALTERNATIVE_BACKGROUND_COLOR;
    }

    public static int getDefaultBorderSize() {
        return DEFAULT_BORDER_SIZE;
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