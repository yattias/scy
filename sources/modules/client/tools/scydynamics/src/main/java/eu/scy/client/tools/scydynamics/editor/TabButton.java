package eu.scy.client.tools.scydynamics.editor;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicButtonUI;

@SuppressWarnings("serial")
public class TabButton extends JButton {

	public TabButton() {
		super(Util.getImageIcon("popout_12.png"));
		setUI(new BasicButtonUI());
		int size = 13;
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		setPreferredSize(new Dimension(size, size));
		setToolTipText("pop out");
		setContentAreaFilled(false);
		setFocusable(false);
		setBorder(BorderFactory.createEtchedBorder());
		setBorderPainted(false);
		//addMouseListener(TabPanel.buttonMouseListener);
		setRolloverEnabled(true);
	}
}