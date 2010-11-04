package eu.scy.tools.math.shapes;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JComponent;

import eu.scy.tools.math.ui.panels.IShapeCanvas;
import eu.scy.tools.math.ui.panels.TestPainter;

public class ShapeMoverAdapter extends MouseAdapter {

	private IMathShape shape;

	int preX, preY;

	boolean isFirstTime = true;
	Rectangle area;
	Point start;
	boolean pressOut = false;

	private int y;

	private int x;

	private boolean dragging = false;

	private IShapeCanvas shapeCanvas;

	public ShapeMoverAdapter(IShapeCanvas shapeCanvas) {
		this.shapeCanvas = shapeCanvas;
		((JComponent) this.shapeCanvas).addMouseListener(this);
		((JComponent) this.shapeCanvas).addMouseMotionListener(this);
	}

	public void mousePressed(MouseEvent e) {
		System.out.println("ShapeMoverAdapter.mousePressed()");
		x = e.getX();
		y = e.getY();


	}

	public void mouseDragged(MouseEvent e) {
		System.out.println("ShapeMoverAdapter.mouseDragged()");
			int dx = e.getX() - x;
			int dy = e.getY() - y;

			ArrayList<IMathShape> mathShapes = this.shapeCanvas.getMathShapes();
			for (IMathShape shape : mathShapes) {
				Point point = new Point(x, y);
				if (shape.contains(point)) {
					if( shape.isHitOnEndPoints(point) == -1 ) {
					System.out.println("contains");
					shape.addX(dx);
					shape.addY(dy);
					System.out.println(shape.toString());
					shape.repaint();
					((JComponent) this.shapeCanvas).repaint();
					}
				}
			}

			x += dx;
			y += dy;
	}

}
