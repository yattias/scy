package eu.scy.tools.math.controller;

import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXTable;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import eu.scy.tools.math.adapters.AdjustSizeAdapter;
import eu.scy.tools.math.adapters.ShapeJXLabelDropTargetListener;
import eu.scy.tools.math.adapters.ShapeMoverAdapter;
import eu.scy.tools.math.doa.ComputationDataObj;
import eu.scy.tools.math.doa.DataStoreObj;
import eu.scy.tools.math.doa.ThreeDObj;
import eu.scy.tools.math.shapes.I3D;
import eu.scy.tools.math.shapes.IMathCylinder3D;
import eu.scy.tools.math.shapes.IMathEllipse;
import eu.scy.tools.math.shapes.IMathRectangle;
import eu.scy.tools.math.shapes.IMathRectangle3D;
import eu.scy.tools.math.shapes.IMathShape;
import eu.scy.tools.math.shapes.IMathTriangle;
import eu.scy.tools.math.shapes.impl.Math3DShape;
import eu.scy.tools.math.shapes.impl.MathCylinder3D;
import eu.scy.tools.math.shapes.impl.MathEllipse;
import eu.scy.tools.math.shapes.impl.MathRectangle;
import eu.scy.tools.math.shapes.impl.MathRectangle3D;
import eu.scy.tools.math.shapes.impl.MathSphere3D;
import eu.scy.tools.math.shapes.impl.MathTriangle;
import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.panels.Calculator;
import eu.scy.tools.math.ui.panels.ShapeCanvas;

public class MathToolController {

	protected static Logger log = Logger.getLogger("MathToolController.class"); //$NON-NLS-1$

	protected Map<String, ShapeCanvas> shapeCanvases = new HashMap<String, ShapeCanvas>();
	protected Map<String, Calculator> calculators = new HashMap<String, Calculator>();
	protected Map<String, JXTable> computationTables = new HashMap<String, JXTable>();
	protected IMathShape mathShape;

	protected XStream xstream;

	public MathToolController() {
		init();
	}

	protected void init() {
		xstream = new XStream(new DomDriver());
		xstream.alias("rectangle", MathRectangle.class);
		xstream.alias("triangle", MathTriangle.class);
		xstream.alias("ellipse", MathEllipse.class);
		xstream.alias("tableObject", ComputationDataObj.class);
		xstream.alias("DataStoreObject", DataStoreObj.class);
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
//		calculator.getSumTextField().addActionListener(equalsAction);

		calculator.getAddButton().setAction(addResultAction);
		calculator.getSubtractButton().setAction(subtractResultAction);
		calculator.getSubtractButton().setEnabled(false);
		getCalculators().put(type, calculator);
	}

	protected String modExpression(String expression) {
		String lowerCaseExpression = StringUtils.lowerCase(expression);

		// check for sqrt
		int indexOf = StringUtils.indexOf(lowerCaseExpression, "^");

		if (mathShape instanceof IMathEllipse)
			lowerCaseExpression = StringUtils.replace(lowerCaseExpression,
					IMathEllipse.RADIUS,
					Double.toString(((IMathEllipse) mathShape).getScaledRadius()));
		else if (mathShape instanceof IMathRectangle) {

			lowerCaseExpression = StringUtils.replace(lowerCaseExpression,
					IMathRectangle.WIDTH,
					Double.toString(((IMathRectangle) mathShape).getScaledWidth()));
			lowerCaseExpression = StringUtils.replace(lowerCaseExpression,
					IMathRectangle.HEIGHT,
					Double.toString(((IMathRectangle) mathShape).getScaledHeight()));
		} else if (mathShape instanceof IMathTriangle) {
			lowerCaseExpression = StringUtils.replace(lowerCaseExpression,
					IMathTriangle.WIDTH,
					Double.toString(((IMathTriangle) mathShape).getScaledWidth()));
			lowerCaseExpression = StringUtils.replace(lowerCaseExpression,
					IMathTriangle.HEIGHT,
					Double.toString(((IMathTriangle) mathShape).getScaledHeight()));
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
		this.highLightShape(mathShape);

		this.selectInTable(mathShape);

	}
	
	protected void highLightShape(IMathShape mathShape) {
		this.mathShape = mathShape;

		String type;
		if (mathShape instanceof I3D) {
			type = UIUtils._3D;
			ShapeCanvas shapeCanvas = shapeCanvases.get(type);
			ArrayList<IMathShape> mathShapes = shapeCanvas.getMathShapes();
			for (IMathShape ms : mathShapes) {
				if (ms instanceof I3D && !ms.equals(mathShape))
					ms.setShowCornerPoints(false);

			}
		} else {
			type = UIUtils._2D;
			ShapeCanvas shapeCanvas = shapeCanvases.get(type);
			ArrayList<IMathShape> mathShapes = shapeCanvas.getMathShapes();

			for (IMathShape ms : mathShapes) {
				if (!ms.equals(mathShape)) {
					ms.setShowCornerPoints(false);
					ms.repaint();
				} else {
					ms.setShowCornerPoints(true);
					ms.repaint();
				}

			}
		}

		mathShape.setShowCornerPoints(true);
		mathShape.repaint();
	}

	protected void selectInTable(IMathShape mathShape) {

		String type;
		if (mathShape.getId() != null && mathShape instanceof I3D) {
			type = UIUtils._3D;

		} else if (mathShape.getId() != null) {
			type = UIUtils._2D;
		} else {
			return;
		}

		JXTable table = getComputationTables().get(type);
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		
		
		List<ComputationDataObj> tableObjects = getTableObjects(model.getDataVector(),type);
		for (ComputationDataObj computationDataObj : tableObjects) {
			if( computationDataObj.getShapeId().equals(mathShape.getId())) {
				table.getSelectionModel().setSelectionInterval(computationDataObj.getColumnNumber().intValue()-1,computationDataObj.getColumnNumber().intValue()-1);
			}
		}
		
		
		
		
	}

	public IMathShape getMathSelectedShape() {
		return mathShape;
	}

	public void setShapeCanvases(HashMap<String, ShapeCanvas> shapeCanvases) {
		this.shapeCanvases = shapeCanvases;
	}

	public Map<String, ShapeCanvas> getShapeCanvases() {
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
			if (mathShape.getError() == true)
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
		
//		if( type.equals(UIUtils._2D))
//			computationTable.getSelectionModel().addListSelectionListener(new TwoDeeTableSelectionListener());
//		
		this.getComputationTables().put(type, computationTable);
		
	}

	public void setCalculators(HashMap<String, Calculator> calculators) {
		this.calculators = calculators;
	}

	public Map<String, Calculator> getCalculators() {
		return calculators;
	}

	public void setComputationTables(HashMap<String, JXTable> computationTables) {
		this.computationTables = computationTables;
	}

	public Map<String, JXTable> getComputationTables() {
		return computationTables;
	}

	Action addResultAction = new AbstractAction("Add") {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {

			if( getMathSelectedShape() == null )
				return;

			
			JXButton addButton = (JXButton) actionEvent.getSource();
			String t = (String) addButton.getClientProperty(UIUtils.TYPE);
			doOperation(t, "+");

		}

	};

	protected void doOperation(String t, String operation) {
		Calculator calculator = getCalculators().get(t);
		calculator.getSubtractButton().setEnabled(true);
		String text = calculator.getResultLabel().getText();
		float parseFloat = Float.parseFloat(text);

		DefaultTableModel model = (DefaultTableModel) getComputationTables()
				.get(t).getModel();

		
		
		Vector dataVector = (Vector) model.getDataVector();
		Float oldSum = null;
		if( !dataVector.isEmpty() ) {
			Vector elementAt = (Vector) dataVector.elementAt(dataVector.size()-1);
		
		if( elementAt != null) {
			ComputationDataObj co = new ComputationDataObj(elementAt, UIUtils._2D);
			oldSum = co.getSum();
		}
		} else {
			oldSum = new Float("0.00");
		}
		
		if( operation.equals("+")) {
			model.addRow(new Object[] { new Integer(model.getRowCount() + 1),
					getMathSelectedShape().getType(), new Float(text),
					new Float(oldSum+parseFloat), operation, getMathSelectedShape().getId()});
		} else {
			model.addRow(new Object[] { new Integer(model.getRowCount() + 1),
					getMathSelectedShape().getType(), new Float(text),
					new Float(oldSum-parseFloat),operation,getMathSelectedShape().getId() });
		}

		//calculator.resetLabel();
	}
	
	Action subtractResultAction = new AbstractAction("Subtract") {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {

			if( getMathSelectedShape() == null )
				return;
			
			JXButton addButton = (JXButton) actionEvent.getSource();
			String t = (String) addButton.getClientProperty(UIUtils.TYPE);
			doOperation(t, "-");
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

	class TwoDeeTableSelectionListener implements ListSelectionListener{

		@Override
		public void valueChanged(ListSelectionEvent e) {

			if( e.getValueIsAdjusting() ) {
			int firstIndex = e.getFirstIndex();

			DefaultTableModel model = (DefaultTableModel) getComputationTables()
					.get(UIUtils._2D).getModel();

			String shapeId = (String) model.getValueAt(firstIndex, 5);

			log.info("shapeId " + shapeId);
			ShapeCanvas sc = shapeCanvases.get(UIUtils._2D);

			for (IMathShape ms : sc.getMathShapes()) {
				if (ms.getId().equals(shapeId)) {
					ms.setShowCornerPoints(true);
					ms.repaint();
					log.info("found shape" + ms);
				} else {
					ms.setShowCornerPoints(false);
					ms.repaint();
					log.info("no found shape" + ms);
				}
			}

			sc.repaint();
			
			}
		}
	}
	public String save() {
		JFileChooser fc = new JFileChooser();

		fc.setSelectedFile(new File("mt-saved-data.xml"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML",
				new String[] { "XML" });
		fc.setFileFilter(filter);
		int returnVal = fc.showSaveDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			File file = fc.getSelectedFile();
			String xml = this.writeXML();
			       
			try {
				FileUtils.writeStringToFile(file, xml);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return xml;

		}
		return null;
	}
	
	protected String writeXML() {
		Map<String, DataStoreObj> objHashMap = new HashMap<String, DataStoreObj>();
		
		 Set<String> keyset = shapeCanvases.keySet();
		Iterator keySetIterator = keyset.iterator();
		while(keySetIterator.hasNext()) {
			String key = (String) keySetIterator.next();
		
			DataStoreObj doa = new DataStoreObj();
			doa.setType(key);
			if( key.equals(UIUtils._3D))
				doa.setThreeDMathShapes(this.convertTo3DDataObj(this.getShapeCanvases().get(key).getMathShapes()));
			else
				doa.setTwoDMathShapes(this.getShapeCanvases().get(key).getMathShapes());
			doa.setTablesObjects(getTableObjects(key));
			objHashMap.put(key, doa);
		}
		
		return xstream.toXML(objHashMap);
	}

	private List<ThreeDObj> convertTo3DDataObj(ArrayList<IMathShape> mathShapes) {
		List<ThreeDObj> objs = new ArrayList<ThreeDObj>();
		for (IMathShape ms : mathShapes) {
			if( ms instanceof MathCylinder3D)  
				objs.add(new ThreeDObj(null,((MathCylinder3D) ms).getRadiusTextField().getText(), ((MathCylinder3D) ms).getSurfaceAreaTextField().getText(), ((MathCylinder3D) ms).getRatioTextField().getText(), ((MathCylinder3D) ms).getLocation(),UIUtils.CYLINDER3D));
			else if( ms instanceof MathSphere3D ) 
				objs.add(new ThreeDObj(null,((MathSphere3D) ms).getRadiusTextField().getText(), ((MathSphere3D) ms).getSurfaceAreaTextField().getText(), ((MathSphere3D) ms).getRatioTextField().getText(), ((MathSphere3D) ms).getLocation(),UIUtils.SPHERE3D));
			else if( ms instanceof MathRectangle3D ) 
				objs.add(new ThreeDObj(((MathRectangle3D) ms).getLengthTextField().getText(),null, ((MathRectangle3D) ms).getSurfaceAreaTextField().getText(), ((MathRectangle3D) ms).getRatioTextField().getText(), ((MathRectangle3D) ms).getLocation(), UIUtils.RECTANGLE3D));
		}
		return objs;
	}

	protected List<ComputationDataObj> getTableObjects(String key) {
		JXTable jxTable = this.computationTables.get(key);
		DefaultTableModel model = (DefaultTableModel) jxTable.getModel();
		Vector<Vector> dataVector = model.getDataVector();
		List<ComputationDataObj> cdos = new ArrayList<ComputationDataObj>();
		for (Vector data : dataVector) {
			cdos.add(new ComputationDataObj(data, key));
		}
		return cdos;
		
	}
	
	protected List<ComputationDataObj> getTableObjects(Vector<Vector>  dataVector, String key) {
		List<ComputationDataObj> cdos = new ArrayList<ComputationDataObj>();
		for (Vector data : dataVector) {
			cdos.add(new ComputationDataObj(data, key));
		}
		return cdos;
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
				this.open(fts, true);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
       }
		
	}
	
	public void open(String xml, boolean showNag) {
		if(showNag) {
			int reply = JOptionPane.showConfirmDialog(null, "Do you want to replace all your current work?", "?", JOptionPane.YES_NO_OPTION);
		    if (reply == JOptionPane.YES_OPTION) {
		      refresh(xml);
		    }
		    return;
		} else {
			refresh(xml);
		}
	}

	protected void refresh(String xml) {
		
		Map<String, DataStoreObj> objHashMap =  (Map<String, DataStoreObj>) xstream.fromXML(xml);
		
		
		 Set<String> keyset = shapeCanvases.keySet();
			Iterator keySetIterator = keyset.iterator();
			while(keySetIterator.hasNext()) {
				String key = (String) keySetIterator.next();
			
				ShapeCanvas sc = shapeCanvases.get(key);
				sc.removeAllShapes();
				
				sc.repaint();
				sc.revalidate();
				
				DataStoreObj dataStoreObj = objHashMap.get(key);
				
				if( key.equals(UIUtils._3D) ) {
					List<ThreeDObj> threeObjects = dataStoreObj.getThreeDMathShapes();
					for (ThreeDObj to : threeObjects) {
							sc.addShape(convertThreeObjToMathShape(to));
					}

				} else {
					List<IMathShape> mathShapes = dataStoreObj.getTwoDMathShapes();
					for (IMathShape iMathShape : mathShapes) {
							sc.addShape(iMathShape);
					}

				}
				
				
				sc.repaint();
				sc.revalidate();
				
				List<ComputationDataObj> tablesObjects = dataStoreObj.getTablesObjects();
				DefaultTableModel model = (DefaultTableModel) getComputationTables().get(key).getModel();
				model.getDataVector().removeAllElements();
				for (ComputationDataObj computationDataObject : tablesObjects) {
					model.addRow(computationDataObject.toArray(key));
				}
			}
	}

	private IMathShape convertThreeObjToMathShape(ThreeDObj to) {
		if( to.getType().equals(UIUtils.CYLINDER3D)) {
			MathCylinder3D ms = new MathCylinder3D(to.getPosition());
			ms.getRadiusTextField().setText(to.getRadius());
			ms.getSurfaceAreaTextField().setText(to.getSurfaceArea());
			ms.getRatioTextField().setText(to.getRatio());
			ms.getAddButton().addActionListener(add3dAction);
			return ms;
		} else if(to.getType().equals(UIUtils.SPHERE3D)) {
			MathSphere3D ms = new MathSphere3D(to.getPosition());
			ms.getRadiusTextField().setText(to.getRadius());
			ms.getSurfaceAreaTextField().setText(to.getSurfaceArea());
			ms.getRatioTextField().setText(to.getRatio());
			ms.getAddButton().addActionListener(add3dAction);
			return ms;
		} else if(to.getType().equals(UIUtils.RECTANGLE3D)) {
			MathRectangle3D ms = new MathRectangle3D(to.getPosition());
			ms.getLengthTextField().setText(to.getLength());
			ms.getSurfaceAreaTextField().setText(to.getSurfaceArea());
			ms.getRatioTextField().setText(to.getRatio());
			ms.getAddButton().addActionListener(add3dAction);
			return ms;
		}
		return null;
	}
}
