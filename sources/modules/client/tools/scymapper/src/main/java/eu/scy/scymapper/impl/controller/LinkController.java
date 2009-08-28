package eu.scy.scymapper.impl.controller;

import eu.scy.scymapper.api.diagram.ILinkController;
import eu.scy.scymapper.api.diagram.ILinkModel;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 20:00:05
 * To change this template use File | Settings | File Templates.
 */
public class LinkController implements ILinkController {
    private ILinkModel model;

    public LinkController(ILinkModel link) {
        this.model = link;
    }

    @Override
    public void setLabel(String text) {
        model.setLabel(text);
    }

    @Override
    public void setTo(Point p) {
        model.setTo(p);
    }

    @Override
    public void setFrom(Point p) {
        model.setFrom(p);
    }
}
