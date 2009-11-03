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
	private Arrowhead arrowhead = new Arrowhead();
	private Arrowhead tail;

	public Arrow() {
    }

	public void setHead(Arrowhead arrowhead) {
		this.arrowhead = arrowhead;
	}

	public void setTail(Arrowhead tail) {
		this.tail = tail;
	}

	public Arrowhead getTail() {
		return tail;
	}

    boolean isBidirectional() {
        return tail != null;
    }

    public Shape getShape(Point from, Point to) {

        Line line = new Line();

        GeneralPath gp = new GeneralPath();
        gp.append(line.getShape(from, to), false);

		if (arrowhead != null) {
			arrowhead.setRotation(Line.getAngle(from, to));
			AffineTransform at = AffineTransform.getTranslateInstance(to.x, to.y);
			gp.append(at.createTransformedShape(arrowhead.getShape()), false);

			if (isBidirectional()) {
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
        g2d.dispose();
	}
}
