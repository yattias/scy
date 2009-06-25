package eu.scy.colemo.client.ui.impl.controller;
import eu.scy.colemo.client.ui.api.nodes.INodeController;
import eu.scy.colemo.client.ui.api.nodes.IConceptNode;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 22.jun.2009
 * Time: 20:03:51
 * To change this template use File | Settings | File Templates.
 */
public class NodeController implements INodeController {
    private IConceptNode model;

    public NodeController(IConceptNode node) {
        this.model = node;
    }

    @Override
    public void setSize(Dimension p) {
        model.setSize(p);
    }

    @Override
    public void setLocation(Point p) {
        model.setLocation(p);
    }

    @Override
    public void setLabel(String text) {
        model.setLabel(text);
    }

    @Override
    public void setSelected(boolean b) {
        model.setSelected(true);
    }
}
