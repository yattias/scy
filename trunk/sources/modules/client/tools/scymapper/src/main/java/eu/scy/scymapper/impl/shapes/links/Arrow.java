package eu.scy.scymapper.impl.shapes.links;

import eu.scy.scymapper.api.shapes.ILinkShape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 03.jun.2009
 * Time: 13:26:09
 */
public class Arrow implements ILinkShape {
    private boolean bidirectional;

	private Arrowhead arrowhead;

	public Arrow() {
    }

	public void setArrowhead(Arrowhead arrowhead) {
		this.arrowhead = arrowhead;
	}

    public Arrow(boolean bidirectional) {
        this.bidirectional = bidirectional;
    }

    public Shape getShape(Point from, Point to) {

        Line line = new Line();

        GeneralPath gp = new GeneralPath();
        gp.append(line.getShape(from, to), false);

		Point2D from2d = new Point2D.Double(from.x, from.y);
		Point2D to2d = new Point2D.Double(to.x, to.y);

		if (arrowhead != null) {
			arrowhead.setRotation(Line.getAngle(from, to));
			AffineTransform at = AffineTransform.getTranslateInstance(to.x, to.y);
			gp.append(at.createTransformedShape(arrowhead.getShape()), false);

			if (bidirectional) {
				Arrowhead tail = new Arrowhead();
				tail.setRotation(Line.getAngle(from , to)+Math.PI);
				at = AffineTransform.getTranslateInstance(from.x, from.y);
				gp.append(at.createTransformedShape(tail.getShape()), false);
			}
		}
        return gp;
    }

	@Override
	public void paint(Graphics g, Point from, Point to) {
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.draw(getShape(from, to));
	}

	public void setBidirectional(boolean bidirectional) {
		this.bidirectional = bidirectional;
	}
}
