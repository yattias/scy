package eu.scy.scymapper.impl.shapes.links;

import eu.scy.scymapper.api.shapes.ILinkShape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 03.jun.2009
 * Time: 13:26:09
 */
public class Arrow implements ILinkShape {
    private boolean bidirectional;

    private Point from;
    private Point to;

    public Arrow() {
    }

    @Override
    public Shape getShape(Point from, Point to) {
        return new Arrow(from, to, false).getShape();
    }

    public Arrow(Point from, Point to, boolean bidirectional) {
        this.from = from;
        this.to = to;
        this.bidirectional = bidirectional;
    }

    public Shape getShape() {

        Line line = new Line(from, to);

        GeneralPath gp = new GeneralPath();
        gp.append(line.getShape(), false);

        Arrowhead head = new Arrowhead(25, Math.PI/3);
        head.setRotation(line.getAngle());
        AffineTransform at = AffineTransform.getTranslateInstance(to.x, to.y);
        gp.append(at.createTransformedShape(head.getShape()), false);

        if (bidirectional) {
            Arrowhead tail = new Arrowhead(25, Math.PI/3);
            tail.setRotation(line.getAngle()+Math.PI);
            at = AffineTransform.getTranslateInstance(from.x, from.y);
            gp.append(at.createTransformedShape(tail.getShape()), false);
        }
        return gp;

    }
}
