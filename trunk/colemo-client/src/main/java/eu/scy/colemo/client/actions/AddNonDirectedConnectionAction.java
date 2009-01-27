package eu.scy.colemo.client.actions;

import eu.scy.colemo.client.ApplicationController;
import eu.scy.colemo.client.GraphicsAssociation;
import eu.scy.colemo.client.ConceptNode;
import eu.scy.colemo.client.SelectionController;
import eu.scy.colemo.server.uml.UmlAssociation;
import eu.scy.colemo.server.uml.UmlClass;

import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 26.nov.2008
 * Time: 06:23:38
 * To change this template use File | Settings | File Templates.
 */
public class AddNonDirectedConnectionAction extends BaseAction implements DoubleSelectAction {

      public Class getOperateson() {
        return ConceptNode.class;
    }

    public void actionPerformed(ActionEvent e) {
        if (SelectionController.getDefaultInstance().getSelectModus().equals(SelectionController.DOUBLE_SELECT_MODUS)) {
            SelectionController.getDefaultInstance().setSelectModus(SelectionController.SINGLE_SELECT_MODUS);
            SelectionController.getDefaultInstance().setCurrentDoubleSelectAction(null);
        } else {
            SelectionController.getDefaultInstance().setSelectModus(SelectionController.DOUBLE_SELECT_MODUS);
            SelectionController.getDefaultInstance().setCurrentDoubleSelectAction(this);
        }

    }

    protected void performAction(ActionEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void performDoubleSelectAction(ActionEvent e) {
        log.info("PERFORMING DOUBLE SELECT ACTION NOW!");
        log.info("READY TO FIRE DOUBLE SELECT ACTION!");
        UmlClass from = (UmlClass) SelectionController.getDefaultInstance().getCurrentSecondarySelection();
        UmlClass to = (UmlClass) SelectionController.getDefaultInstance().getCurrentPrimarySelection();
        UmlAssociation link = new UmlAssociation(from.getName(), to.getName(), "Henrik");

        GraphicsAssociation gLink = new GraphicsAssociation(link, ApplicationController.getDefaultInstance().getGraphicsDiagram());
        //ApplicationController.getDefaultInstance().getMainFrame().getGraphicsDiagram().addLink(link);
        throw new RuntimeException("NOT IMPLEMENTED");
        /*ApplicationController.getDefaultInstance().getMainFrame().getClient().send(link);

        AssociateClass associateClass = new AssociateClass(from.getName(), to.getName(), "Henrik", ApplicationController.getDefaultInstance().getMainFrame().getClient().getConnection().getSocket().getLocalAddress(), ApplicationController.getDefaultInstance().getMainFrame().getClient().getPerson());
        ApplicationController.getDefaultInstance().getClient().getConnection().send(associateClass);
        */
    }

}
