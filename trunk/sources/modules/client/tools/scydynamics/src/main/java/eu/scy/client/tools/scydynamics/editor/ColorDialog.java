package eu.scy.client.tools.scydynamics.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JDialog;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;

@SuppressWarnings("serial")
public class ColorDialog extends JDialog implements
java.awt.event.ActionListener {
	
	private static final Color[] colors = {
		Color.GRAY,
		new Color(184, 62, 62),
		new Color(182, 111, 45),
		new Color(182, 179, 62),
		new Color(113, 178, 50),
		new Color(51, 178, 45),
		new Color(49, 178, 114),
		new Color(45, 181, 181),
		new Color(49, 114, 182),
		new Color(50, 50, 182),
		new Color(110, 49, 178),
		new Color(180, 52, 175),
		new Color(181, 48, 113)};		
	private static final int size = 20;
	private VariableDialog variableDialog;
	
	public static Color getColor(int index) {
		index = index % colors.length;
		return colors[index];
	}
	
	public ColorDialog(Frame frame, Point position, VariableDialog variableDialog, ResourceBundleWrapper bundle) {
		super(frame, true);
		this.setTitle(bundle.getString("VARIABLEDIALOG_CHOOSECOLOR"));
		this.variableDialog = variableDialog;
		this.getContentPane().setLayout(new FlowLayout());
		
		JButton colorButton;		
		for (Color color : colors) {
                        BufferedImage image = new BufferedImage(size, size,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.setColor(color);
			g.fillRect(0, 0, size, size);
			colorButton = new JButton(new javax.swing.ImageIcon(image));
			colorButton.setPreferredSize(new Dimension(20,20));
			colorButton.setBackground(color);
			colorButton.setActionCommand("color");
			colorButton.setSelected(false);
			colorButton.addActionListener(this);
			this.getContentPane().add(colorButton);
		}
		
		colorButton = new JButton(bundle.getString("VARIABLEDIALOG_CANCEL"));
		colorButton.setActionCommand("cancel");
		colorButton.addActionListener(this);
		this.getContentPane().add(colorButton);
		pack();
		this.setLocation(position.x-this.getWidth()/2, position.y);		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("color")) {
			variableDialog.setNewColor(((JButton)e.getSource()).getBackground());
			setVisible(false);
			setEnabled(false);
			dispose();
		} else if (e.getActionCommand().equals("cancel")) {
			setVisible(false);
			setEnabled(false);
			dispose();
		}
	}

}
