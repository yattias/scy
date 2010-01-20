package eu.scy.scyplanner.components.application;

import eu.scy.scyplanner.action.AbstractSCYPlannerAction;
import eu.scy.scyplanner.application.SCYPlannerApplicationManager;

import eu.scy.scyplanner.application.Strings;
import javax.swing.*;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.InternalFrameEvent;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 25.sep.2009
 * Time: 13:29:46
 */
public class WindowMenu extends JMenu implements InternalFrameListener {
    private HashMap map = new HashMap<JMenuItem,JInternalFrame>();
    public WindowMenu() {
        super(Strings.getString("Window"));
        SCYPlannerApplicationManager.getApplicationManager().setWindowMenu(this);
    }

    @Override
    public void internalFrameOpened(final InternalFrameEvent e) {
        final JInternalFrame frame = e.getInternalFrame();
        JMenuItem item = new JMenuItem();
        item.setAction(new AbstractSCYPlannerAction() {

            @Override
            protected void doActionPerformed(ActionEvent actionEvent) {
                try {
                    frame.setIcon(false);
                    frame.setSelected(true);
                } catch (PropertyVetoException e1) {
                    e1.printStackTrace();
                }
            }
        });
        map.put(frame, item);
        item.setText(frame.getTitle());
        add(item);
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
        JInternalFrame frame = e.getInternalFrame();
        remove((JMenuItem) map.get(frame));
        map.remove(frame);
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
    }
}
