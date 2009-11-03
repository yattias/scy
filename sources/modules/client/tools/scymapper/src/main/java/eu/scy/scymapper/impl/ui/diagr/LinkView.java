package eu.scy.scymapper.impl.ui.diagr;

import eu.scy.scymapper.api.diagram.ILinkController;
import eu.scy.scymapper.api.diagram.ILinkModel;
import eu.scy.scymapper.api.diagram.ILinkModelListener;
import eu.scy.scymapper.api.styling.ILinkStyle;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 23.jun.2009
 * Time: 16:23:37
 */
public class LinkView extends JComponent implements ILinkModelListener {

    private ILinkController controller;
    private ILinkModel model;

    private int minWidth = 100;
    private int minHeight = 100;

    private final static Logger logger = Logger.getLogger(LinkView.class);

    public LinkView(ILinkController controller, ILinkModel model) {
        this.controller = controller;
        this.model = model;

        // I want to observe changes in my model
        this.model.addListener(this);

        setLayout(null);
        updatePosition();
    }

    public void paint(Graphics g) {

		g = g.create();

        Point from = model.getFrom();
        Point to = model.getTo();

        if (from == null || to == null) {
            logger.warn("From or to is null");
            return;
        }
        Point relFrom = new Point(from);
        relFrom.translate(-getX(), -getY());

        Point relTo = new Point(to);
        relTo.translate(-getX(), -getY());

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ILinkStyle style = model.getStyle();

        g2.setStroke(style.getStroke());

        g2.setColor(model.isSelected() ? style.getSelectionColor() : style.getColor());

        model.getShape().paint(g2, relFrom, relTo);

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
    public void updated(ILinkModel m) {
        updatePosition();
		repaint();
    }

    @Override
    public void selectionChanged(ILinkModel node) {
        System.out.println("LinkView.selectionChanged");
        //setBorder(BorderFactory.createLineBorder(Color.blue, 1));
        repaint();
    }

    public ILinkModel getModel() {
        return model;
    }

    public void setModel(ILinkModel model) {
        this.model = model;
    }

    public ILinkController getController() {
        return controller;
    }
}
