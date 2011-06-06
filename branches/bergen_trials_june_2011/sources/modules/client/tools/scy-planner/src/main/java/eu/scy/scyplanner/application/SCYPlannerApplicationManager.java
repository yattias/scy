package eu.scy.scyplanner.application;

import eu.scy.core.model.auth.SessionInfo;
import eu.scy.core.model.pedagogicalplan.*;
import eu.scy.scyplanner.components.application.SCYPlannerFrame;
import eu.scy.scyplanner.components.application.SCYPlannerLogin;
import eu.scy.scyplanner.components.application.WindowMenu;
import eu.scy.scyplanner.components.titled.TitledPanel;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;
import eu.scy.server.pedagogicalplan.StudentPedagogicalPlanService;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import javax.swing.border.Border;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 23.sep.2009
 * Time: 23:29:42
 */
public class SCYPlannerApplicationManager {

    private static Logger log = Logger.getLogger("SCYPlannerApplicationManager.class");

    private final static Color LINK_COLOR = Color.LIGHT_GRAY;
    private final static Color ALTERNATIVE_BACKGROUND_COLOR = Color.WHITE;
    private final static int DEFAULT_BORDER_SIZE = 7;

    private final static SCYPlannerApplicationManager applicationManager = new SCYPlannerApplicationManager();
    private SCYPlannerFrame scyPlannerFrame = null;
    private WindowMenu windowMenu = null;

    private ToolBrokerAPI toolBrokerAPI = null;//new ToolBrokerImpl();

    private PedagogicalPlanService pedagogicalPlanService;

    private SCYPlannerApplicationManager() {

        //pedagogicalPlanService = new PedagogicalPlanServiceMock();


        /*HttpInvokerProxyFactoryBean bean = (HttpInvokerProxyFactoryBean) getToolBrokerAPI().getBean("httpInvokerPedagogicalPlanServiceProxy");
        bean.setServiceUrl("http://localhost:8080/webapp/remoting/pedagogicalPlan-httpinvoker");
        getToolBrokerAPI().getPedagogicalPlanService().getScenarios();
        */

        //String url = JOptionPane.showInputDialog("Input host (for example localhost)");

        PedagogicalPlanService service = SCYPlannerLogin.logIn();
        if (service == null) {
            log.info("Not logged in, exiting!");
            System.exit(0);
        } else {
            /*List pedagogicalPlans = service.getPedagogicalPlanTemplates();
            for (Object pedagogicalPlan : pedagogicalPlans) {
                PedagogicalPlanTemplate pedagogicalPlanTemplate = (PedagogicalPlanTemplate) pedagogicalPlan;
                // System.out.println("TEMPLATE:" + pedagogicalPlanTemplate.getName());
            }


            List scenarios = service.getScenarios();
            // System.out.println("SECARIO COUNT: " + scenarios.size());
            for (Object scenario1 : scenarios) {
                Scenario scenario = (Scenario) scenario1;
                // System.out.println("SCENARIO: " + scenario.getName());
            }
            */                                                               
            this.pedagogicalPlanService = service;

        }
        /*StudentPedagogicalPlanService studentService = createStudentPedagogicalPlanService();
        if(studentService == null) throw new RuntimeException("STUDENT SERVICE IS NULL!");
        studentService.createStudentPlan(null, null);
        */

    }

    private PedagogicalPlanService createPedagogicalPlanService() {
        HttpInvokerProxyFactoryBean fb = new HttpInvokerProxyFactoryBean();
        fb.setServiceInterface(PedagogicalPlanService.class);
        fb.setServiceUrl("http://scy.collide.info:8080/extcomp/remoting/pedagogicalPlan-httpinvoker");
        //fb.setServiceUrl("http://localhost:8080/server-external-components/remoting/pedagogicalPlan-httpinvoker");
        fb.afterPropertiesSet();
        PedagogicalPlanService service = (PedagogicalPlanService) fb.getObject();
        return service;
    }

    private StudentPedagogicalPlanService createStudentPedagogicalPlanService() {
        HttpInvokerProxyFactoryBean fb = new HttpInvokerProxyFactoryBean();
        fb.setServiceInterface(StudentPedagogicalPlanService.class);
        fb.setServiceUrl("http://localhost:8080/server-external-components/remoting/studentPlan-httpinvoker");
        fb.afterPropertiesSet();
        StudentPedagogicalPlanService service = (StudentPedagogicalPlanService) fb.getObject();
        return service;
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

    public PedagogicalPlanService getPedagogicalPlanService() {
        return pedagogicalPlanService;
    }

    public void setPedagogicalPlanService(PedagogicalPlanService pedagogicalPlanService) {
        this.pedagogicalPlanService = pedagogicalPlanService;
    }
}