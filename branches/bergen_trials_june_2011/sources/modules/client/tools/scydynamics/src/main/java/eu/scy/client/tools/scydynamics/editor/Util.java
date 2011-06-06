package eu.scy.client.tools.scydynamics.editor;

import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import colab.um.tools.JTools;

public class Util {
	
	public static JButton createJButton(String label, String command, String iconPropertyName, ActionListener listener) {
		URL url = JTools.getSysResourceImage(iconPropertyName);
		JButton button = new JButton(label, new ImageIcon(url));
		button.setActionCommand(command);
		button.addActionListener(listener);
		return button;
	}
	
	public static JToggleButton createJToggleButton(String label, String command, String iconPropertyName, ActionListener listener) {
		URL url = JTools.getSysResourceImage(iconPropertyName);
		JToggleButton button = new JToggleButton(label, new ImageIcon(url));
		button.setActionCommand(command);
		button.addActionListener(listener);
		return button;
	}

}
