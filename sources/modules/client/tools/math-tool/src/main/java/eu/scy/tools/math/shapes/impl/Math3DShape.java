package eu.scy.tools.math.shapes.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;

import eu.scy.tools.math.shapes.I3D;
import eu.scy.tools.math.shapes.IMathShape;
import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.border.DashBorder;


public class Math3DShape extends JXPanel implements IMathShape, I3D{


	protected static final int TEXTFIELD_LENGTH = 4;
	private String id;
	protected JXLabel surfaceAreaLabel;
	private JXTextField surfaceAreaTextField;
	private JXButton addButton;
	protected JXPanel labelPanel;
	protected JXLabel ratioLabel;
	private JTextField ratioTextField;
	protected JXLabel volumeLabel;
	private JXLabel volumeValueLabel;
	protected JXLabel errorLabel;
	private boolean hasError = false;
	private List<Action> shapeListeners = new ArrayList<Action>();
	
	
	public Math3DShape(int x, int y) {
		setLayout(new BorderLayout(1, 1));
		this.setBackground(Color.white);
		this.setLocation(x, y);
		this.setOpaque(false);
		init();
	}
	
	protected void setupVolumeLabel() {
		volumeLabel = new JXLabel("V = ");
		setVolumeValueLabel(new JXLabel("100.0"));
		labelPanel.add(volumeLabel);
		labelPanel.add(getVolumeValueLabel(), "wrap");
	}
	protected void setupCommonInputs() {
		
		surfaceAreaLabel = new JXLabel("SA = ");
		
		labelPanel.add(surfaceAreaLabel);
		
		setSurfaceAreaTextField(new JXTextField());
		getSurfaceAreaTextField().setColumns(4);
		
		labelPanel.add(surfaceAreaLabel);
		labelPanel.add(getSurfaceAreaTextField(),"wrap");
		
		ratioLabel = new JXLabel("Ratio = ");
		
		
		setRatioTextField(new JXTextField());
		getRatioTextField().setColumns(TEXTFIELD_LENGTH);
		
		labelPanel.add(ratioLabel);
		labelPanel.add(getRatioTextField(),"wrap");
		
		labelPanel.add(new JXLabel(" "),"wrap");
	}
	
	protected void addButtonPanel() {
		JXPanel buttonPanel = new JXPanel(new GridLayout(2,1, 1,1));
//		buttonPanel.setBackground(Color.yellow);
		errorLabel = new JXLabel("Needs a Number.");
		errorLabel.setForeground(UIUtils.NONSHAPE_SHAPE_COLOR);
		addButton = new JXButton("Add");
		addButton.putClientProperty(UIUtils.SHAPE, this);
		
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
			String sa = getSurfaceAreaTextField().getText();
			boolean hasError1;
			boolean hasError2;
			if( sa.isEmpty() ||  StringUtils.isNumeric(sa) == false) {
				getSurfaceAreaTextField().setBackground(UIUtils.ERROR_SHAPE_COLOR);
				hasError1 = true;
			} else {
				getSurfaceAreaTextField().setBackground(UIUtils.NONSHAPE_SHAPE_COLOR);
				hasError1 = false;
			}
			
			String ratio = getRatioTextField().getText();
			
			if( ratio.isEmpty()  ||  StringUtils.isNumeric(ratio) == false) {
				getRatioTextField().setBackground(UIUtils.ERROR_SHAPE_COLOR);
				hasError2 = true;
			} else {
				getRatioTextField().setBackground(UIUtils.NONSHAPE_SHAPE_COLOR);
				
				hasError2 = false;
			}
			
			if((hasError1 || hasError2)) {
				errorLabel.setForeground(UIUtils.ERROR_SHAPE_COLOR);
				setHasError(true);
			} else {
				errorLabel.setForeground(UIUtils.NONSHAPE_SHAPE_COLOR);
				setHasError(false);
			}
			
			return isHasError();
			
		}
	
	
	protected void init() {
		
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
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public boolean isShowCornerPoints() {
		return false;
	}

	@Override
	public void setShowCornerPoints(boolean showCornerPoints) {
		if( showCornerPoints )
			this.setBorder(new DashBorder(UIUtils.SHAPE_3D_DASH_BORDER_COLOR));
		else 
			this.setBorder(null);
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

	public void setVolumeValueLabel(JXLabel volumeValueLabel) {
		this.volumeValueLabel = volumeValueLabel;
	}

	public JXLabel getVolumeValueLabel() {
		return volumeValueLabel;
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

	
	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}

	@Override
	public boolean isHasError() {
		return hasError;
	}

	public void setShapeListeners(List<Action> shapeListeners) {
		this.shapeListeners = shapeListeners;
	}

	public List<Action> getShapeListeners() {
		return shapeListeners;
	}
}
