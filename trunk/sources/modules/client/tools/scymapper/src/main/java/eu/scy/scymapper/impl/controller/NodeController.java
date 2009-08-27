package eu.scy.scymapper.impl.controller;
import eu.scy.scymapper.api.nodes.INodeController;
import eu.scy.scymapper.api.nodes.INode;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 20:03:51
 * To change this template use File | Settings | File Templates.
 */
public class NodeController implements INodeController {
    private INode model;

    public NodeController(INode node) {
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
