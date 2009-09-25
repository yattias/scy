package eu.scy.scyplanner.action;

import eu.scy.scyplanner.application.SCYPlannerApplicationManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 23.sep.2009
 * Time: 23:19:10
 */
public abstract class AbstractSCYPlannerAction extends AbstractAction {
/**
     * Creates new AbstractSCYPlannerAction.
     */
    public AbstractSCYPlannerAction() {
    }

    /**
     * Creates new AbstractSCYPlannerAction.
     */
    public AbstractSCYPlannerAction(String title, Icon icon) {
        super(title, icon);
    }
                    
    public void actionPerformed(ActionEvent actionEvent) {
        SCYPlannerApplicationManager.getApplicationManager().showWaitCursor(SCYPlannerApplicationManager.getApplicationManager().getScyPlannerFrame());
        try {
            doActionPerformed(actionEvent);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            JTextArea textArea = new JTextArea();
            textArea.setPreferredSize(new Dimension(350, 150));
            textArea.setText(throwable.toString());
            JOptionPane.showMessageDialog(null, textArea, "Exception occured", JOptionPane.ERROR_MESSAGE);
            SCYPlannerApplicationManager.getApplicationManager().showDefaultCursor();
        }
        SCYPlannerApplicationManager.getApplicationManager().showDefaultCursor();
    }

    protected abstract void doActionPerformed(ActionEvent actionEvent);
}