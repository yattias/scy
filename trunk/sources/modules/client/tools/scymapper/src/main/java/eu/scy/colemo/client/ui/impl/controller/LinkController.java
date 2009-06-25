package eu.scy.colemo.client.ui.impl.controller;

import eu.scy.colemo.client.ui.api.links.ILinkController;
import eu.scy.colemo.client.ui.api.links.ILink;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 22.jun.2009
 * Time: 20:00:05
 * To change this template use File | Settings | File Templates.
 */
public class LinkController implements ILinkController {
    private ILink model;

    public LinkController(ILink link) {
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
