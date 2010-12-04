package eu.scy.tools.math.controller;

import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.error.ErrorInfo;

import com.jhlabs.image.CausticsFilter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import eu.scy.tools.math.adapters.AdjustSizeAdapter;
import eu.scy.tools.math.adapters.ShapeJXLabelDropTargetListener;
import eu.scy.tools.math.adapters.ShapeMoverAdapter;
import eu.scy.tools.math.shapes.I3D;
import eu.scy.tools.math.shapes.IMathCylinder3D;
import eu.scy.tools.math.shapes.IMathEllipse;
import eu.scy.tools.math.shapes.IMathRectangle;
import eu.scy.tools.math.shapes.IMathRectangle3D;
import eu.scy.tools.math.shapes.IMathShape;
import eu.scy.tools.math.shapes.IMathTriangle;
import eu.scy.tools.math.shapes.Math3DShape;
import eu.scy.tools.math.shapes.MathCylinder3D;
import eu.scy.tools.math.shapes.MathEllipse;
import eu.scy.tools.math.shapes.MathRectangle;
import eu.scy.tools.math.shapes.MathRectangle3D;
import eu.scy.tools.math.shapes.MathSphere3D;
import eu.scy.tools.math.shapes.MathTriangle;
import eu.scy.tools.math.ui.ComputationDataObject;
import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.panels.Calculator;
import eu.scy.tools.math.ui.panels.ControlPanel;
import eu.scy.tools.math.ui.panels.ShapeCanvas;

public class MathToolController {

	private static Logger log = Logger.getLogger("MathToolController.class"); //$NON-NLS-1$

	private HashMap<String, ShapeCanvas> shapeCanvases = new HashMap<String, ShapeCanvas>();
	private HashMap<String, Calculator> calculators = new HashMap<String, Calculator>();
	private HashMap<String, JXTable> computationTables = new HashMap<String, JXTable>();
	private IMathShape mathShape;

	private XStream xstream;

	public MathToolController() {
		init();
	}

	protected void init() {
		xstream = new XStream(new DomDriver());
		xstream.alias("rectangle", MathRectangle.class);
		xstream.alias("triangle", MathTriangle.class);
		xstream.alias("ellipse", MathEllipse.class);
	}

	public void addCanvas(String type, ShapeCanvas shapeCanvas) {

		getShapeCanvases().put(type, shapeCanvas);

		ShapeMoverAdapter shapeMoverAdapter = new ShapeMoverAdapter(this, type);
		AdjustSizeAdapter adjustSizeAdapter = new AdjustSizeAdapter(this, type);

		shapeCanvas.setDropTarget(new DropTarget(shapeCanvas,
				new ShapeJXLabelDropTargetListener(this, type)));

	}

	public void addCalculator(String type, Calculator calculator) {
		calculator.getEqualsButton().addActionListener(equalsAction);
		calculator.getSumTextField().addActionListener(equalsAction);

		calculator.getAddButton().setAction(addResultAction);
		calculator.getSubtractButton().setAction(subtractResultAction);
		getCalculators().put(type, calculator);
	}

	protected String modExpression(String expression) {
		String lowerCaseExpression = StringUtils.lowerCase(expression);

		// check for sqrt
		int indexOf = StringUtils.indexOf(lowerCaseExpression, "^");

		if (mathShape instanceof IMathEllipse)
			lowerCaseExpression = StringUtils.replace(lowerCaseExpression,
					IMathEllipse.RADIUS,
					Double.toString(((IMathEllipse) mathShape).getRadius()));
		else if (mathShape instanceof IMathRectangle) {

			lowerCaseExpression = StringUtils.replace(lowerCaseExpression,
					IMathRectangle.WIDTH,
					Double.toString(((IMathRectangle) mathShape).getWidth()));
			lowerCaseExpression = StringUtils.replace(lowerCaseExpression,
					IMathRectangle.HEIGHT,
					Double.toString(((IMathRectangle) mathShape).getHeight()));
		} else if (mathShape instanceof IMathTriangle) {
			lowerCaseExpression = StringUtils.replace(lowerCaseExpression,
					IMathTriangle.WIDTH,
					Double.toString(((IMathTriangle) mathShape).getWidth()));
			lowerCaseExpression = StringUtils.replace(lowerCaseExpression,
					IMathTriangle.HEIGHT,
					Double.toString(((IMathTriangle) mathShape).getHeight()));
		} else if (mathShape instanceof IMathRectangle3D) {
			lowerCaseExpression = StringUtils.replace(lowerCaseExpression,
					IMathRectangle3D.WIDTH,
					((IMathRectangle3D) mathShape).getWidthValue());
			lowerCaseExpression = StringUtils.replace(lowerCaseExpression,
					IMathRectangle3D.HEIGHT,
					((IMathRectangle3D) mathShape).getHeightValue());
		} else if (mathShape instanceof IMathCylinder3D) {
			lowerCaseExpression = StringUtils.replace(lowerCaseExpression,
					IMathCylinder3D.RADIUS,
					((IMathCylinder3D) mathShape).getRadiusValue());
			lowerCaseExpression = StringUtils.replace(lowerCaseExpression,
					IMathCylinder3D.HEIGHT,
					((IMathCylinder3D) mathShape).getHeightValue());
		}
		return lowerCaseExpression;
	}

	public void setSelectedMathShape(IMathShape mathShape) {
		this.mathShape = mathShape;

		String t;
		if (mathShape instanceof I3D) {
			t = UIUtils._3D;
			ShapeCanvas shapeCanvas = shapeCanvases.get(t);
			ArrayList<IMathShape> mathShapes = shapeCanvas.getMathShapes();
			for (IMathShape ms : mathShapes) {
				if (ms instanceof I3D && !ms.equals(mathShape))
					ms.setShowCornerPoints(false);

			}
		} else {
			t = UIUtils._2D;
			ShapeCanvas shapeCanvas = shapeCanvases.get(t);
			ArrayList<IMathShape> mathShapes = shapeCanvas.getMathShapes();

			for (IMathShape ms : mathShapes) {
				if (!(ms instanceof I3D) && !ms.equals(mathShape))
					ms.setShowCornerPoints(false);

			}
		}

		mathShape.setShowCornerPoints(true);

		this.selectInTable(mathShape);

	}

	protected void selectInTable(IMathShape mathShape) {

		String t;
		if (mathShape.getId() != null && mathShape instanceof I3D) {
			t = UIUtils._3D;

		} else if (mathShape.getId() != null) {
			t = UIUtils._2D;
		} else {
			return;
		}

		JXTable table = getComputationTables().get(t);
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		table.getSelectionModel().setSelectionInterval(
				Integer.parseInt(mathShape.getId()),
				Integer.parseInt(mathShape.getId()));
	}

	public IMathShape getMathSelectedShape() {
		return mathShape;
	}

	public void setShapeCanvases(HashMap<String, ShapeCanvas> shapeCanvases) {
		this.shapeCanvases = shapeCanvases;
	}

	public HashMap<String, ShapeCanvas> getShapeCanvases() {
		return shapeCanvases;
	}

	public void addShape(JLabel label, Point dropPoint, String type) {
		System.out.println("ShapeCanvas.addShape()" + label.getName());

		ShapeCanvas sc = getShapeCanvases().get(type);

		if (label.getName().equals(UIUtils.CIRCLE)) {
			MathEllipse t = new MathEllipse(dropPoint.x, dropPoint.y, 200, 200);
			sc.addShape(t);
			this.setSelectedMathShape(t);
		} else if (label.getName().equals(UIUtils.RECTANGLE)) {
			MathRectangle t = new MathRectangle(dropPoint.x, dropPoint.y, 100,
					100);
			sc.addShape(t);
			this.setSelectedMathShape(t);
		} else if (label.getName().equals(UIUtils.TRIANGLE)) {
			MathTriangle t = new MathTriangle(dropPoint.x, dropPoint.y, 200);
			sc.addShape(t);
			this.setSelectedMathShape(t);
		} else if (label.getName().equals(UIUtils.RECTANGLE3D)) {
			MathRectangle3D t = new MathRectangle3D(dropPoint.x, dropPoint.y);
			t.getAddButton().addActionListener(add3dAction);
			sc.addShape(t);
			this.setSelectedMathShape(t);
		} else if (label.getName().equals(UIUtils.SPHERE3D)) {
			MathSphere3D t = new MathSphere3D(dropPoint.x, dropPoint.y);

			sc.addShape(t);
			t.getAddButton().addActionListener(add3dAction);
			t.setLocation(dropPoint);
			sc.repaint();
			this.setSelectedMathShape(t);
		} else if (label.getName().equals(UIUtils.CYLINDER3D)) {
			MathCylinder3D t = new MathCylinder3D(dropPoint.x, dropPoint.y);
			t.getAddButton().addActionListener(add3dAction);
			sc.addShape(t);
			t.setLocation(dropPoint);
			sc.repaint();
			this.setSelectedMathShape(t);
		}
		sc.repaint();
	}

	Action add3dAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JXButton button = (JXButton) e.getSource();
			Math3DShape mathShape = (Math3DShape) button
					.getClientProperty(UIUtils.SHAPE);

			mathShape.checkForError();
			if (mathShape.isHasError() == true)
				return;

			String volume = mathShape.getVolumeValueLabel().getText();
			String surfaceArea = mathShape.getSurfaceAreaTextField().getText();
			String ratio = mathShape.getRatioTextField().getText();
			String shape = mathShape.getType();

			JXTable table = getComputationTables().get(UIUtils._3D);
			ShapeCanvas sc = getShapeCanvases().get(UIUtils._3D);
			DefaultTableModel model = (DefaultTableModel) table.getModel();

			Integer id = new Integer(model.getRowCount() + 1);
			model.addRow(new Object[] { id, shape, ratio, surfaceArea, volume });
			int indexOf = sc.getMathShapes().indexOf(mathShape);
			mathShape.setId(Integer.toString(model.getRowCount() - 1));
			sc.getMathShapes().add(indexOf, mathShape);

			table.getSelectionModel().setSelectionInterval(
					model.getRowCount() - 1, model.getRowCount() - 1);
			button.putClientProperty(UIUtils.SHAPE, mathShape);
		}
	};

	public void addComputationTable(String type, JXTable computationTable) {
		this.getComputationTables().put(type, computationTable);
	}

	public void setCalculators(HashMap<String, Calculator> calculators) {
		this.calculators = calculators;
	}

	public HashMap<String, Calculator> getCalculators() {
		return calculators;
	}

	public void setComputationTables(HashMap<String, JXTable> computationTables) {
		this.computationTables = computationTables;
	}

	public HashMap<String, JXTable> getComputationTables() {
		return computationTables;
	}

	Action addResultAction = new AbstractAction("Add") {

		@Override
		public void actionPerformed(ActionEvent ae) {

			JXButton addButton = (JXButton) ae.getSource();
			String t = (String) addButton.getClientProperty(UIUtils.TYPE);

			Calculator calculator = getCalculators().get(t);

			String text = calculator.getResultLabel().getText();
			float parseFloat = Float.parseFloat(text);

			DefaultTableModel model = (DefaultTableModel) getComputationTables()
					.get(t).getModel();

			model.addRow(new Object[] { new Integer(model.getRowCount() + 1),
					getMathSelectedShape().getType(), new Float(text),
					new Float(text) });
			getMathSelectedShape().setId(
					Integer.toString((model.getRowCount() - 1)));

			calculator.resetLabel();
		}

	};

	Action subtractResultAction = new AbstractAction("Subtract") {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			// float parseFloat = Float.parseFloat(resultLabel.getText());

		}

	};

	ActionListener equalsAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JComponent equalsButton = (JComponent) e.getSource();
			String calcType = (String) equalsButton
					.getClientProperty(UIUtils.TYPE);
			Calculator c = getCalculators().get(calcType);

			String expression = c.getSumTextField().getText();

			if (expression != null
					&& StringUtils.stripToNull(expression) != null) {
				String modExpression = modExpression(expression);
				log.info("new expression: " + modExpression);

				try {
					Evaluator evaluator = new Evaluator();
					String result = evaluator.evaluate(modExpression);
					c.getResultLabel().setText(result);
				} catch (EvaluationException ee) {
					ee.printStackTrace();
				}
			}

		}
	};

	public void save() {
		JFileChooser fc = new JFileChooser();

		fc.setSelectedFile(new File("mt-saved-data.xml"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML",
				new String[] { "XML" });
		fc.setFileFilter(filter);
		int returnVal = fc.showSaveDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			File file = fc.getSelectedFile();
			  Collection c = shapeCanvases.values();
			  
			    //obtain an Iterator for Collection
			    Iterator itr = c.iterator();
			 
			    //iterate through HashMap values iterator
//			    while(itr.hasNext()) {
//			      ShapeCanvas  sc = (ShapeCanvas) itr.next();
//			    }
			    	ShapeCanvas sc = shapeCanvases.get(UIUtils._2D);
			      String xml = xstream.toXML(sc.getMathShapes());
			try {
				FileUtils.writeStringToFile(file, xml);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	public void open() {
		JFileChooser fc = new JFileChooser();
		
		 
		 FileNameExtensionFilter filter = new FileNameExtensionFilter("XML", new String[] { "XML" });
		 fc.setFileFilter(filter);
		int returnVal = fc.showOpenDialog(null);
		
		
		 
       if (returnVal == JFileChooser.APPROVE_OPTION) {
     
			
           File file = fc.getSelectedFile();
           String fts = null;
			try {
				fts = FileUtils.readFileToString(file);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			ShapeCanvas sc = shapeCanvases.get(UIUtils._2D);
			sc.getMathShapes().removeAll(sc.getMathShapes());
			sc.repaint();
			sc.revalidate();
			ArrayList newJoe = (ArrayList)xstream.fromXML(fts);
			for (Object object : newJoe) {
				sc.addShape((IMathShape) object);
			}
			
			sc.repaint();
			sc.revalidate();
			
       }
		
	}

}
