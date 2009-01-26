package eu.scy.colemo.client.actions;

import eu.scy.colemo.client.ApplicationController;
import eu.scy.colemo.client.MainFrame;
import eu.scy.colemo.client.GraphicsDiagram;
import eu.scy.colemo.network.Person;
import eu.scy.colemo.contributions.AddClass;
import eu.scy.colemo.server.exceptions.ClassNameAlreadyExistException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.InetAddress;

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
        ApplicationController.getDefaultInstance().getConnectionHandler().sendMessage("ADD CONCEPT!");
        ApplicationController.getDefaultInstance().getColemoPanel().addNewConcept(ApplicationController.getDefaultInstance().getGraphicsDiagram().getUmlDiagram(), "c");


        /*String name = JOptionPane.showInputDialog(this, "Please type name of new concept:");
        if (name != null) {
            try {
                if (!ApplicationController.getDefaultInstance().getGraphicsDiagram().getUmlDiagram().nameExist(name)) {
                    InetAddress address = null;
                    Person person = null;
                    AddClass addClass = new AddClass(name, "c", "", address, person);
                    ApplicationController.getDefaultInstance().getConnectionHandler().sendObject(addClass);
                } else {
                    JOptionPane.showMessageDialog(null, "This concept already exists!");
                }
            } catch (ClassNameAlreadyExistException e) {
                e.printStackTrace();
            }
        } */
    }
}
