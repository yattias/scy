package eu.scy.tools.math.shapes.impl;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberRange;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;

import eu.scy.tools.math.doa.json.ICylinderToolbarShape;
import eu.scy.tools.math.doa.json.ISphereToolbarShape;
import eu.scy.tools.math.shapes.IMathCylinder3D;
import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.images.Images;

public class MathCylinder3D extends Math3DShape implements IMathCylinder3D {
	
	private JXTextField radiusTextField;
	private JXLabel radiusLabel;
	private Component heightLabel;
	private JXLabel heightValueLabel;
	
	private JXLabel iconLabel;


	public MathCylinder3D(int x, int y) {
		super(x,y);
	}

	public MathCylinder3D(Point location) {
		super(location);
	}


	public MathCylinder3D(ICylinderToolbarShape shape, int x, int y) {
		super(x,y);
		this.shape = shape;
		this.getVolumeValueLabel().setText(this.shape.getVolume());
		this.getHeightValueLabel().setText(((ICylinderToolbarShape) this.shape).getHeight());
		this.getIconLabel().setIcon(Images.getIcon(this.shape.getCanvasIcon()));
	}

	protected void init() {

		super.init();
		JXPanel allPanel = new JXPanel(new BorderLayout(0, 0));

		allPanel.setOpaque(false);
		ImageIcon icon = (ImageIcon) Images.Cylinder3dLarge.getIcon();

		setIconLabel(new JXLabel(icon));
		// iconLabel.setSize(iconLabel.getSize());
		allPanel.add(getIconLabel(), BorderLayout.CENTER);

	    labelPanel = new JXPanel(new MigLayout("insets 0 0 0 0"));
		labelPanel.setOpaque(false);
		// labelPanel.setBackground(Color.green);

		setupVolumeLabel();

	
		heightLabel = new JXLabel("H = ");
		labelPanel.add(heightLabel);
		setHeightValueLabel(new JXLabel("10"));
		
		labelPanel.add(getHeightValueLabel());
		labelPanel.add(new JXLabel(UIUtils.unitsGeneral),"wrap");
		
		radiusLabel = new JXLabel("R = ");
		labelPanel.add(radiusLabel);
		
		radiusTextField = new JXTextField();
		getRadiusTextField().setColumns(TEXTFIELD_LENGTH);
		
		labelPanel.add(getRadiusTextField());
		labelPanel.add(new JXLabel(UIUtils.unitsGeneral),"wrap");

		setupCommonInputs();
		
		addButtonPanel();

		allPanel.add(labelPanel, BorderLayout.EAST);

		this.add(allPanel, BorderLayout.CENTER);
		this.setSize(allPanel.getPreferredSize());

		this.getAddButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}
	

	@Override
	public boolean checkForError() {
		boolean checkForError = super.checkForError();
		
		String radius = getRadiusTextField().getText();
		String radiusStripped = StringUtils.stripToNull(radius);
		
		if( radiusStripped == null || StringUtils.isAlpha(radiusStripped) ) {
			getRadiusTextField().setBackground(UIUtils.ERROR_SHAPE_COLOR);
			errorLabel.setForeground(UIUtils.ERROR_SHAPE_COLOR);
			setError(true);
		} else {
			
			try {
				Double.parseDouble(radiusStripped);
				getRadiusTextField().setBackground(UIUtils.NONSHAPE_SHAPE_COLOR);
				errorLabel.setForeground(UIUtils.NONSHAPE_SHAPE_COLOR);
				setError(false);
			} catch (NumberFormatException e) {
				getRadiusTextField().setBackground(UIUtils.ERROR_SHAPE_COLOR);
				errorLabel.setForeground(UIUtils.ERROR_SHAPE_COLOR);
				setError(true);
			}
			
		}
		
		if( radiusStripped != null ) {
			NumberRange radiusRange = new NumberRange(new Double(((ICylinderToolbarShape) shape).getRadiusMinValue()), new Double(((ICylinderToolbarShape) shape).getRadiusMaxValue()));
			
			boolean containsRadius = radiusRange.containsDouble(new Double(radiusStripped));
			
			if( !containsRadius ) {
				getRadiusTextField().setBackground(UIUtils.ERROR_SHAPE_COLOR);
				errorLabel.setText(WRONG_VALUE);
				errorLabel.setForeground(UIUtils.ERROR_SHAPE_COLOR);
				setError(true);
			}
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
		return "Cylinder";
	}

	@Override
	public String getHeightValue() {
		return getHeightValueLabel().getText();
	}

	@Override
	public String getWidthValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRadiusValue() {
		return getRadiusTextField().getText();
	}

	public JXTextField getRadiusTextField() {
		return radiusTextField;
	}

	public void setHeightValueLabel(JXLabel heightValueLabel) {
		this.heightValueLabel = heightValueLabel;
	}

	public JXLabel getHeightValueLabel() {
		return heightValueLabel;
	}

	public void setIconLabel(JXLabel iconLabel) {
		this.iconLabel = iconLabel;
	}

	public JXLabel getIconLabel() {
		return iconLabel;
	}
	
}
