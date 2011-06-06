package eu.scy.tools.math.shapes.impl;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;

import eu.scy.tools.math.doa.json.IRectanglarPrismToolbarShape;
import eu.scy.tools.math.shapes.IMathRectangle3D;
import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.images.Images;

public class MathRectangle3D extends Math3DShape implements IMathRectangle3D {
	
	private static final long serialVersionUID = 1L;
	private JXLabel lengthLabel;
	private JXTextField lengthTextField;
	protected JXLabel heightLabel;
	protected JXLabel widthLabel;
	private JXLabel heightValueLabel;
	private JXLabel widthValueLabel;



	public MathRectangle3D(Point point, String iconName, String id) {
		super(point,iconName, id);
	}

	public MathRectangle3D(Point location) {
		super(location);
		// TODO Auto-generated constructor stub
	}

	public MathRectangle3D(IRectanglarPrismToolbarShape shape, int x, int y, String id) {
		super(x,y,shape.getCanvasIcon(),id);
		this.shape = shape;
		
		getVolumeValueLabel().setText(this.shape.getVolume());
		getHeightValueLabel().setText(((IRectanglarPrismToolbarShape) this.shape).getHeight());
		getWidthValueLabel().setText(((IRectanglarPrismToolbarShape) this.shape).getWidth());
//		iconLabel.setIcon(Images.getIcon(this.shape.getCanvasIcon()));
//		this.setIconName(this.shape.getCanvasIcon());
	}

	protected void init() {
		super.init();
		
		JXPanel allPanel = new JXPanel(new BorderLayout(0,0));
		
		allPanel.setOpaque(false);
//		ImageIcon icon = (ImageIcon) Images.Rectangle3dLarge.getIcon();
		
		setIconLabel(new JXLabel(Images.getIcon(this.getIconName())));
//		iconLabel.setSize(iconLabel.getSize());
		allPanel.add(getIconLabel(),BorderLayout.CENTER);
		
		labelPanel = new JXPanel(new MigLayout("insets 0 0 0 0"));
		labelPanel.setOpaque(false);
//		labelPanel.setBackground(Color.green);
		
		setupVolumeLabel();
		
		
		heightLabel = new JXLabel("H = ");
		labelPanel.add(heightLabel);
		 setHeightValueLabel(new JXLabel("5.0"));
		labelPanel.add(getHeightValueLabel());
		labelPanel.add(new JXLabel(UIUtils.unitsGeneral),"wrap");

		
		widthLabel = new JXLabel("W = ");
		labelPanel.add(widthLabel);
		 setWidthValueLabel(new JXLabel("5.0"));
		labelPanel.add(getWidthValueLabel());
		labelPanel.add(new JXLabel(UIUtils.unitsGeneral),"wrap");

		
		lengthLabel = new JXLabel("L = ");
		labelPanel.add(lengthLabel);
		
		lengthTextField = new JXTextField();
		getLengthTextField().setColumns(TEXTFIELD_LENGTH);
		
		labelPanel.add(getLengthTextField());
		labelPanel.add(new JXLabel(UIUtils.unitsGeneral),"wrap");


		setupCommonInputs();
		
		addButtonPanel();
		
		
		
		
		allPanel.add(labelPanel,BorderLayout.EAST);
		
		this.add(allPanel,BorderLayout.CENTER);
		this.setSize(allPanel.getPreferredSize());
		
	}
	
	@Override
	public boolean checkForError() {
		boolean checkForError = super.checkForError();
		
		String length = getLengthTextField().getText();
		
		String lenghtStripped = StringUtils.stripToNull(length);
		
		if( lenghtStripped == null || StringUtils.isAlpha(lenghtStripped) ) {
			getLengthTextField().setBackground(UIUtils.ERROR_SHAPE_COLOR);
			errorLabel.setText(NEEDS_A_NUMBER);
			errorLabel.setForeground(UIUtils.ERROR_SHAPE_COLOR);
			setError(true);
		} else {
			
			try {
				Double.parseDouble(lenghtStripped);
				getLengthTextField().setBackground(UIUtils.NONSHAPE_SHAPE_COLOR);
				errorLabel.setForeground(UIUtils.NONSHAPE_SHAPE_COLOR);
				setError(false);
			} catch (NumberFormatException e) {
				getLengthTextField().setBackground(UIUtils.ERROR_SHAPE_COLOR);
				errorLabel.setText(NEEDS_A_NUMBER);
				errorLabel.setForeground(UIUtils.ERROR_SHAPE_COLOR);
				setError(true);
			}
		}
		
		if( lenghtStripped != null && !lenghtStripped.equals(((IRectanglarPrismToolbarShape) shape).getLength())) {
			getLengthTextField().setBackground(UIUtils.ERROR_SHAPE_COLOR);
			errorLabel.setText(WRONG_VALUE);
			errorLabel.setForeground(UIUtils.ERROR_SHAPE_COLOR);
			setError(true);
		}
		
		
		if(checkForError == true) {
			setError(true);
			errorLabel.setForeground(UIUtils.ERROR_SHAPE_COLOR);
			return getError();
		}
		
		
		
	
		
		return getError();
	}



	

	@Override
	public String getType() {
		return "Rectanglar Prism";
	}

	@Override
	public String getHeightValue() {
		return getHeightValueLabel().getText();
	}

	@Override
	public String getWidthValue() {
		return getWidthValueLabel().getText();
	}

	public JXTextField getLengthTextField() {
		return lengthTextField;
	}

	@Override
	public String getLengthValue() {
		return this.lengthTextField.getText();
	}

	public void setHeightValueLabel(JXLabel heightValueLabel) {
		this.heightValueLabel = heightValueLabel;
	}

	public JXLabel getHeightValueLabel() {
		return heightValueLabel;
	}

	public void setWidthValueLabel(JXLabel widthValueLabel) {
		this.widthValueLabel = widthValueLabel;
	}

	public JXLabel getWidthValueLabel() {
		return widthValueLabel;
	}


}
