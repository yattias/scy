package eu.scy.tools.math.controller;

import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXTable;

import com.jhlabs.composite.ScreenComposite;
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
import eu.scy.tools.math.shapes.IMathSphere3D;
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
import eu.scy.tools.math.ui.panels.ScratchPanel;
import eu.scy.tools.math.ui.panels.ShapeCanvas;
import eu.scy.tools.math.util.MathEvaluator;

public class MathToolController {

	protected static Logger log = Logger.getLogger("MathToolController.class"); //$NON-NLS-1$

	protected Map<String, ShapeCanvas> shapeCanvases = new HashMap<String, ShapeCanvas>();
	private Map<String, ScratchPanel> scratchPadPanels = new HashMap<String, ScratchPanel>();

	protected Map<String, Calculator> calculators = new HashMap<String, Calculator>();
	protected Map<String, JXTable> computationTables = new HashMap<String, JXTable>();
	protected Map<String, String>  shapeIdToForumla = new HashMap<String, String>();
	
 	protected IMathShape mathShape;
	Random generator = new Random( 19580427 );

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

	public void addCanvas(ShapeCanvas shapeCanvas) {

		getShapeCanvases().put(shapeCanvas.getType(), shapeCanvas);

		ShapeMoverAdapter shapeMoverAdapter = new ShapeMoverAdapter(this, shapeCanvas.getType());
		AdjustSizeAdapter adjustSizeAdapter = new AdjustSizeAdapter(this,  shapeCanvas.getType());

		shapeCanvas.setDropTarget(new DropTarget(shapeCanvas,
				new ShapeJXLabelDropTargetListener(this,  shapeCanvas.getType())));

	}

	public void addCalculator(String type, Calculator calculator) {
		calculator.getEqualsButton().addActionListener(equalsAction);
		calculator.getSumTextField().setEnabled(false);
		calculator.getSumTextField().addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
				  if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			          	doEqualsAction((JComponent) e.getSource());
			      } else {
			    	  String calcType = (String) ((JComponent) e.getSource())
						.getClientProperty(UIUtils.TYPE);
			    	  Calculator c = getCalculators().get(calcType);
			    	  c.getResultLabel().setText("0.00");
			    	  c.getAddButton().setEnabled(false);
			      }
				
			}
		});

		calculator.getAddButton().setAction(addResultAction);
		calculator.getAddButton().setEnabled(false);
		calculator.getSubtractButton().setAction(subtractResultAction);
		calculator.getSubtractButton().setEnabled(false);
		getCalculators().put(type, calculator);
	}

	protected void doEqualsAction(JComponent component) {
	
		String calcType = (String) component
				.getClientProperty(UIUtils.TYPE);
		Calculator c = getCalculators().get(calcType);

		ScratchPanel scratchPanel = scratchPadPanels.get(calcType);
		
		String expression = c.getForumla();

		if (expression != null
				&& StringUtils.stripToNull(expression) != null) {
			
			if( mathShape != null) {
				mathShape.setFormula(expression);
			} else {
				
				scratchPanel.setExpression(expression);
			}
			
			 MathEvaluator m = new MathEvaluator(StringUtils.lowerCase(expression));
			
			modExpression(m,expression);

				Double value = m.getValue();
				if( value == null) {
					value = 0.00;
					JOptionPane.showMessageDialog(null, UIUtils.invalidExpressionErrorMessage, "Math Expression Problem", JOptionPane.ERROR_MESSAGE, null);
					c.getResultLabel().setText(""+value);
					c.getAddButton().setEnabled(false);
				} else {
					c.getResultLabel().setText(""+value);
					
					
					c.getAddButton().setEnabled(true);
				}
				
				if( mathShape != null ) {
					mathShape.setResult(""+value);
				} else {
					scratchPanel.setResult(""+value);
				}
		}
		
	}

	protected void modExpression(MathEvaluator mathEvaluator, String expression) {
		
		if( expression.contains("pi")) {
			mathEvaluator.addVariable("pi", Math.PI);
		}
		
		if (mathShape instanceof IMathEllipse) {
			
			if( expression.contains(IMathEllipse.RADIUS) ) {
				mathEvaluator.addVariable(IMathEllipse.RADIUS, ((IMathEllipse) mathShape).getScaledRadius());
			}
			
		} else if (mathShape instanceof IMathRectangle) {

			if( expression.contains(IMathRectangle.WIDTH) ) {
				mathEvaluator.addVariable(IMathRectangle.WIDTH, ((IMathRectangle) mathShape).getScaledWidth());
			}
			
			if( expression.contains(IMathRectangle.HEIGHT) ) {
				mathEvaluator.addVariable(IMathRectangle.HEIGHT, ((IMathRectangle) mathShape).getScaledHeight());
			}
			
		} else if (mathShape instanceof IMathTriangle) {
			
			if( expression.contains(IMathTriangle.WIDTH) ) {
				mathEvaluator.addVariable(IMathTriangle.WIDTH, ((IMathTriangle) mathShape).getScaledWidth());
			}
			
			if( expression.contains(IMathTriangle.HEIGHT) ) {
				mathEvaluator.addVariable(IMathTriangle.HEIGHT, ((IMathTriangle) mathShape).getScaledHeight());
			}
			
		
			
		} else if (mathShape instanceof IMathSphere3D) {
			
			
			if( expression.contains(IMathSphere3D.RADIUS) ) {
				String radiusValue = StringUtils.trimToNull(((IMathSphere3D) mathShape).getRadiusValue());
				if( radiusValue != null )
					mathEvaluator.addVariable(IMathSphere3D.RADIUS, Double.parseDouble(radiusValue));
			}
			
		} else if (mathShape instanceof IMathCylinder3D) {
			
			
			if( expression.contains(IMathCylinder3D.RADIUS) ) {
				String radiusValue = StringUtils.trimToNull(((IMathCylinder3D) mathShape).getRadiusValue());
				if( radiusValue != null )
					mathEvaluator.addVariable(IMathCylinder3D.RADIUS, Double.parseDouble(radiusValue));
			}
			
			if( expression.contains(IMathCylinder3D.HEIGHT) ) {
				mathEvaluator.addVariable(IMathCylinder3D.HEIGHT, Double.parseDouble( ((IMathCylinder3D) mathShape).getHeightValue()));
			}
		} else if (mathShape instanceof IMathRectangle3D) {
			
			if( expression.contains(IMathRectangle3D.WIDTH) ) {
				mathEvaluator.addVariable(IMathRectangle3D.WIDTH, Double.parseDouble(((IMathRectangle3D) mathShape).getWidthValue()));
			}
			
			if( expression.contains(IMathRectangle3D.HEIGHT) ) {
				mathEvaluator.addVariable(IMathRectangle3D.HEIGHT, Double.parseDouble( ((IMathRectangle3D) mathShape).getHeightValue()));
			}
		}
	}
	

	public void setSelectedShape(String type) {
		this.selectAllShapes(false, type);
		this.setSelectedMathShape(null);
	}

	public void setSelectedMathShape(IMathShape mathShape) {
		IMathShape oldMathShape = this.mathShape;
		this.mathShape = mathShape;
		Calculator calc = null;
		
		//save the forumla of the last shape
//		if( oldMathShape != null ) {
//			calc = getCalculator(oldMathShape);
//
//			String forumla = calc.getForumla();
//			shapeIdToForumla.put(oldMathShape.getId(), forumla);
//			if( this.mathShape == null)
//				calc.clearForumla();
//				return;
//
//		}
		
		if( this.mathShape != null) {
//			
//			ScratchPanel scratchPad = getScratchPad(this.mathShape);
//			scratchPad.transferFocus();
			
			//deselect the pad
			ShapeCanvas shapeCanvas = this.getShapeCanvas(this.mathShape);
			shapeCanvas.requestFocusInWindow();

			
			this.highLightShape(this.mathShape);
			this.selectInTable(this.mathShape);
		} else {
			
		}
		

	}
	
	
	protected ScratchPanel getScratchPad(IMathShape mathShape) {
		//save the forumla of the last shape
		ScratchPanel sc = null;
		if( this.mathShape instanceof I3D ) {
			sc = getScratchPadPanels().get(UIUtils._3D);
		} else {
			sc = getScratchPadPanels().get(UIUtils._2D);
		}
		return sc;
	}
	
	protected Calculator getCalculator(IMathShape mathShape) {
		//save the forumla of the last shape
		Calculator calc = null;
		if( this.mathShape instanceof I3D ) {
			calc = calculators.get(UIUtils._3D);
		} else {
			calc = calculators.get(UIUtils._2D);
		}
		return calc;
	}
	
	protected ShapeCanvas getShapeCanvas(IMathShape mathShape) {
		//save the forumla of the last shape
		ShapeCanvas shapeCanvas = null;
		if( this.mathShape instanceof I3D ) {
			shapeCanvas = shapeCanvases.get(UIUtils._3D);
		} else {
			shapeCanvas = shapeCanvases.get(UIUtils._2D);
		}
		return shapeCanvas;
	}
	
	protected void highLightShape(IMathShape mathShape) {
		

		String type;
		if (mathShape instanceof I3D) {
			type = UIUtils._3D;
			ShapeCanvas shapeCanvas = shapeCanvases.get(type);
			ArrayList<IMathShape> mathShapes = shapeCanvas.getMathShapes();
			for (IMathShape ms : mathShapes) {
				if (ms instanceof I3D && !ms.equals(mathShape)) {
					ms.setShowCornerPoints(false);
					
				}

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

		Calculator calculator = calculators.get(type);
		calculator.getSumTextField().setEnabled(true);
		calculator.setForumla(mathShape.getFormula());
		calculator.setResultValue(mathShape.getResult());
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
		//  System.out.println("ShapeCanvas.addShape()" + label.getName());

		ShapeCanvas sc = getShapeCanvases().get(type);

		String id = new Integer(generator.nextInt(1000)).toString();
		
		if (label.getName().equals(UIUtils.CIRCLE)) {
			MathEllipse t = new MathEllipse(dropPoint.x, dropPoint.y, 200, 200);
			t.setId(id);
			t.setHasDecorations(true);
			sc.addShape(t);
			this.setSelectedMathShape(t);
		} else if (label.getName().equals(UIUtils.RECTANGLE)) {
			MathRectangle t = new MathRectangle(dropPoint.x, dropPoint.y, 100,
					100);
			t.setHasDecorations(true);
			t.setId(id);
			sc.addShape(t);
			this.setSelectedMathShape(t);
		} else if (label.getName().equals(UIUtils.TRIANGLE)) {
			MathTriangle t = new MathTriangle(dropPoint.x, dropPoint.y, 200);
			t.setId(id);
			t.setHasDecorations(true);
			sc.addShape(t);
			this.setSelectedMathShape(t);
		} else if (label.getName().equals(UIUtils.RECTANGLE3D)) {
			MathRectangle3D t = new MathRectangle3D(dropPoint.x, dropPoint.y);
			MathShape3DFocusListener mathShape3DFocusListener = new MathShape3DFocusListener(t);
			t.getSurfaceAreaTextField().addFocusListener(mathShape3DFocusListener);
			t.getRatioTextField().addFocusListener(mathShape3DFocusListener);
			t.getLengthTextField().addFocusListener(mathShape3DFocusListener);
			t.setId(id);
			t.setName(UIUtils._3D);
			t.getAddButton().addActionListener(add3dAction);
			sc.addShape(t);
			this.setSelectedMathShape(t);
		} else if (label.getName().equals(UIUtils.SPHERE3D)) {
			MathSphere3D t = new MathSphere3D(dropPoint.x, dropPoint.y);
			MathShape3DFocusListener mathShape3DFocusListener = new MathShape3DFocusListener(t);
			t.getSurfaceAreaTextField().addFocusListener(mathShape3DFocusListener);
			t.getRadiusTextField().addFocusListener(mathShape3DFocusListener);
			t.getRatioTextField().addFocusListener(mathShape3DFocusListener);
			t.setId(id);
			t.setName(UIUtils._3D);
			sc.addShape(t);
			t.getAddButton().addActionListener(add3dAction);
			t.setLocation(dropPoint);
			sc.repaint();
			this.setSelectedMathShape(t);
		} else if (label.getName().equals(UIUtils.CYLINDER3D)) {
			MathCylinder3D t = new MathCylinder3D(dropPoint.x, dropPoint.y);
			MathShape3DFocusListener mathShape3DFocusListener = new MathShape3DFocusListener(t);
			t.getSurfaceAreaTextField().addFocusListener(mathShape3DFocusListener);
			t.getRadiusTextField().addFocusListener(mathShape3DFocusListener);
			t.getRatioTextField().addFocusListener(mathShape3DFocusListener);
			t.setId(id);
			t.setName(UIUtils._3D);
			t.getAddButton().addActionListener(add3dAction);
			sc.addShape(t);
			t.setLocation(dropPoint);
			sc.repaint();
			this.setSelectedMathShape(t);
		}
		sc.repaint();
	}

	private class MathShape3DFocusListener implements FocusListener {

		private Math3DShape math3DShape;

		public MathShape3DFocusListener(Math3DShape math3DShape) {
			this.math3DShape = math3DShape;
		}

		@Override
		public void focusGained(FocusEvent e) {
			selectAllShapes(false, UIUtils._3D);
			this.math3DShape.setShowCornerPoints(true);
			setSelectedMathShape(this.math3DShape);
		}

		@Override
		public void focusLost(FocusEvent e) {
			
		}
	    
	}
	
	Action add3dAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JXButton button = (JXButton) e.getSource();
			String mathShapeId = (String) button
					.getClientProperty(UIUtils.SHAPE_ID);

			ShapeCanvas shapeCanvas = shapeCanvases.get(UIUtils._3D);
			
			Math3DShape ashape = null;
			
			ArrayList<IMathShape> mathShapes = shapeCanvas.getMathShapes();
			for (IMathShape ms : mathShapes) {
				if(ms.isShowCornerPoints())
					ashape = (Math3DShape) ms;
			}
			
			
			ashape.checkForError();
			if (ashape.getError() == true)
				return;

			String volume = ashape.getVolumeValueLabel().getText();
			String surfaceArea = ashape.getSurfaceAreaTextField().getText();
			String ratio = ashape.getRatioTextField().getText();
			String shape = ashape.getType();
			String shapeId = ashape.getId();

			JXTable table = getComputationTables().get(UIUtils._3D);
			ShapeCanvas sc = getShapeCanvases().get(UIUtils._3D);
			DefaultTableModel model = (DefaultTableModel) table.getModel();

			Integer id = new Integer(model.getRowCount() + 1);
			model.addRow(new Object[] { id, shape, ratio, surfaceArea, volume, shapeId });
			int indexOf = sc.getMathShapes().indexOf(ashape);
//			ashape.set(Integer.toString(model.getRowCount() - 1));
//			sc.getMathShapes().add(indexOf, ashape);

			table.getSelectionModel().setSelectionInterval(
					model.getRowCount() - 1, model.getRowCount() - 1);
		}
	};

	public void addComputationTable(String type, JXTable computationTable) {
		
//		if( type.equals(UIUtils._2D))
//		computationTable.getSelectionModel().addListSelectionListener(new TwoDeeTableSelectionListener());
//		
		
//		computationTable.getSelectionModel().addListSelectionListener(new ForumlaTableSelectionListener(type));
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
	
	public void selectAllShapes(boolean isSelected, String type) {
		ShapeCanvas shapeCanvas = shapeCanvases.get(type);
		
		Calculator calculator = calculators.get(type);
		
		for (IMathShape shape : shapeCanvas.getMathShapes()) {
			if(UIUtils._3D.equals(type) && shape instanceof I3D) {
				shape.setShowCornerPoints(isSelected);
				shape.repaint();
			} else if( UIUtils._2D.equals(type)) {
				shape.setShowCornerPoints(isSelected);
				shape.repaint();
			}
			
			if( isSelected == false ) {
				shapeIdToForumla.put(shape.getId(), calculator.getForumla());

			}
		}
		shapeCanvas.repaint();
		shapeCanvas.revalidate();
		
//		shapeCanvas.selectAll(isSelected, type);
		
	}

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

		calculator.resetLabel();
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
				doEqualsAction((JComponent) e.getSource());

		}
	};

	public class ForumlaTableSelectionListener implements ListSelectionListener {

		private String type;

		public ForumlaTableSelectionListener(String type) {
			this.type = type;
		}
		
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if( e.getValueIsAdjusting() ) {
				
				int firstIndex = e.getFirstIndex();

				DefaultTableModel model = (DefaultTableModel) getComputationTables()
						.get(UIUtils._2D).getModel();

				String shapeId = (String) model.getValueAt(firstIndex, 5);
				
				
				String newForumla = shapeIdToForumla.get(shapeId);
				Calculator calc = calculators.get(type);
				
				if (newForumla != null) {
					calc.setForumla(newForumla);
				} else {
					calc.clearForumla();
				}
			}
		}

	}
	
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
			if( key.equals(UIUtils._3D)) {
				doa.setThreeDMathShapes(this.convertTo3DDataObj(this.getShapeCanvases().get(key).getMathShapes()));
			} else {
				doa.setTwoDMathShapes(this.getShapeCanvases().get(key).getMathShapes());
			}
			
			doa.setTablesObjects(getTableObjects(key));
			
			String padText = StringUtils.stripToNull(getScratchPadPanels().get(key).getEditor().getText());
			
			if( padText != null ) {
				doa.setScratchPadText(padText);
			}
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
				
				String scratchPadText = dataStoreObj.getScratchPadText();
				if( scratchPadText != null ) {
					getScratchPadPanels().get(key).getEditor().setText(scratchPadText);
				}
				
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

	public void removeSelectedShape() {
		String type = null;
		if( this.getMathSelectedShape() instanceof I3D ) {
			type = UIUtils._3D;
		} else {
			type = UIUtils._2D;
		}
		
		ShapeCanvas shapeCanvas = this.getShapeCanvases().get(type);
		
		this.removeRowsTable(this.getMathSelectedShape(), type);
		shapeCanvas.removeSelectedShape(this.getMathSelectedShape());
		
		this.checkCalcTextField(shapeCanvas, type);
		
		
	}

	private void checkCalcTextField(ShapeCanvas shapeCanvas, String type) {
		Calculator calculator = calculators.get(type);
			ArrayList<IMathShape> mathShapes = shapeCanvas.getMathShapes();
			
			if( mathShapes.isEmpty() ) {
				calculator.getSumTextField().setEnabled(false);
				calculator.getSumTextField().setText(null);
				return;
			}
			
			for (IMathShape ms : mathShapes) {
				if( ms.isShowCornerPoints() ) {
					calculator.getSumTextField().setEnabled(true);
					calculator.getSumTextField().setText(null);
					return;
				} else {
					calculator.getSumTextField().setEnabled(false);
					calculator.getSumTextField().setText(null);
				}
			}
		
	}

	private void removeRowsTable(IMathShape mathSelectedShape, String type) {
		JXTable jxTable = this.computationTables.get(type);
		DefaultTableModel model = (DefaultTableModel) jxTable.getModel();
		
		Vector<Vector> dataVector = model.getDataVector();
		
		if( dataVector.isEmpty() )
			return;
		
		List<ComputationDataObj> cdos = new ArrayList<ComputationDataObj>();
		
		for (Vector data : dataVector) {
			ComputationDataObj computationDataObj = new ComputationDataObj(data, type);
			if( !computationDataObj.getShapeId().equals(mathSelectedShape.getId()) )  
				cdos.add(computationDataObj);
		}
		
		this.recalculateModel(model, cdos, type);
	}

	public void recalculateModel(DefaultTableModel model, List<ComputationDataObj> newDataSet, String type) {
		//remove all the rows
		while (model.getRowCount() > 0){
			model.removeRow(0);
		}
		
		if( type.equals(UIUtils._3D)) {
			int i = 0;
			for (ComputationDataObj nco : newDataSet) {
				
				model.addRow(new Object[] { i++, nco.getName(), nco.getRatio(), nco.getSurfaceArea(), nco.getVolume(), nco.getShapeId() });

//				table.getSelectionModel().setSelectionInterval(
//						model.getRowCount() - 1, model.getRowCount() - 1);
				
				
			}
			
		} else {
			Float newSum = null;
			
			for (ComputationDataObj nco : newDataSet) {
				
				if( newSum == null) {
					newSum = new Float(nco.getValue());
					nco.setOperation("+");
				} else {
					if( nco.getOperation().equals("+"))
						newSum = newSum + new Float(nco.getValue());
					else
						newSum = newSum - new Float(nco.getValue());
				}
				
				model.addRow(new Object[] { new Integer(model.getRowCount() + 1),
						nco.getName(), new Float(nco.getValue()),
						new Float(newSum), nco.getOperation(), nco.getShapeId()});
				
			}
		}
		
	}

	public void addScratchPanel(ScratchPanel scratchPanel) {
		getScratchPadPanels().put(scratchPanel.getType(), scratchPanel);
	}

	public void setScratchPadPanels(Map<String, ScratchPanel> scratchPadPanels) {
		this.scratchPadPanels = scratchPadPanels;
	}

	public Map<String, ScratchPanel> getScratchPadPanels() {
		return scratchPadPanels;
	}


}
