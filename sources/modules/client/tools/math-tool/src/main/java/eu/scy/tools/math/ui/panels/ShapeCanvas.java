package eu.scy.tools.math.ui.panels;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.ArrayUtils;
import org.jdesktop.swingx.JXButton;

import eu.scy.tools.math.adapters.AdjustSizeAdapter;
import eu.scy.tools.math.adapters.ShapeJXLabelDropTargetListener;
import eu.scy.tools.math.adapters.ShapeMoverAdapter;
import eu.scy.tools.math.shapes.I3D;
import eu.scy.tools.math.shapes.IMathShape;
import eu.scy.tools.math.shapes.Math3DShape;
import eu.scy.tools.math.shapes.MathCylinder3D;
import eu.scy.tools.math.shapes.MathEllipse;
import eu.scy.tools.math.shapes.MathRectangle;
import eu.scy.tools.math.shapes.MathRectangle3D;
import eu.scy.tools.math.shapes.MathSphere3D;
import eu.scy.tools.math.shapes.MathTriangle;
import eu.scy.tools.math.ui.UIUtils;


public class ShapeCanvas extends JPanel implements IShapeCanvas{
	
	private boolean showGrid;
	private ArrayList<IMathShape> mathShapes = new ArrayList<IMathShape>();
	private ControlPanel controlPanel;



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
		
		new ShapeMoverAdapter(this);
		new AdjustSizeAdapter(this);
		setDoubleBuffered(true);
		setDropTarget(new DropTarget(this, new ShapeJXLabelDropTargetListener(this)));
			
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
		
		if( shape instanceof I3D )
			this.add((JComponent) shape);
	
	}
	
	public void addShape(JLabel label, Point dropPoint) {
		System.out.println("ShapeCanvas.addShape()" + label.getName());
		
		if( label.getName().equals(UIUtils.CIRCLE)) {
			MathEllipse me = new MathEllipse(dropPoint.x, dropPoint.y, 200, 200);
			this.addShape(me);
		} else if( label.getName().equals(UIUtils.RECTANGLE)) {
			MathRectangle mtr = new MathRectangle(dropPoint.x, dropPoint.y, 100, 100);
			this.addShape(mtr);
		} else if( label.getName().equals(UIUtils.TRIANGLE)) {
			MathTriangle t = new MathTriangle(dropPoint.x, dropPoint.y,200);
			this.addShape(t);
		} else if( label.getName().equals(UIUtils.RECTANGLE3D)) {
			MathRectangle3D t = new MathRectangle3D(dropPoint.x, dropPoint.y);
			t.getAddButton().addActionListener(add3dAction);
			this.addShape(t);
		} else if( label.getName().equals(UIUtils.SPHERE3D)) {
			MathSphere3D t = new MathSphere3D(dropPoint.x, dropPoint.y);
			
			this.addShape(t);
			t.getAddButton().addActionListener(add3dAction);
			t.setLocation(dropPoint);
			this.repaint();
		} else if( label.getName().equals(UIUtils.CYLINDER3D)) {
			MathCylinder3D t = new MathCylinder3D(dropPoint.x, dropPoint.y);
			t.getAddButton().addActionListener(add3dAction);
			this.addShape(t);
			t.setLocation(dropPoint);
			this.repaint();
		}
		this.repaint();
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


	Action add3dAction = new AbstractAction() {
		
	@Override
	public void actionPerformed(ActionEvent e) {
			JXButton button = (JXButton) e.getSource();
			Math3DShape mathShape = (Math3DShape) button.getClientProperty(Math3DShape.SHAPE);
			
			mathShape.checkForError();
			if( mathShape.isHasError() == true )
				return;
				
			String volume = mathShape.getVolumeValueLabel().getText();
			String surfaceArea = mathShape.getSurfaceAreaTextField().getText();
			String ratio = mathShape.getRatioTextField().getText();
			String shape = mathShape.getType();
			
			
//			
			DefaultTableModel model = (DefaultTableModel) controlPanel.getComputationTable().getModel();
//			
			if( model.getRowCount() == 0 ) {
				model.addRow(new Object[]{new Integer("1"), shape,ratio,surfaceArea,volume});
			} else {
				
				Vector data = model.getDataVector();
				Vector lastElement = (Vector) data.lastElement();
//				ComputationDataObject c = new ComputationDataObject(lastElement,UIUtils._3D);
//				
				
				model.addRow(new Object[]{new Integer(model.getRowCount() + 1),  shape,ratio,surfaceArea,volume});
				
			}
		}
	};

	
}