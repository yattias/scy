package eu.scy.scyplanner.components.application;

import eu.scy.core.model.auth.SessionInfo;
import eu.scy.scyplanner.application.Strings;
import eu.scy.scyplanner.components.utilities.LabelComponent;
import eu.scy.scyplanner.components.utilities.LabeledComponentsPanel;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;

import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 19.jan.2010
 * Time: 11:39:53
 */
public class SCYPlannerLogin {
    private static Logger log = Logger.getLogger("eu.scy.scyplanner.components.application.SCYPlannerLogin");

    /**
     * Presents a modal dialog asking for the user's user name
     * and password.
     *
     * @return true if the user decides to go on with the connect, false if cancel is pressed.
     */
    public static PedagogicalPlanService logIn() {
        SessionInfo sessionInfo = null;
        PedagogicalPlanService service = null;

        HttpInvokerProxyFactoryBean fb = new HttpInvokerProxyFactoryBean();
        fb.setServiceInterface(PedagogicalPlanService.class);


        if (System.getProperty("username") != null) {
            String username = System.getProperty("username");
            String password = System.getProperty("password");
            String host = System.getProperty("host");

            fb.setServiceUrl("http://" + host + ":8080/server-external-components/remoting/pedagogicalPlan-httpinvoker");
            fb.afterPropertiesSet();
            service = (PedagogicalPlanService) fb.getObject();

            sessionInfo = service.login(username, password);

        } else {
            String[] urls = new String[]{
                    "http://localhost:8080/server-external-components/remoting/pedagogicalPlan-httpinvoker",
                    "http://scy.collide.info:8080/extcomp/remoting/pedagogicalPlan-httpinvoker"
            };

            JTextField userName = new JTextField();
            userName.setCaretPosition(0);
            JPasswordField password = new JPasswordField();
            password.setCaretPosition(0);
            JComboBox box = new JComboBox(urls);
            LabelComponent[] fields = new LabelComponent[]{
                    new LabelComponent(Strings.getString("Username"), userName),
                    new LabelComponent(Strings.getString("Password"), password),
                    new LabelComponent(Strings.getString("Server"), box)
            };
            LabeledComponentsPanel panel = new LabeledComponentsPanel(fields);

            boolean cont = true;
            boolean returnValue = false;

            while (cont) {
                int result = JOptionPane.showConfirmDialog(null, panel, Strings.getString("Confirm Log On"), JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    log.info((String) box.getSelectedItem());
                    fb.setServiceUrl((String) box.getSelectedItem());
                    fb.afterPropertiesSet();
                    service = (PedagogicalPlanService) fb.getObject();

                    sessionInfo = service.login(userName.getText(), new String(password.getPassword()));
                    if (sessionInfo != null) {
                        returnValue = true;
                    }
                    if (returnValue) {
                        cont = false;
                    } else {
                        JOptionPane.showMessageDialog(null, Strings.getString("User name and / or password was incorect. Plase try again."), Strings.getString("User and / or password not found"), JOptionPane.INFORMATION_MESSAGE);
                    }
                } else if (result == JOptionPane.CANCEL_OPTION) {
                    cont = false;
                    JOptionPane.showMessageDialog(null, Strings.getString("Log in cancelled."), Strings.getString("Log in cancelled"), JOptionPane.INFORMATION_MESSAGE);
                    log.info("Log in cancelled by user");
                }
            }
        }


        if (sessionInfo == null) {
            return null;
        }

        return service;
    }
}