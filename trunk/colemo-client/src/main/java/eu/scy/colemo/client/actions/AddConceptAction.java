package eu.scy.colemo.client.actions;

import eu.scy.colemo.client.ApplicationController;
import eu.scy.colemo.client.MainFrame;
import eu.scy.colemo.client.GraphicsDiagram;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 19.nov.2008
 * Time: 06:18:40
 * To change this template use File | Settings | File Templates.
 */
public class AddConceptAction extends BaseAction {
    public Class getOperateson() {
        return GraphicsDiagram.class;
    }

    protected void performAction(ActionEvent e) {
        log.info("Adding concept....");
        MainFrame frame = ApplicationController.getDefaultInstance().getMainFrame();
        if(frame == null) log.info("Frame is null");
        if(frame.getGraphicsDiagram() == null) log.info("Graphics diagram is null");
        if(frame.getGraphicsDiagram().getUmlDiagram() == null) log.info("UML DIAGRAM is null");
        ApplicationController.getDefaultInstance().getConnectionHandler().sendMessage("ADD CONCEPT!");
        //ApplicationController.getDefaultInstance().getMainFrame().addClass(ApplicationController.getDefaultInstance().getGraphicsDiagram().getUmlDiagram(), "c");
    }
}
