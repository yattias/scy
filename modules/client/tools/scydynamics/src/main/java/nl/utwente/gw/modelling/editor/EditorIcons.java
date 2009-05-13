package nl.utwente.gw.modelling.editor;

import java.awt.MediaTracker;
import java.net.URL;

import javax.swing.ImageIcon;

public class EditorIcons {
	
	private static final String imageLocation = "/nl/utwente/gw/modelling/resources/";
	
	public static ImageIcon getImageIcon(String name)
	{
		URL url = EditorIcons.class.getResource(imageLocation + name);
		if (url == null)
		{
			System.out.println("EditorIcons: couldn't load "+name);
		}
		return getImageIcon(url);
	}
	
	/**
	 * A utility function to get a icon from the resources in the program.
	 * 
	 * @param url
	 *           The url of the image to get
	 * @return ImageIcon
	 */
	public static ImageIcon getImageIcon(URL url)
	{
		if (url != null)
		{
			ImageIcon image = new ImageIcon(url);
			while (image.getImageLoadStatus() != MediaTracker.COMPLETE)
			{
				// System.out.println("Image " + url + " is not yet loaded (" +
				// image.getImageLoadStatus() + "), waiting..");
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException ex)
				{
				}
			}
			return image;
		}
		else
		{
			// System.out.println("Failed to find url for " + name);
			return null;
		}
	}
}
