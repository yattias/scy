package eu.scy.scymapper.impl.shapes.links;

import eu.scy.scymapper.api.shapes.ILinkShape;

import java.awt.*;
import java.awt.geom.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 03.jun.2009
 * Time: 13:26:09
 */
public class Arrow implements ILinkShape {
	private Arrowhead arrowhead = new Arrowhead();
	private Arrowhead tail;
	private ILinkShape lineShape = new Line();

	public Arrow() {
    }

	public void setLineShape(ILinkShape lineShape) {
		this.lineShape = lineShape;
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

	@Override
	public Point2D getDeCasteljauPoint(Point2D from, Point2D to, double param) {
		return lineShape.getDeCasteljauPoint(from, to, param);
	}


	private double getLength(Shape shape) {
		double flatness = 0.5;
		double[] coords = new double[6];
		Point2D.Double prev = null;
		PathIterator pit = shape.getPathIterator(null, flatness);

		double length = 0d;
		do {
			pit.currentSegment(coords);

			Point2D.Double curr = new Point2D.Double(coords[0], coords[1]);

			if (prev != null) {
				length += Point2D.distance(prev.x, prev.y, coords[0], coords[1]);
			}
			prev = curr;
			pit.next();
		} while (!pit.isDone());

		return length;
	}
	private GeneralPath printSegments(Shape shape, double flatness) {
		GeneralPath gp = new GeneralPath();
		double[] coords = new double[6];
		Point2D.Double prev = null;
		PathIterator pit = shape.getPathIterator(null, flatness);

		double length = 0d;
		int counter = 0;
		while (!pit.isDone()) {
			pit.currentSegment(coords);

			Point2D.Double curr = new Point2D.Double(coords[0], coords[1]);

			if (prev != null) {
				length += Point2D.distance(prev.x, prev.y, coords[0], coords[1]);
				gp.append(new Line2D.Double(prev, curr), true);
			}
			prev = curr;
			pit.next();
			counter++;
		}

		return gp;
	}

	@Override
    public Shape getShape(Point2D from, Point2D to) {

        GeneralPath gp = new GeneralPath();
		//gp.append(printSegments(lineShape.getShape(from, to), 0.1), false);
        gp.append(lineShape.getShape(from, to), false);

		if (arrowhead != null) {
			double dist = getLength(gp);
//			double lineDist = from.distance(to);
//			System.out.println("lineDist = " + lineDist);
//			System.out.println("dist = " + dist);

			double h = (Math.sqrt((arrowhead.getLength()*arrowhead.getLength())+(arrowhead.getLength()*arrowhead.getLength()))/2d);

			double fact = (1d/dist)*h;

			Point2D p = getDeCasteljauPoint(from, to, 1-fact);

//			gp.append(new Ellipse2D.Double(p.getX(), p.getY(), 1, 1), false);

			arrowhead.setRotation(Line.getAngle(p, to));
			AffineTransform at = AffineTransform.getTranslateInstance(to.getX(), to.getY());
			gp.append(at.createTransformedShape(arrowhead.getShape()), false);

			if (isBidirectional()) {

				p = getDeCasteljauPoint(from, to, fact);
//				gp.append(new Ellipse2D.Double(p.getX(), p.getY(), 1, 1), false);

				tail.setRotation(Line.getAngle(from, p)+3d*Math.PI);

				at = AffineTransform.getTranslateInstance(from.getX(), from.getY());
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
