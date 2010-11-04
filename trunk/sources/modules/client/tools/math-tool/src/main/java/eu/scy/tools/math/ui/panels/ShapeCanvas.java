package eu.scy.tools.math.ui.panels;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import org.jdesktop.swingx.JXPanel;

import eu.scy.tools.math.shapes.IMathShape;


public class ShapeCanvas extends JXPanel implements IShapeCanvas {
	
	private boolean showGrid;
	private ArrayList<IMathShape> mathShapes = new ArrayList<IMathShape>();
	

	
    public ShapeCanvas(LayoutManager layout) {
    	super.setLayout(layout);
    	setDoubleBuffered(true);
	}


	public ShapeCanvas() {
		setDoubleBuffered(true);
	}


	public ShapeCanvas(boolean showGrid) {
		this.showGrid = showGrid;
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
			ms.paintComponent(g);
		}
	        
    }


	public void setShowGrid(boolean showGrid) {
		this.showGrid = showGrid;
	}

	public boolean isShowGrid() {
		return showGrid;
	}
	
	@Override
	public void addShape(IMathShape shapes) {
		getMathShapes().add(shapes);
	
	}
	@Override
	public void setMathShapes(ArrayList<IMathShape> mathShapes) {
		this.mathShapes = mathShapes;
	}

	@Override
	public ArrayList<IMathShape> getMathShapes() {
		return mathShapes;
	}
	
}