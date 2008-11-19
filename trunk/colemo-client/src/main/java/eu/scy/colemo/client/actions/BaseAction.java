package eu.scy.colemo.client.actions;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 19.nov.2008
 * Time: 06:18:54
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseAction extends AbstractAction {

    protected Logger log = Logger.getLogger("BaseAction.class");



    public void setTitle(String title) {
        super.putValue(AbstractAction.NAME, title);
    }

    public abstract Class getOperateson();

    public void actionPerformed(ActionEvent e) {
        log.info("Performing action: " + getClass().getName());
        performAction(e);
        log.info("Action performed");
    }

    protected abstract void performAction(ActionEvent e) ;
}
