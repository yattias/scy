package eu.scy.client.tools.formauthor;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

public class IconCreator {
	/**
	 * Returns an ImageIcon, or null if the path was invalid.
	 * 
	 * @throws MalformedURLException
	 */
	protected static ImageIcon createImageIcon(URL url, String description)
			throws MalformedURLException {
		java.net.URL imgURL = url;
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + url.toString());
			return null;
		}
	}
}
