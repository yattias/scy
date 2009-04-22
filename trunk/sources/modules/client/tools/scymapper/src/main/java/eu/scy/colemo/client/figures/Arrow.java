package eu.scy.colemo.client.figures;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 02.apr.2009
 * Time: 15:41:46
 * To change this template use File | Settings | File Templates.
 */
public class Arrow extends JComponent {
	public static final int STYLE_SOLID    = 0;
	public static final int STYLE_DOTTED   = 1;
	public static final int STYLE_DASHED   = 2;
	private int lineStyle = STYLE_SOLID;
	private Point from = new Point(0, 0);
	private Point to = new Point(100, 100);
	private boolean bidirectional = false;
	private Color color;

	// directions - should be able to get the opposite direction by multiplying with -1
	public static final int NORTH = 2;
	public static final int WEST = 1;
	public static final int EAST = -1;
	public static final int SOUTH = -2;
	public static final int NORTHWEST = 3;
	public static final int NORTHEAST = 4;
	public static final int SOUTHWEST = -4;
	public static final int SOUTHEAST = -3;
	private int minWidth = 0;
	private int minHeight = 0;

	public double getDistanceFromLine(Point pt) {

		double dx = pt.x - from.x;
		double dy = pt.y - from.y;

		if (dx == 0)
			return dx;

		double fromtoAngle = Math.toRadians(getAngle());
		double pointAngle = Math.atan(dy / dx);
		return Math.abs(Math.sqrt(dx * dx + dy * dy) * Math.sin(fromtoAngle - pointAngle));
	}

	public Shape getArrow() {
		GeneralPath path = new GeneralPath();

		float diag = ((float) Math.sqrt(Math.pow(getInnerHeight(), 2) + Math.pow(getInnerWidth(), 2)));

		float[] start = new float[]{diag / 2f, 0};
		float[] end = new float[]{diag / -2f, 0};

		path.moveTo(start[0], start[1]);
		path.lineTo(end[0], end[1]);

		// Draw the arrow heads
		int x, y;

		// degrees between head and shaft
		float deg = 45;

		int size = 10;

		// Create the first arrow head line.
		x = (int) end[0] + size;
		y = (int) (size * Math.sin(deg));
		path.lineTo(x, y);

		// Move to line-end
		path.moveTo(end[0], end[1]);

		// and create the second
		y = (int) (size * Math.sin(-deg));
		path.lineTo(x, y);

		if (bidirectional) {
			// Move to start
			path.moveTo(start[0], start[1]);
			x = (int) start[0] - size;
			y = (int) (size * Math.sin(deg));
			path.lineTo(x, y);
			path.moveTo(start[0], start[1]);
			y = (int) (size * Math.sin(-deg));
			path.lineTo(x, y);
		}
		AffineTransform at = AffineTransform.getTranslateInstance(getWidth() / 2, getHeight() / 2);
		at.rotate(Math.toRadians(getAngle()));
		return at.createTransformedShape(path);
	}

	protected void setMinWidth(int mw) {
		if (mw > minWidth) minWidth = mw;
	}
	protected void setMinHeight(int mh) {
		if (mh > minHeight) minHeight = mh;
	}

	private void update() {
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
		setBounds(x - (minWidth / 2) - 10, y - (minHeight / 2) - 10, w + minWidth + 20, h + minHeight + 20);
		repaint();
	}

	public final void setFrom(Point p) {
		from = p;
		update();
	}

	public final void setTo(Point p) {
		to = p;
		update();
	}

	public boolean isBidirectional() {
		return bidirectional;
	}

	public void setBidirectional(boolean bidirectional) {
		this.bidirectional = bidirectional;
	}

	private int getInnerWidth() {
		return Math.abs(from.x - to.x);
	}

	private int getInnerHeight() {
		return Math.abs(from.y - to.y);
	}
	public double getAngle() {
		double dx = from.getX() - to.getX();
		double dy = from.getY() - to.getY();
		double angle;

		if (dx == 0.0) {
			if (dy == 0.0) angle = 0.0;
			else if (dy > 0.0) angle = Math.PI / 2.0;
			else angle = (Math.PI * 3.0) / 2.0;
		} else if (dy == 0.0) {
			if (dx > 0.0) angle = 0.0;
			else angle = Math.PI;
		} else {
			if (dx < 0.0) angle = Math.atan(dy / dx) + Math.PI;
			else if (dy < 0.0) angle = Math.atan(dy / dx) + (2 * Math.PI);
			else angle = Math.atan(dy / dx);
		}
		return (angle * 180) / Math.PI;
	}


	public int findDirection(Point from, Point to) {
		double x = to.x - from.x;
		double y = to.y - from.y;

		if (Math.abs(x) > Math.abs(y)) {
			if (from.getX() < to.getX()) {
				if (Math.abs(from.getY() - to.getY()) < 100) {
					return EAST;
				} else if (from.getY() > to.getY()) {
					return NORTHEAST;
				} else
					return SOUTHEAST;
			} else {
				if (Math.abs(from.getY() - to.getY()) < 100) {
					return WEST;
				} else if (from.getY() > to.getY()) {
					return NORTHWEST;
				} else
					return SOUTHWEST;
			}
		} else {
			if (from.getY() > to.getY()) {
				return NORTH;
			} else {
				return SOUTH;
			}
		}
	}
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(getColor());
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		switch (lineStyle) {
			case STYLE_SOLID:
				break;
			case STYLE_DOTTED:
				g2.setStroke(new BasicStroke(
						1f,
						BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND,
						1f,
						new float[]{2f},
						0f));
				break;
			case STYLE_DASHED:
				g2.setStroke(new BasicStroke(
						1f,
						BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND,
						1f,
						new float[]{6f},
						0f));
				break;
		}

		Shape arrow = getArrow();
		g2.draw(arrow);
		super.paint(g2);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setLineStyle(int i) {
		lineStyle = i;
	}
}
