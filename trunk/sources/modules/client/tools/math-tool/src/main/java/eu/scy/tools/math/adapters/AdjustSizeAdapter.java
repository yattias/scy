package eu.scy.tools.math.adapters;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import eu.scy.tools.math.controller.MathToolController;
import eu.scy.tools.math.shapes.IMathEllipse;
import eu.scy.tools.math.shapes.IMathRectangle;
import eu.scy.tools.math.shapes.IMathShape;
import eu.scy.tools.math.shapes.IMathTriangle;
import eu.scy.tools.math.ui.panels.IShapeCanvas;
import eu.scy.tools.math.ui.panels.ShapeCanvas;

public class AdjustSizeAdapter extends MouseAdapter {

	private int position = 0;
	final int PROX_DIST = 3;
	private IShapeCanvas shapeCanvas;
	private IMathShape foundShape;
	private int x;
	private int y;
	private MathToolController mathToolController;
	
	public AdjustSizeAdapter(IShapeCanvas shapeCanvas) {
		this.shapeCanvas = shapeCanvas;
		((JComponent) this.shapeCanvas).addMouseListener(this);
		((JComponent) this.shapeCanvas).addMouseMotionListener(this);
	}

	public AdjustSizeAdapter(MathToolController mathToolController, String type) {
		this.mathToolController = mathToolController;
		Map<String, ShapeCanvas> shapeCanvases = this.mathToolController.getShapeCanvases();
		this.shapeCanvas = shapeCanvases.get(type);
		((JComponent) this.shapeCanvas).addMouseListener(this);
		((JComponent) this.shapeCanvas).addMouseMotionListener(this);
	}

	@Override
	public void mousePressed(MouseEvent event) {
//		System.out.println("AdjustSizeAdapter.mousePressed()");
		Point eventPoint = event.getPoint();
		x = eventPoint.x;
		y = eventPoint.y;

		ArrayList<IMathShape> mathShapes = this.shapeCanvas.getMathShapes();
		for (IMathShape shape : mathShapes) {
				int i = shape.isHitOnEndPoints(event.getPoint());
				if (i != -1) {
//					System.out.println("adjust contains found it!! "
//							+ foundShape);
					foundShape = shape;
					
					if( mathToolController != null) {
						mathToolController.setSelectedMathShape(foundShape);
//						System.out.println("AdjustSizeAdapter.mousePressed() SELECTED");
					}
					position = i;
					return;
			}
		}
	}

	public void mouseReleased(MouseEvent event) {
		foundShape = null;
		position = -1;
	}

	public void mouseDragged(MouseEvent event) {
		if (foundShape == null)
			return;
//		System.out.println("AdjustSizeAdapter.mouseDragged()");
//		System.out.println("changing position " + position);
		if (position == -1)
			return;

		Point eventPoint = event.getPoint();

		if( foundShape instanceof IMathEllipse) {
			Point[] points = foundShape.getPoints();		
			points[position] = event.getPoint();
//			System.out.println("position dragging " + position);
			
			 int dx = event.getX() - x;
	         int dy = event.getY() - y;
	         ((IMathEllipse) foundShape).addHeight(dx);
	         ((IMathEllipse) foundShape).addWidth(dx);
//	         System.out.println(foundShape.toString());
	         x += dx;
	         y += dy;
		} else if( foundShape instanceof IMathTriangle) {
			((IMathTriangle) foundShape).moveCornerPoint(position,event.getPoint());
		} else if( foundShape instanceof IMathRectangle ) {
			Point[] points = foundShape.getPoints();		
			points[position] = event.getPoint();
//			System.out.println("position dragging " + position);
			
		}
		foundShape.repaint();
		((JComponent) this.shapeCanvas).repaint();

	}

	@Override
	public void mouseMoved(MouseEvent event) {

//		System.out.println("AdjustSizeAdapter.mouseMoved()");
		// if( !foundShape.isShowCornerPoints() )
		// return;

		Point p = event.getPoint();
		
		if (foundShape == null) {
//			System.out.println("no shape found reset cursor");
			// If cursor is not over rect reset it to the default.
			((JComponent) this.shapeCanvas).setCursor(Cursor
					.getDefaultCursor());
			return;
		}
		

		ArrayList<IMathShape> mathShapes = this.shapeCanvas.getMathShapes();
		for (IMathShape shape : mathShapes) {
			
				if( shape.isHitOnEndPoints(p) != -1) {
					((JComponent) this.shapeCanvas).setCursor(Cursor
							.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
				}
			
			
		}
			
		

		

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

		} else if(foundShape instanceof IMathEllipse ) {
			if (foundShape.contains(p)) {
				if (((JComponent) this.shapeCanvas).getCursor() != Cursor
						.getDefaultCursor()) {
					// If cursor is not over rect reset it to the default.
					((JComponent) this.shapeCanvas).setCursor(Cursor
							.getDefaultCursor());
				}
				return;
			} else if(foundShape.isHitOnEndPoints(p) != -1) {
				((JComponent) this.shapeCanvas).setCursor(Cursor
						.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
				return;
			}
			
			
		} else if( foundShape instanceof IMathTriangle) {
			if (foundShape.contains(p)) {
				if (((JComponent) this.shapeCanvas).getCursor() != Cursor
						.getDefaultCursor()) {
					// If cursor is not over rect reset it to the default.
					((JComponent) this.shapeCanvas).setCursor(Cursor
							.getDefaultCursor());
				}
				return;
			} else if(foundShape.isHitOnEndPoints(p) == 1) {
				((JComponent) this.shapeCanvas).setCursor(Cursor
						.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
				return;
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
