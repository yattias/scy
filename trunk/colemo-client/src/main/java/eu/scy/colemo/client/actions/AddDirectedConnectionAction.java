package eu.scy.colemo.client.actions;

import eu.scy.colemo.client.actions.BaseAction;
import eu.scy.colemo.client.SelectionController;
import eu.scy.colemo.client.GraphicsClass;
import eu.scy.colemo.client.GraphicsLink;
import eu.scy.colemo.client.ApplicationController;
import eu.scy.colemo.server.uml.UmlClass;
import eu.scy.colemo.server.uml.UmlLink;
import eu.scy.colemo.contributions.AddLink;

import java.awt.event.ActionEvent;

import eu.scy.colemo.client.actions.DoubleSelectAction;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 21.nov.2008
 * Time: 14:13:01
 * To change this template use File | Settings | File Templates.
 */
public class AddDirectedConnectionAction extends BaseAction implements DoubleSelectAction {
    public Class getOperateson() {
        return GraphicsClass.class;
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
        UmlLink link = new UmlLink(from.getName(), to.getName(), "Henrik");

        GraphicsLink gLink = new GraphicsLink(link, ApplicationController.getDefaultInstance().getGraphicsDiagram());
        ApplicationController.getDefaultInstance().getColemoPanel().getGraphicsDiagram().addLink(link);
        //ApplicationController.getDefaultInstance().getMainFrame().getClient().send(link);

        //AddLink addLink = new AddLink(from.getName(), to.getName(), "Henrik", ApplicationController.getDefaultInstance().getMainFrame().getClient().getConnection().getSocket().getLocalAddress(), ApplicationController.getDefaultInstance().getMainFrame().getClient().getPerson());
        //ApplicationController.getDefaultInstance().getClient().getConnection().send(addLink);
    }
}
