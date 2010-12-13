package eu.scy.tools.math.shapes.impl;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;

import eu.scy.tools.math.shapes.IMathCylinder3D;
import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.images.Images;

public class MathCylinder3D extends Math3DShape implements IMathCylinder3D {
	
	private JXTextField radiusTextField;
	private JXLabel radiusLabel;
	private Component heightLabel;
	private JXLabel heightValueLabel;


	public MathCylinder3D(int x, int y) {
		super(x,y);
	}

	public MathCylinder3D(Point location) {
		super(location);
	}


	protected void init() {

		super.init();
		JXPanel allPanel = new JXPanel(new BorderLayout(0, 0));

		allPanel.setOpaque(false);
		ImageIcon icon = (ImageIcon) Images.Cylinder3dLarge.getIcon();

		JXLabel iconLabel = new JXLabel(icon);
		// iconLabel.setSize(iconLabel.getSize());
		allPanel.add(iconLabel, BorderLayout.CENTER);

	    labelPanel = new JXPanel(new MigLayout("insets 0 0 0 0"));
		labelPanel.setOpaque(false);
		// labelPanel.setBackground(Color.green);

		setupVolumeLabel();

	
		heightLabel = new JXLabel("H = ");
		labelPanel.add(heightLabel);
		
		heightValueLabel = new JXLabel("10");
		
		labelPanel.add(heightValueLabel,"wrap");
		
		radiusLabel = new JXLabel("R = ");
		labelPanel.add(radiusLabel);
		
		radiusTextField = new JXTextField();
		getRadiusTextField().setColumns(TEXTFIELD_LENGTH);
		
		labelPanel.add(getRadiusTextField(),"wrap");
		
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
		
		if( radius.isEmpty() || StringUtils.isNumeric(radius) == false ) {
			getRadiusTextField().setBackground(UIUtils.ERROR_SHAPE_COLOR);
			errorLabel.setForeground(UIUtils.ERROR_SHAPE_COLOR);
			setError(true);
		} else {
			getRadiusTextField().setBackground(UIUtils.NONSHAPE_SHAPE_COLOR);
			errorLabel.setForeground(UIUtils.NONSHAPE_SHAPE_COLOR);
			setError(false);
		}
		
		
		if(checkForError == true) {
			setError(true);
			errorLabel.setForeground(UIUtils.ERROR_SHAPE_COLOR);
		}
		
		return getError();
	}

	@Override
	public String getType() {
		return "Cylinder";
	}

	@Override
	public String getHeightValue() {
		return heightValueLabel.getText();
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
	
}
