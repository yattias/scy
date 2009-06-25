package eu.scy.colemo.client.ui.impl.component;

import eu.scy.colemo.client.ui.api.links.ILink;
import eu.scy.colemo.client.ui.api.links.ILinkObserver;
import eu.scy.colemo.client.ui.api.links.ILinkController;
import eu.scy.colemo.client.ui.api.styling.ILinkStyle;
import eu.scy.colemo.client.ui.impl.model.DefaultLinkStyle;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 23.jun.2009
 * Time: 16:23:37
 */
public class LinkView extends Container implements ILinkObserver {

    static final ILinkStyle DEFAULT_LINKSTYLE = new DefaultLinkStyle();

    protected ILinkController controller;
    protected ILink model;

    private int minWidth = 100;
    private int minHeight = 100;

    public LinkView(ILinkController controller, ILink model) {
        this.controller = controller;
        this.model = model;

        // I want to observe changes in my model
        this.model.addObserver(this);

        setLayout(null);

    }

    public void paint(Graphics g) {

        Point from = model.getFrom();
        Point to = model.getTo();

        if (from == null || to == null) return;
        Point relFrom = new Point(from);
        relFrom.translate(-getX(), -getY());

        Point relTo = new Point(to);
        relTo.translate(-getX(), -getY());

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ILinkStyle style = model.getStyle();

        g2.setStroke(style.getStroke());
        g2.setColor(style.getColor());
        g2.draw(model.getShape().getShape(relFrom, relTo));

        g2.dispose();

        // Continue painting other component on top
        super.paint(g);

    }

    public void setFrom(Point p) {
        controller.setFrom(p);
    }
    public void setTo(Point p) {
        controller.setTo(p);
    }

    void updatePosition() {
        Point from = model.getFrom();
        Point to = model.getTo();
        if (from == null || to == null) return;

        int w, x, h, y;
        if (to.x < from.x) {
            x = to.x;
            w = from.x - to.x;
        } else {
            x = from.x;
            w = to.x - from.x;
        }
        if (to.y < from.y) {
            y = to.y;
            h = from.y - to.y;
        } else {
            y = from.y;
            h = to.y - from.y;
        }

        setBounds(x - minWidth / 2, y - minHeight / 2, w + minWidth, h + minHeight);
    }


    @Override
    public void updated(ILink m) {
        updatePosition();
    }
}
