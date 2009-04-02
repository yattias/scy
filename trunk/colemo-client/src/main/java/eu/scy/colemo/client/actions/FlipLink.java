package eu.scy.colemo.client.actions;

import eu.scy.colemo.client.ConceptLink;
import eu.scy.colemo.client.SelectionController;
import eu.scy.colemo.client.ConceptNode;
import eu.scy.colemo.client.ApplicationController;

import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 30.mar.2009
 * Time: 08:42:58
 * To change this template use File | Settings | File Templates.
 */
public class FlipLink extends BaseAction{
    public FlipLink() {
        super("Flip link");
    }

    public Class getOperateson() {
        return ConceptLink.class;
    }

    protected void performAction(ActionEvent e) {
        ConceptLink link = (ConceptLink) SelectionController.getDefaultInstance().getCurrentPrimarySelection();
        ConceptNode fromNode = link.getFromNode();
        ConceptNode toNode = link.getToNode();

        link.setFromNode(toNode);
        link.setToNode(fromNode);

        ApplicationController.getDefaultInstance().getColemoPanel().revalidate();
        ApplicationController.getDefaultInstance().getConnectionHandler().updateObject(link.getModel());
    }
}
