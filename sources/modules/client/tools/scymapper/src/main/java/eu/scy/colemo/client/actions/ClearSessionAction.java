package eu.scy.colemo.client.actions;

import eu.scy.colemo.client.ApplicationController;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.apr.2009
 * Time: 20:37:40
 * To change this template use File | Settings | File Templates.
 */
public class ClearSessionAction extends SCYMapperAction{

    private static final Logger log = Logger.getLogger(ClearSessionAction.class.getName());

    public ClearSessionAction() {
        setTitle("Clear");
	    putValue(AbstractAction.SMALL_ICON, new ImageIcon(getClass().getResource("clear.png")));
    }

    protected void performAction(ActionEvent e) {
        log.debug("Clearing session");
        ApplicationController.getDefaultInstance().getConnectionHandler().cleanUp();
    }
}
