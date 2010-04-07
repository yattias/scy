package eu.scy.scymapper.impl.ui.toolbar;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author bjoerge
 * @created 05.feb.2010 17:06:08
 */
public class ColorChooserButton extends JButton implements ChangeListener {
	JPopupMenu popup;
	JColorChooser colorChooser;
	private Color displayColor;

	public ColorChooserButton() {
		this("");
	}

	ColorChooserButton(String label) {
		super(label);

		popup = new JPopupMenu(label);
		colorChooser = new JColorChooser();
		colorChooser.getSelectionModel().addChangeListener(this);


		popup.add(colorChooser);

		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				popup.show(ColorChooserButton.this, 0, ColorChooserButton.this.getHeight());
			}
		});
	}

	void setDisplayColor(Color c) {
		this.displayColor = c;
		this.colorChooser.setColor(c);
		repaint();
	}


	@Override
	public void stateChanged(ChangeEvent e) {
		// User selected a color
		popup.setVisible(false);
		Color c = colorChooser.getColor();
		colorSelected(c);
	}

	void colorSelected(Color c) {
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Icon icon = getIcon();
		if (icon != null && displayColor != null) {
			g.setColor(displayColor);
			Insets i = getInsets();
			g.fillRect(i.left, i.top, icon.getIconWidth(), icon.getIconWidth());
			icon.paintIcon(this, g, i.left, i.top);
		}
	}
}
