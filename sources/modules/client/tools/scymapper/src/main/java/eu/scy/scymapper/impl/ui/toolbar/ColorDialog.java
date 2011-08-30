package eu.scy.scymapper.impl.ui.toolbar;

import eu.scy.scymapper.impl.ui.Localization;
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

@SuppressWarnings("serial")
public class ColorDialog extends JDialog implements
java.awt.event.ActionListener {
	
	private static final Color[] colors = {Color.WHITE, new Color(250,128,114), new Color(255,105,180),
	new Color(255,165,0), new Color(255,255,0), new Color(238,130,238), new Color(124,252,0), new Color(154,205,50),
	new Color(102,205,170), new Color(100,149,237), new Color(255,222,173)};
	private static final int size = 20;
	private final ConceptMapToolBar toolbar;
	
	public ColorDialog(Frame frame, Point position, ConceptMapToolBar toolbar) {
		super(frame, true);
		this.toolbar = toolbar;
		this.setTitle(Localization.getString("Mainframe.Toolbar.Background.Tooltip"));
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
		
		colorButton = new JButton(Localization.getString("Mainframe.Input.Close"));
		colorButton.setActionCommand("cancel");
		colorButton.addActionListener(this);
		this.getContentPane().add(colorButton);
		pack();
		this.setLocation(position.x, position.y);		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("color")) {
			toolbar.setBackgroundColorOfSelection(((JButton)e.getSource()).getBackground());
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
