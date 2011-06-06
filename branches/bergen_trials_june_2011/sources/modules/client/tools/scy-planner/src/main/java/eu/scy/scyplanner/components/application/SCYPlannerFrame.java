package eu.scy.scyplanner.components.application;

import eu.scy.scyplanner.application.SCYPlannerApplicationManager;

import eu.scy.scyplanner.application.Strings;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 23.sep.2009
 * Time: 12:38:30
 */
public class SCYPlannerFrame extends JFrame {
    private JComponent currentComponentInsideContentArea = null;
    private JComponent defaultScreen = null;
    private JDesktopPane desktop = new JDesktopPane();

    public SCYPlannerFrame() throws HeadlessException {
        super(Strings.getString("SCYPlanner"));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 748);
        setLocationRelativeTo(null);

        setJMenuBar(new SCYPlannerMenu());

        getContentPane().setLayout(new BorderLayout());

        getContentPane().add(BorderLayout.NORTH, new SCYPlannerToolBar());

        setContentPane(desktop);
        setContent(Strings.getString("SCYPlanner"), new WelcomePanel(), true, false, true, false, true);
        currentComponentInsideContentArea = defaultScreen;
    }

    public void setContent(String title, JComponent component) {
        setContent(title, component, true, true, true, true);
    }

    public void setContent(String title, JComponent component, boolean resizeable, boolean cloesable, boolean maximizable, boolean iconifiable) {
        setContent(title, component, resizeable, cloesable, maximizable, iconifiable, false);
    }

    public void setContent(String title, JComponent component, boolean resizeable, boolean cloesable, boolean maximizable, boolean iconifiable, boolean maximizedImmediately) {
        JInternalFrame frame = new JInternalFrame("", resizeable, cloesable, maximizable, iconifiable);
        frame.setTitle(title);
        frame.addInternalFrameListener(SCYPlannerApplicationManager.getApplicationManager().getWindowMenu());
        frame.add(component);
        frame.setVisible(true);
        frame.setSize(750, 600);        
        desktop.add(frame);

        try {
            frame.setSelected(true);
            frame.setMaximum(maximizedImmediately);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }                
    }
}