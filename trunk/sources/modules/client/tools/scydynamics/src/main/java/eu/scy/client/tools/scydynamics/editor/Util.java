package eu.scy.client.tools.scydynamics.editor;

import java.awt.Color;
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

	/** This method return a value that describes the brightness of a given {@link java.awt.Color}.
	 *  The brightness value is subjective to the human eye, taking into account different sensitivenesses
	 *  to red, green and blue.
	 *  
	 * @param color		The {@link java.awt.Color} of which the brightness will be calculated.
	 * @return			The brightness of the given color, as a double value between 0.0 and 255.0.
	 */
	public static double getBrightness(Color color) {
		return 0.2126*color.getRed()+0.7152*color.getGreen()+0.0722*color.getBlue();
	}
	
	public static JButton _createJButton(String label, String command, String iconPropertyName, ActionListener listener) {
		URL url = JTools.getSysResourceImage(iconPropertyName);
		JButton button = new JButton(label, new ImageIcon(url));
		button.setActionCommand(command);
		button.addActionListener(listener);
		return button;
	}
	
	public static JButton createJButton(String label, String command, String iconFileName, ActionListener listener) {
		//URL url = JTools.getSysResourceImage(iconPropertyName);
		Icon icon = getImageIcon(iconFileName);
		JButton button = new JButton(label, icon);
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
