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

public class ColorDialog extends javax.swing.JDialog implements
java.awt.event.ActionListener {
	
	private static final Color[] colors = { Color.BLACK, Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW};
	private static final int size = 20;
	private VariableDialog variableDialog;

	
	public ColorDialog(Frame frame, Point position, VariableDialog variableDialog) {
		super(frame, false);
		this.setLocation(position);
		this.setTitle("Please choose a color...");
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
		
		colorButton = new JButton("cancel");
		colorButton.setActionCommand("cancel");
		colorButton.addActionListener(this);
		this.getContentPane().add(colorButton);
		pack();
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
