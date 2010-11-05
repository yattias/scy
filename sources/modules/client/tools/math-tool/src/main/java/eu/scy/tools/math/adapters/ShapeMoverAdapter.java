package eu.scy.tools.math.adapters;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JComponent;

import eu.scy.tools.math.shapes.IMathRectangle;
import eu.scy.tools.math.shapes.IMathShape;
import eu.scy.tools.math.ui.panels.IShapeCanvas;
import eu.scy.tools.math.ui.panels.TestPainter;

public class ShapeMoverAdapter extends MouseAdapter {

	int preX, preY;

	boolean isFirstTime = true;
	Rectangle area;
	Point start;
	boolean pressOut = false;

	private int y;

	private int x;

	private boolean isMoving = false;

	private IShapeCanvas shapeCanvas;
	
	private IMathShape foundShape;

	public ShapeMoverAdapter(IShapeCanvas shapeCanvas) {
		this.shapeCanvas = shapeCanvas;
		((JComponent) this.shapeCanvas).addMouseListener(this);
		((JComponent) this.shapeCanvas).addMouseMotionListener(this);
	}

	public void mousePressed(MouseEvent e) {
		System.out.println("ShapeMoverAdapter.mousePressed()");
		x = e.getX();
		y = e.getY();

	
		
		ArrayList<IMathShape> mathShapes = this.shapeCanvas.getMathShapes();
		

		Collections.reverse(mathShapes);
		for (IMathShape shape : mathShapes) {
			Point point = new Point(x, y);
			int hitOnEndPoints = shape.isHitOnEndPoints(point);
			if ( hitOnEndPoints != -1 || shape.contains(point)) {
				System.out.println(shape.toString());
				foundShape = shape;
				foundShape.setShowCornerPoints(true);
					if (hitOnEndPoints == -1) {
						System.out.println("moving adapter found shape  off corner" +shape.toString());
						
						
						
						isMoving = true;
						break;
					} else {
						System.out.println("moving adapter found shape ON corner" +shape.toString());
						isMoving = false;
						break;
					}
			}
		}
		
		Collections.reverse(mathShapes);
		//unshow corner points except the selected one.
		if( foundShape != null ) {
			for (IMathShape shape : mathShapes) {
				if( !shape.equals(foundShape) )
					shape.setShowCornerPoints(false);
			}
		//bump it up
		int i = mathShapes.indexOf(foundShape);
		mathShapes.remove(i);
		mathShapes.add(foundShape);
		((JComponent) this.shapeCanvas).repaint();
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("ShapeMoverAdapter.mouseReleased()");
		foundShape = null;
//		if( foundShape != null ) {
//			foundShape.setShowCornerPoints(false);
//			foundShape.repaint();
//			((JComponent) this.shapeCanvas).repaint();
//
//		}
	}
	public void mouseDragged(MouseEvent e) {
		System.out.println("ShapeMoverAdapter.mouseDragged()");
		if( !isMoving )
			return;
		
		int dx = e.getX() - x;
		int dy = e.getY() - y;

		if( foundShape != null ) {
			
			foundShape.addX(dx);
			foundShape.addY(dy);
			foundShape.repaint();
			((JComponent) this.shapeCanvas).repaint();
		}
		x += dx;
		y += dy;
	}

}
