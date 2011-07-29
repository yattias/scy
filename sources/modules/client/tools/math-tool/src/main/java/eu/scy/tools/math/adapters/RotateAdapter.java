package eu.scy.tools.math.adapters;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JComponent;

import eu.scy.tools.math.controller.MathToolController;
import eu.scy.tools.math.shapes.IMathRectangle;
import eu.scy.tools.math.shapes.IMathShape;
import eu.scy.tools.math.shapes.impl.MathRectangle;
import eu.scy.tools.math.ui.panels.ShapeCanvas;

public class RotateAdapter extends MouseAdapter {

	private ShapeCanvas shapeCanvas;
	private MathToolController mathToolController;
	private int x;
	private int y;
	private IMathShape foundShape;
	private int position;
	private int counter = 1;

	public RotateAdapter(ShapeCanvas shapeCanvas) {
		this.shapeCanvas = shapeCanvas;
		((JComponent) this.shapeCanvas).addMouseListener(this);
		((JComponent) this.shapeCanvas).addMouseMotionListener(this);
	}

	public RotateAdapter(MathToolController mathToolController, String type) {
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
		
		if( foundShape == null ){
			for (IMathShape shape : mathShapes) {
					if( shape.contains(eventPoint) ) {
						foundShape = shape;
						shapeCanvas.removeSelectedShape(shape);
						
						if( mathToolController != null) {
							mathToolController.setSelectedMathShape(foundShape);
							
						}
						return;
					}
			}
		}
		
		this.rotateShape(foundShape);
	}

	public void rotateShape(IMathShape mathShape) {
		 
				
				
				 AffineTransform  atx = AffineTransform.getRotateInstance (-counter,x,y);
		 
		 		counter = counter + 1;
			    // Take the shape object and create a rotated version
			    Shape shape = atx.createTransformedShape ((Shape) mathShape);
			    MathRectangle m = new MathRectangle(shape.getBounds2D().getX(), shape.getBounds2D().getY(), shape.getBounds2D().getWidth(), shape.getBounds2D().getHeight());
			    
//			    Graphics2D g2 = (Graphics2D) shapeCanvas.getGraphics();
//			    
//			    g2.draw(shape);
			   
//			    shapeCanvas.paintComponent(g2);
			    shapeCanvas.shapes.removeAll(shapeCanvas.shapes);
				shapeCanvas.addRegularShape(shape);
				
				((JComponent) this.shapeCanvas).repaint();
			   // g2.draw (atShape);
		
	}
	public void mouseDragged(MouseEvent event) {
//		if (foundShape == null)
//			return;
////		System.out.println("AdjustSizeAdapter.mouseDragged()");
////		System.out.println("changing position " + position);
//		if (position == -1)
//			return;
//
//		Point eventPoint = event.getPoint();
//
//		if( foundShape instanceof IMathTriangle) {
//			((IMathTriangle) foundShape).moveCornerPoint(position,event.getPoint());
//		} else if( foundShape instanceof IMathRectangle ) {
//			startMotion(eventPoint.x, eventPoint.y, (Shape) foundShape);
//			
//		}
//		foundShape.repaint();
//		((JComponent) this.shapeCanvas).repaint();

	}
	
	public boolean startMotion (double startX, double startY, Shape shape) {
		AffineTransform transform = new AffineTransform();
		Point2D.Double center;
		Rectangle2D bounds;

		//... find the nearest Shape object and assign it to the shape variable...
		bounds = shape.getBounds2D();
		
		System.out.println("old bounds: " + bounds);
		center = new Point2D.Double( bounds.getCenterX(), bounds.getCenterY() );

		transform.rotate( startX - center.x, startY - center.y );
		shape = transform.createTransformedShape( shape ); // shape is now a transformed copy. leave the
		MathRectangle m = new MathRectangle(shape.getBounds2D().getX(), shape.getBounds2D().getY(), shape.getBounds2D().getWidth(), shape.getBounds2D().getHeight());
		shapeCanvas.addShape(m);
		// original shape alone--to ensure that the
		// same corner/edge of the shape follows the
		// mouse--and save the copy somewhere for the paint() method.
		return true; // ?
		}

}
