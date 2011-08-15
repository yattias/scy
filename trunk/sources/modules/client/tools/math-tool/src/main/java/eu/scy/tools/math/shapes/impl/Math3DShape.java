package eu.scy.tools.math.shapes.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberRange;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;

import eu.scy.tools.math.doa.json.ICylinderToolbarShape;
import eu.scy.tools.math.doa.json.IRectanglarPrismToolbarShape;
import eu.scy.tools.math.doa.json.IToolbarShape;
import eu.scy.tools.math.doa.result.ShapeResult;
import eu.scy.tools.math.shapes.I3D;
import eu.scy.tools.math.shapes.IMathShape;
import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.border.DashBorder;


public abstract class Math3DShape extends JXPanel implements IMathShape, I3D{


	protected static final String NEEDS_A_NUMBER = "Needs a Number.";
	protected static final String WRONG_VALUE = "Incorrect Calculation.";
	
	protected String oldResult;
	
	protected static final int TEXTFIELD_LENGTH = 4;
	private String id;
	protected JXLabel surfaceAreaLabel;
	private JXTextField surfaceAreaTextField;
	private JXButton addButton;
	protected JXPanel labelPanel;
	protected JXLabel ratioLabel;
	private JTextField ratioTextField;
	protected JXLabel volumeLabel;
	private JComboBox volumeValueCombo;
	protected JXLabel errorLabel;
	private boolean error = false;
	private List<Action> shapeListeners = new ArrayList<Action>();
	private boolean showCornerPoints;
	private String formula;
	protected IToolbarShape shape;
	protected ArrayList<IToolbarShape> shapesCollection;
	protected JXLabel iconLabel;
	private String iconName;
	protected JComboBox itemCombo;
	protected HashMap<String, ShapeResult> resultMap = new HashMap<String, ShapeResult>();
	
	public Math3DShape(Point point, String iconName, String id) {
		this.setLocation(point);
		this.setIconName(iconName);
		this.setId(id);
		init();
		
		
	}
	
	public Math3DShape(Point location) {
		this.setLocation(location);
		init();
		
		
	}

	public Math3DShape(int x, int y, String iconName, String id) {
		this.setIconName(iconName);
		this.setLocation(x, y);
		this.setId(id);
		init();
	}

	public Math3DShape(ArrayList<IToolbarShape> shapes, int x, int y, String iconName, String id) {
		this.setIconName(iconName);
		this.setLocation(x, y);
		this.setId(id);
		
		
		this.shape = shapes.get(0);
		this.shapesCollection = shapes;
		
		itemCombo = new JComboBox();
		
		//Vector<String> volumes = new Vector<String>();
		for (IToolbarShape toolBarShape : shapes) {
			//volumes.add(toolBarShape.getVolume());
			itemCombo.addItem(toolBarShape.getVolume());
			itemCombo.setPrototypeDisplayValue(toolBarShape.getVolume());
		}
		
		
		itemCombo.setSelectedIndex(0);
		itemCombo.setPreferredSize(new Dimension(50, itemCombo.getPreferredSize().height));

		itemCombo.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				JComboBox source = (JComboBox) e.getSource();
		        int selectedIndex = source.getSelectedIndex();
		        updateLabels(selectedIndex);
				
			}
		});
		itemCombo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox source = (JComboBox) e.getSource();
		        int selectedIndex = source.getSelectedIndex();
		        updateLabels(selectedIndex);

			}

			
		});
		
		setVolumeValueLabel(itemCombo);
		init();
		updateLabels(0);
//		iconLabel.setIcon(Images.getIcon(this.shape.getCanvasIcon()));
//		this.setIconName(this.shape.getCanvasIcon());
	}
	
	protected abstract void updateLabels(int selectedIndex);
	
	protected void setupVolumeLabel() {
		volumeLabel = new JXLabel("V = ");
		
		labelPanel.add(volumeLabel);
		labelPanel.add(getVolumeLabelCombo(), "width 60::60");
		labelPanel.add(new JXLabel(UIUtils.unitsVolume), "wrap");
	}
	protected void setupCommonInputs() {
		
		surfaceAreaLabel = new JXLabel("SA = ");
		
		labelPanel.add(surfaceAreaLabel);
		
		setSurfaceAreaTextField(new JXTextField());
		getSurfaceAreaTextField().setColumns(4);
		
		labelPanel.add(surfaceAreaLabel);
		labelPanel.add(getSurfaceAreaTextField());
		labelPanel.add(new JXLabel(UIUtils.unitsSurfaceArea),"wrap");
		
		ratioLabel = new JXLabel("SA/V Ratio = ");
		
		
		setRatioTextField(new JXTextField());
		getRatioTextField().setColumns(TEXTFIELD_LENGTH);
		
		labelPanel.add(ratioLabel);
		labelPanel.add(getRatioTextField(),"wrap");
		
		labelPanel.add(new JXLabel(" "),"wrap");
	}
	
	 protected void addButtonPanel() {
		JXPanel buttonPanel = new JXPanel(new GridLayout(2,1, 1,1));
//		buttonPanel.setBackground(Color.yellow);
		errorLabel = new JXLabel(NEEDS_A_NUMBER);
		errorLabel.setForeground(UIUtils.NONSHAPE_SHAPE_COLOR);
		addButton = new JXButton("Add");
		addButton.setEnabled(false);
	
		
		buttonPanel.setOpaque(false);
		buttonPanel.add(errorLabel);
		buttonPanel.add(getAddButton());
		labelPanel.add(buttonPanel,"growx, span");
	}
	
	public void attachListeners() {
//		Collections.reverse(shapeListeners);
		for (Action action : shapeListeners) {
			addButton.addActionListener(action);
		}
	}
	
		
		public boolean checkForError() {
			String surfaceArea = getSurfaceAreaTextField().getText();
			boolean hasError1;
			boolean hasError2;
			
			String surfaceAreaStripped = StringUtils.stripToNull(surfaceArea);
			
			if( surfaceAreaStripped == null || StringUtils.isAlpha(surfaceAreaStripped )) {
				getSurfaceAreaTextField().setBackground(UIUtils.ERROR_SHAPE_COLOR);
				hasError1 = true;
			} else {
				try {
					Double.parseDouble(surfaceAreaStripped);
					getSurfaceAreaTextField().setBackground(UIUtils.NONSHAPE_SHAPE_COLOR);
					hasError1 = false;
				} catch (NumberFormatException e) {
					getSurfaceAreaTextField().setBackground(UIUtils.ERROR_SHAPE_COLOR);
					hasError1 = true;
				}
				
				
			}
			
			
			
			String ratio = getRatioTextField().getText();
			
			String ratioStripped = StringUtils.stripToNull(ratio);
			
			if( ratioStripped == null || StringUtils.isAlpha(ratioStripped )) {
				getRatioTextField().setBackground(UIUtils.ERROR_SHAPE_COLOR);
				hasError2 = true;
			} else{
				try {
					Double.parseDouble(ratioStripped);
					getRatioTextField().setBackground(UIUtils.NONSHAPE_SHAPE_COLOR);
					hasError2 = false;
				} catch (NumberFormatException e) {
					getRatioTextField().setBackground(UIUtils.ERROR_SHAPE_COLOR);
					hasError2 = true;
				}
			
			}
			
		
			
			if((hasError1 || hasError2)) {
				errorLabel.setForeground(UIUtils.ERROR_SHAPE_COLOR);
				setError(true);
			} else {
				errorLabel.setForeground(UIUtils.NONSHAPE_SHAPE_COLOR);
				setError(false);
			}
			
			if( surfaceAreaStripped != null) {
				
				NumberRange saRange = new NumberRange(new Double(shape.getSurfaceAreaMinValue()), new Double(shape.getSurfaceAreaMaxValue()));
				if( !saRange.containsNumber(new Double(surfaceAreaStripped))) {
					getSurfaceAreaTextField().setBackground(UIUtils.ERROR_SHAPE_COLOR);
					errorLabel.setText(WRONG_VALUE);
					errorLabel.setForeground(UIUtils.ERROR_SHAPE_COLOR);
					setError(true);
				}
			}
			
		
			if( ratioStripped != null ) {
				NumberRange raRange = new NumberRange(new Double(shape.getSurfaceAreaRatioMinValue()), new Double(shape.getSurfaceAreaRatioMaxValue()));
				if( !raRange.containsNumber(new Double(ratioStripped))) {
					getRatioTextField().setBackground(UIUtils.ERROR_SHAPE_COLOR);
					errorLabel.setText(WRONG_VALUE);
					errorLabel.setForeground(UIUtils.ERROR_SHAPE_COLOR);
					setError(true);
				}
			}
	
			return getError();
			
		}
	
	
	protected void init() {
		setLayout(new BorderLayout(1, 1));
		this.setOpaque(false);
		this.setBackground(Color.white);
		this.setName(UIUtils._3D);
	}
	
	@Override
	public void addX(int x) {
		double x1 = this.getLocation().getX();
		x1 += x;
		setLocation((int) x1, (int) this.getLocation().getY());
	}

	@Override
	public void addY(int y) {
		double y1 = this.getLocation().getY();
		y1 += y;
		setLocation((int) this.getLocation().getX(), (int) y1);
	}

	@Override
	public String toString() {
		return "name: " + getName() + "id: " + getId();
	}
	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public boolean isShowCornerPoints() {
		return this.showCornerPoints ;
	}

	@Override
	public void setShowCornerPoints(boolean showCornerPoints) {
		this.showCornerPoints = showCornerPoints;
		if( showCornerPoints ) {
			this.setBorder(new DashBorder(UIUtils.SHAPE_3D_DASH_BORDER_COLOR));
			this.getAddButton().setEnabled(showCornerPoints);
		} else {
			this.setBorder(null);
			this.getAddButton().setEnabled(showCornerPoints);
		}
	}

	@Override
	public Point[] getPoints() {
		return null;
	}

	@Override
	public void setPoints(Point[] points) {
	}

	@Override
	public int isHitOnEndPoints(Point eventPoint) {
		return -1;
	}

	@Override
	public void setFillColor(Color fillColor) {
	}

	@Override
	public Color getFillColor() {
		return null;
	}

	@Override
	public String getType() {
		return "null";
	}

	@Override
	public void createCornerPoints() {
	}

	@Override
	public void setCornerPointRectangles(Rectangle[] cornerPointRectangles) {
	}

	@Override
	public Rectangle[] getCornerPointRectangles() {
		return null;
	}

	@Override
	public boolean contains(Point point) {
		return this.getBounds().contains(point);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	public void setAddButton(JXButton addButton) {
		this.addButton = addButton;
	}

	public JXButton getAddButton() {
		return addButton;
	}

	public void setVolumeValueLabel(JComboBox volumeValueLabel) {
		this.volumeValueCombo = volumeValueLabel;
	}

	public JComboBox getVolumeLabelCombo() {
		return volumeValueCombo;
	}

	public void setRatioTextField(JTextField ratioTextField) {
		this.ratioTextField = ratioTextField;
	}

	public JTextField getRatioTextField() {
		return ratioTextField;
	}

	public void setSurfaceAreaTextField(JXTextField surfaceAreaTextField) {
		this.surfaceAreaTextField = surfaceAreaTextField;
	}

	public JXTextField getSurfaceAreaTextField() {
		return surfaceAreaTextField;
	}

	
	public void setError(boolean error) {
		this.error = error;
	}

	@Override
	public boolean getError() {
		return error;
	}

	public void setShapeListeners(List<Action> shapeListeners) {
		this.shapeListeners = shapeListeners;
	}

	public List<Action> getShapeListeners() {
		return shapeListeners;
	}
	
	@Override
	public String getFormula() {
		return this.formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	@Override
	public String getResult() {
		return this.oldResult;
	}

	@Override
	public void setResult(String result) {
		this.oldResult = result;
	}

	@Override
	public void setHasDecorations(boolean hasDecorations) {
		// TODO Auto-generated method stub
		
	}
	
	public void setIconLabel(JXLabel iconLabel) {
		this.iconLabel = iconLabel;
	}

	public JXLabel getIconLabel() {
		return iconLabel;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public String getIconName() {
		return iconName;
	}
}
