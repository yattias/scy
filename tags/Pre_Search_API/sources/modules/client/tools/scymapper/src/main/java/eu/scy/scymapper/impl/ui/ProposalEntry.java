package eu.scy.scymapper.impl.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ProposalEntry extends JPanel {

	private JPanel compound = null;
	
	private JLabel iconLabel = null;
	
	private JLabel firstTextLabel = null;
	
	private JLabel secondTextLabel = null;

	public ProposalEntry(ImageIcon icon, JLabel textLabel) {
		this(icon, textLabel, new JLabel());
	}

	public ProposalEntry(ImageIcon icon, JLabel firstLabel, JLabel secondLabel) {
		this.setLayout(new BorderLayout());
		this.secondTextLabel = secondLabel;
		this.firstTextLabel = firstLabel;
		compound = new JPanel(new GridLayout(2, 1));
		if (icon != null) {
			iconLabel = new JLabel(icon);
		} else {
			iconLabel = new JLabel("+");
		}
		firstLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		secondLabel.setVerticalAlignment(SwingConstants.TOP);

		compound.add(firstLabel);
		compound.add(secondLabel);

		add(iconLabel, BorderLayout.WEST);
		add(compound, BorderLayout.CENTER);
		setBackground(Color.WHITE);
		compound.setBackground(Color.WHITE);
		firstLabel.setBackground(Color.WHITE);
		secondLabel.setBackground(Color.WHITE);
	}

	public JLabel getIconLabel() {
		return iconLabel;
	}
	
	public JLabel getTextLabel() {
		return firstTextLabel;
	}
	
	public JLabel getLinkLabel() {
		return secondTextLabel;
	}
	
	public boolean hasLinkLabel() {
		return (secondTextLabel == null) ? false : true;
	}
}
