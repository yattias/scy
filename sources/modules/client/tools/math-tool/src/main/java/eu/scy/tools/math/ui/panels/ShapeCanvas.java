package eu.scy.tools.math.ui.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import eu.scy.tools.math.shapes.I3D;
import eu.scy.tools.math.shapes.IMathShape;
import eu.scy.tools.math.ui.UIUtils;

public class ShapeCanvas extends JPanel implements IShapeCanvas {

	private static Logger log = Logger.getLogger("ShapeCanvas.class"); //$NON-NLS-1$

	private boolean showGrid;
	private ArrayList<IMathShape> mathShapes = new ArrayList<IMathShape>();
	
	public ArrayList<Shape> shapes = new ArrayList<Shape>();
	
	private ControlPanel controlPanel;
	private String type;

	private boolean hasDecorations;

	private boolean hasCornerPoints;

	private boolean isScreenCaptureMode;

	public ShapeCanvas() {
		super(null);
		init();
	}

	public ShapeCanvas(boolean showGrid) {
		super(null);
		this.showGrid = showGrid;
		init();
	}

	private void init() {
		setDoubleBuffered(true);
	}

	public void setEnabledShapeDecorations(boolean hasDecorations) {
		this.hasDecorations = hasDecorations;
	}

	public void setEndabledShapeCornerPoints(boolean hasCornerPoints) {
		this.hasCornerPoints = hasCornerPoints;
	}

	public void setScreenCaptureMode(boolean isScreenCaptureMode) {
		this.isScreenCaptureMode = isScreenCaptureMode;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// System.out.println("repaint canvas show grid " + showGrid);

		if (isShowGrid()) {
			double xInc, yInc;
			final int GRID_SIZE = 20, DRAW = 0, FILL = 1, PAD = 20;

			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.GRAY);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			double w = getWidth();
			double h = getHeight();

			xInc = (w - 2 * PAD) / GRID_SIZE;
			yInc = (h - 2 * PAD) / GRID_SIZE;

			// row lines
			double x1 = PAD, y1 = PAD, x2 = w - PAD, y2 = h - PAD;

			// col lines
			y1 = PAD;

			for (int i = 0; i < GRID_SIZE + GRID_SIZE; i++) {
				for (int j = 0; j <= GRID_SIZE; j++) {
					g2.draw(new Line2D.Double(x1, y1, x1 + 1, y1));
					x1 += xInc;
				}
				x1 = PAD;
				y1 += yInc;
			}
		}

		for (IMathShape ms : getMathShapes()) {
			if (!(ms instanceof I3D)) {

				if (isScreenCaptureMode) {
					ms.setShowCornerPoints(false);
				}

				ms.setHasDecorations(true);
				ms.paintComponent(g);
			}
		}
		
		Graphics2D g2 = (Graphics2D) g;
		for (Shape aShape : shapes) {
			g2.draw(aShape);
		}
		// System.out.println("LAYOUT: "+getLayout());

	}

	public void setShowGrid(boolean showGrid) {
		this.showGrid = showGrid;
	}

	public boolean isShowGrid() {
		return showGrid;
	}

	public void addRegularShape(Shape shape) {
		shapes.add(shape);
	}
	
	@Override
	public void addShape(IMathShape shape) {
		getMathShapes().add(shape);
		if (shape instanceof I3D) {
			this.add((JComponent) shape);
		}
	}

	public void selectAll(boolean isSelected, String type) {
		for (IMathShape shape : mathShapes) {
			if (UIUtils._3D.equals(type) && shape instanceof I3D) {
				shape.setShowCornerPoints(isSelected);
				shape.repaint();
			} else if (UIUtils._2D.equals(type)) {
				shape.setShowCornerPoints(isSelected);
				shape.repaint();
			}
		}
		this.repaint();
		this.revalidate();
	}

	@Override
	public void setMathShapes(ArrayList<IMathShape> mathShapes) {
		this.mathShapes = mathShapes;
	}

	@Override
	public ArrayList<IMathShape> getMathShapes() {
		return mathShapes;
	}

	public void setControlPanel(ControlPanel controlPanel) {
		this.controlPanel = controlPanel;

	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void removeAllShapes() {

		Iterator iterator = getMathShapes().iterator();
		while (iterator.hasNext()) {
			IMathShape ms = (IMathShape) iterator.next();
			removeSelectedShape(ms);

		}

	}

	public void removeSelectedShape(IMathShape shape) {
		if (shape instanceof I3D) {
			remove3DFromCanvas(shape);
		}
		this.mathShapes.remove(shape);

	}

	protected void remove3DFromCanvas(IMathShape shape) {

		Component[] components = this.getComponents();

		for (Component component : components) {
			if (component.getName() != null
					&& component.getName().equals(UIUtils._3D)) {
				if (((IMathShape) component).getId().equals(shape.getId())) {
					this.remove(component);
				}

			}

		}
	}

}