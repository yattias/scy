package eu.scy.client.tools.scydynamics.editor;

import java.awt.MediaTracker;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import colab.um.tools.JTools;
import java.util.logging.Logger;

public class Util {
	
	private static final String resourcesLocation = "/eu/scy/client/tools/scydynamics/resources/";
	private final static Logger DEBUGLOGGER = Logger.getLogger(Util.class.getName());

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
	
	public static Icon getImageIcon(String name) {
		return getImageIcon(Util.class.getResource(resourcesLocation + name));
	}
	
	public static ImageIcon getImageIcon(URL url) {
		//DEBUGLOGGER.info("loading "+url);
		if (url != null) {
			ImageIcon image = new ImageIcon(url);
			while (image.getImageLoadStatus() != MediaTracker.COMPLETE) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
				}
			}
			return image;
		} else {
			return null;
		}
	}

}
