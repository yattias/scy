package eu.scy.colemo.client.actions;

import eu.scy.colemo.server.uml.UmlClass;
import eu.scy.colemo.client.ApplicationController;
import eu.scy.colemo.client.SelectionController;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 19.nov.2008
 * Time: 06:40:21
 * To change this template use File | Settings | File Templates.
 */
public class DeleteConcept extends BaseAction{
    public DeleteConcept(String name) {
        super(name);
	    putValue(AbstractAction.SMALL_ICON, new ImageIcon(getClass().getResource("delete.png")));
    }

    public Class getOperateson() {
        return UmlClass.class;
    }

    protected void performAction(ActionEvent e) {
        log.info("Deleting concept!");
        Object objectToDelete = SelectionController.getDefaultInstance().getCurrentPrimarySelection();

        ApplicationController.getDefaultInstance().getConnectionHandler().deleteObject(objectToDelete);
        ApplicationController.getDefaultInstance().getGraphicsDiagram().deleteConcept((UmlClass) objectToDelete);
    }
}
