package eu.scy.tools.math.ui.panels;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import eu.scy.tools.math.shapes.I3D;
import eu.scy.tools.math.shapes.IMathShape;
import eu.scy.tools.math.ui.UIUtils;


public class ShapeCanvas extends JPanel implements IShapeCanvas{
	
	private static Logger log = Logger.getLogger("ShapeCanvas.class"); //$NON-NLS-1$

	
	private boolean showGrid;
	private ArrayList<IMathShape> mathShapes = new ArrayList<IMathShape>();
	private ControlPanel controlPanel;
	private String type;

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

	public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	System.out.println("repaint canvas show grid " + showGrid);
    	
		if (isShowGrid()) {
			double xInc, yInc;
			final int GRID_SIZE = 20, DRAW = 0, FILL = 1, PAD = 20;

			Graphics2D g2 = (Graphics2D) g;

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
			if( !(ms instanceof I3D)) {
				ms.paintComponent(g);
			}
		}
		System.out.println("LAYOUT: "+getLayout());
	        
    }


	public void setShowGrid(boolean showGrid) {
		this.showGrid = showGrid;
	}

	public boolean isShowGrid() {
		return showGrid;
	}
	
	@Override
	public void addShape(IMathShape shape) {
		getMathShapes().add(shape);
		
		
		if( shape instanceof I3D ) {
			((JComponent) shape).setName(UIUtils._3D);
			this.add((JComponent) shape);
		}
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


	public void removeAllShapes() {
		this.mathShapes.removeAll(mathShapes);
		//remove the 3d ones
		Component[] components = this.getComponents();
		for (Component component : components) {
			if( component.getName() != null && component.getName().equals(UIUtils._3D))
				this.remove(component);
				
		}
		
	}

	
}