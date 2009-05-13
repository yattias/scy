package eu.scy.colemo.client.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.apr.2009
 * Time: 20:36:56
 * To change this template use File | Settings | File Templates.
 */
public abstract class SCYMapperAction extends AbstractAction {
    protected java.util.logging.Logger log = java.util.logging.Logger.getLogger("BaseAction.class");


    public void setTitle(String title) {
        putValue(AbstractAction.NAME, title);
    }

    public void actionPerformed(ActionEvent e) {
        log.info("Performing action: " + getClass().getName());
        performAction(e);
        log.info("Action performed");
    }

    protected abstract void performAction(ActionEvent e) ;
}
