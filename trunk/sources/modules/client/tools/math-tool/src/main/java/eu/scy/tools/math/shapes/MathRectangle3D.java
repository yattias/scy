package eu.scy.tools.math.shapes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;

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

public class MathRectangle3D extends Math3DShape {
	
	private JXLabel lengthLabel;
	private JXTextField lengthTextField;
	protected JXLabel heightLabel;
	protected JXLabel widthLabel;
	private JXLabel heightValueLabel;
	private JXLabel widthValueLabel;



	public MathRectangle3D(int x, int y) {
		super(x,y);
	}

	protected void init() {
		
		
		JXPanel allPanel = new JXPanel(new BorderLayout(0,0));
		
		allPanel.setOpaque(false);
		ImageIcon icon = (ImageIcon) Images.Rectangle3d.getIcon();
		
		JXLabel iconLabel = new JXLabel(icon);
//		iconLabel.setSize(iconLabel.getSize());
		allPanel.add(iconLabel,BorderLayout.CENTER);
		
		labelPanel = new JXPanel(new MigLayout("insets 0 0 0 0"));
		labelPanel.setOpaque(false);
//		labelPanel.setBackground(Color.green);
		
		setupVolumeLabel();
		
		
		heightLabel = new JXLabel("H = ");
		labelPanel.add(heightLabel);
		 heightValueLabel = new JXLabel("5.0");
		labelPanel.add(heightValueLabel,"wrap");
		
		
		widthLabel = new JXLabel("W = ");
		labelPanel.add(widthLabel);
		 widthValueLabel = new JXLabel("5.0");
		labelPanel.add(widthValueLabel,"wrap");
		
		lengthLabel = new JXLabel("L = ");
		labelPanel.add(lengthLabel);
		
		lengthTextField = new JXTextField();
		lengthTextField.setColumns(TEXTFIELD_LENGTH);
		
		labelPanel.add(lengthTextField,"wrap");
		

		setupCommonInputs();
		
		addButtonPanel();
		
		
		
		
		allPanel.add(labelPanel,BorderLayout.EAST);
		
		this.add(allPanel,BorderLayout.CENTER);
		this.setSize(allPanel.getPreferredSize());
		
	}
	
	@Override
	public boolean checkForError() {
		boolean checkForError = super.checkForError();
		
		String length = lengthTextField.getText();
		
		if( length.isEmpty() || StringUtils.isNumeric(length) == false ) {
			lengthTextField.setBackground(UIUtils.ERROR_SHAPE_COLOR);
			errorLabel.setForeground(UIUtils.ERROR_SHAPE_COLOR);
			setHasError(true);
		} else {
			lengthTextField.setBackground(UIUtils.NONSHAPE_SHAPE_COLOR);
			errorLabel.setForeground(UIUtils.NONSHAPE_SHAPE_COLOR);
			setHasError(false);
		}
		
		
		if(checkForError = true) {
			setHasError(true);
			errorLabel.setForeground(UIUtils.ERROR_SHAPE_COLOR);
		}
		return isHasError();
	}



	

	@Override
	public String getType() {
		return "3D Rectangle";
	}

}
