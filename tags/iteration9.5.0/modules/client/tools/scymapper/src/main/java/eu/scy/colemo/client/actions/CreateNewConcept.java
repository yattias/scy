package eu.scy.colemo.client.actions;

import eu.scy.colemo.client.ApplicationController;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.apr.2009
 * Time: 20:52:54
 * To change this template use File | Settings | File Templates.
 */
public class CreateNewConcept extends SCYMapperAction{

    public CreateNewConcept() {
        setTitle("New Concept");
	    putValue(AbstractAction.SMALL_ICON, new ImageIcon(getClass().getResource("new.png")));
    }

    protected void performAction(ActionEvent e) {
        ApplicationController.getDefaultInstance().getColemoPanel().addNewConcept(ApplicationController.getDefaultInstance().getGraphicsDiagram().getUmlDiagram(), "c");
    }
}