package eu.scy.scymapper.impl.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ProposalEntry extends JPanel {

	private JPanel compound = null;
	
	private JLabel iconLabel = null;
	
	private JLabel textLabel = null;
	
	private KeywordLabel linkLabel = null;

	public ProposalEntry(ImageIcon icon, JLabel textLabel) {
		this(icon, textLabel, new JLabel());
	}

	public ProposalEntry(ImageIcon icon, JLabel textLabel, JLabel linkLabel) {
		this.setLayout(new BorderLayout());

		compound = new JPanel(new GridLayout(2, 1));

		if (icon != null) {
			iconLabel = new JLabel(icon);
		} else {
			iconLabel = new JLabel("+");
		}
		textLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		
		linkLabel.setVerticalAlignment(SwingConstants.TOP);
		linkLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		compound.add(textLabel);
		compound.add(linkLabel);

		add(iconLabel, BorderLayout.WEST);
		add(compound, BorderLayout.CENTER);
	}

	public JLabel getIconLabel() {
		return iconLabel;
	}
	
	public JLabel getTextLabel() {
		return textLabel;
	}
	
	public JLabel getLinkLabel() {
		return linkLabel;
	}
	
	public boolean hasLinkLabel() {
		return (linkLabel == null) ? false : true;
	}
}
