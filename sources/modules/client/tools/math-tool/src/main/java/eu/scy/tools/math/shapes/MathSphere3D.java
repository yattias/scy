package eu.scy.tools.math.shapes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;

import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.images.Images;

public class MathSphere3D extends Math3DShape {

	

	
	
	private JXTextField radiusTextField;
	private JXLabel radiusLabel;
	private JXLabel surfaceAreaLabel;

	public MathSphere3D(int x, int y) {
		super(x,y);
	}

	protected void init() {

		JXPanel allPanel = new JXPanel(new BorderLayout(0, 0));

		allPanel.setOpaque(false);
		ImageIcon icon = (ImageIcon) Images.Sphere3d.getIcon();

		JXLabel iconLabel = new JXLabel(icon);
		// iconLabel.setSize(iconLabel.getSize());
		allPanel.add(iconLabel, BorderLayout.CENTER);

	    labelPanel = new JXPanel(new MigLayout("insets 0 0 0 0"));
		labelPanel.setOpaque(false);
		// labelPanel.setBackground(Color.green);

		setupVolumeLabel();

	
		radiusLabel = new JXLabel("R = ");
		labelPanel.add(radiusLabel);
		
		radiusTextField = new JXTextField();
		radiusTextField.setColumns(TEXTFIELD_LENGTH);
		
		labelPanel.add(radiusTextField,"wrap");
		
		setupCommonInputs();
		
		addButtonPanel();

		allPanel.add(labelPanel, BorderLayout.EAST);

		this.add(allPanel, BorderLayout.CENTER);
		this.setSize(allPanel.getPreferredSize());

	}
	
	@Override
	public boolean checkForError() {
		boolean checkForError = super.checkForError();
		
		String radius = radiusTextField.getText();
		
		if( radius.isEmpty() || StringUtils.isNumeric(radius) == false ) {
			radiusTextField.setBackground(UIUtils.ERROR_SHAPE_COLOR);
			errorLabel.setForeground(UIUtils.ERROR_SHAPE_COLOR);
			setHasError(true);
		} else {
			radiusTextField.setBackground(UIUtils.NONSHAPE_SHAPE_COLOR);
			errorLabel.setForeground(UIUtils.NONSHAPE_SHAPE_COLOR);
			setHasError(false);
		}
		
		
		if(checkForError == true) {
			setHasError(true);
			errorLabel.setForeground(UIUtils.ERROR_SHAPE_COLOR);
		}
		
		return isHasError();
	}


	@Override
	public String getType() {
		return "Sphere 3D";
	}

}
