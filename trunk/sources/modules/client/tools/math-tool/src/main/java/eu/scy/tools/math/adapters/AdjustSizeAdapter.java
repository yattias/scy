package eu.scy.tools.math.adapters;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.activation.MailcapCommandMap;
import javax.swing.JComponent;

import eu.scy.tools.math.shapes.IMathRectangle;
import eu.scy.tools.math.shapes.IMathShape;
import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.panels.IShapeCanvas;

public class AdjustSizeAdapter extends MouseAdapter {

	private int position = 0;
	final int PROX_DIST = 3;
	private IShapeCanvas shapeCanvas;
	private IMathShape foundShape;

	public AdjustSizeAdapter(IShapeCanvas shapeCanvas) {
		this.shapeCanvas = shapeCanvas;
		((JComponent) this.shapeCanvas).addMouseListener(this);
		((JComponent) this.shapeCanvas).addMouseMotionListener(this);
	}

	@Override
	public void mousePressed(MouseEvent event) {
		System.out.println("AdjustSizeAdapter.mousePressed()");
		Point eventPoint = event.getPoint();
		int x = eventPoint.x;
		int y = eventPoint.y;

		ArrayList<IMathShape> mathShapes = this.shapeCanvas.getMathShapes();
		for (IMathShape shape : mathShapes) {
			if (shape instanceof IMathRectangle) {
				int i = shape.isHitOnEndPoints(new Point(x, y));
				if (i != -1) {
					System.out.println("adjust contains found it!! "
							+ foundShape);
					foundShape = shape;
					position = i;
					return;
				}
			}
		}
	}

	// ((JComponent)mathRectangle).requestFocusInWindow();
	//
	// if( !mathRectangle.isShowCornerPoints() ) {
	// mathRectangle.setShowCornerPoints(true);
	// mathRectangle.repaint();
	// } else {
	// Point eventPoint = event.getPoint();
	//
	// position = mathRectangle.isHitOnEndPoints(eventPoint);
	// }

	public void mouseReleased(MouseEvent event) {
		position = -1;
	}

	public void mouseDragged(MouseEvent event) {
		if (foundShape == null)
			return;
		System.out.println("AdjustSizeAdapter.mouseDragged()");
		System.out.println("changing position " + position);
		if (position == -1)
			return;

		Point eventPoint = event.getPoint();

		Point[] points = foundShape.getPoints();
		points[position] = event.getPoint();
		foundShape.repaint();
		((JComponent) this.shapeCanvas).repaint();

	}

	@Override
	public void mouseMoved(MouseEvent event) {

		// if( !foundShape.isShowCornerPoints() )
		// return;

		if (foundShape == null)
			return;

		Point p = event.getPoint();

		if (foundShape instanceof IMathRectangle) {
			if (!isOverRect(p, foundShape)) {
				if (((JComponent) this.shapeCanvas).getCursor() != Cursor
						.getDefaultCursor()) {
					// If cursor is not over rect reset it to the default.
					((JComponent) this.shapeCanvas).setCursor(Cursor
							.getDefaultCursor());
				}
				return;
			}

			// Locate cursor relative to center of rect.
			int outcode = getOutcode(p, foundShape);
			Rectangle r = (Rectangle) foundShape;
			switch (outcode) {
			case Rectangle.OUT_BOTTOM + Rectangle.OUT_RIGHT:
				if (Math.abs(p.x - (r.x + r.width)) < PROX_DIST
						&& Math.abs(p.y - (r.y + r.height)) < PROX_DIST) {
					((JComponent) this.shapeCanvas).setCursor(Cursor
							.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
				}
				break;
			default: // center
				((JComponent) this.shapeCanvas).setCursor(Cursor
						.getDefaultCursor());
			}

		}

	}

	/**
	 * Make a smaller Rectangle and use it to locate the cursor relative to the
	 * Rectangle center.
	 */
	private int getOutcode(Point p, IMathShape mathShape) {
		Rectangle r = (Rectangle) ((Rectangle) mathShape).clone();
		r.grow(-PROX_DIST, -PROX_DIST);
		return r.outcode(p.x, p.y);
	}

	/**
	 * Make a larger Rectangle and check to see if the cursor is over it.
	 */
	private boolean isOverRect(Point p, IMathShape mathShape) {
		Rectangle r = (Rectangle) ((Rectangle) mathShape).clone();
		r.grow(PROX_DIST, PROX_DIST);
		return r.contains(p);
	}

}
