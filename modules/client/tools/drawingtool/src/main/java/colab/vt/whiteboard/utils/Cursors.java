package colab.vt.whiteboard.utils;

import java.awt.Cursor;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

public class Cursors
{
	private static final Logger logger = Logger.getLogger(Cursors.class.getName());
	private static final String cursorLocation = "/colab/vt/whiteboard/resources/";
	private static final Cursor penCursor = loadCursor("cursorPen.png", "pen", 11, 22);
	private static final Cursor moveCursor = loadCursor("cursorMove.png", "rotate", 16, 16);
	private static final Cursor rotateCursor = loadCursor("cursorRotate.png", "rotate", 16, 16);
	private static final Cursor deleteCursor = loadCursor("cursorDelete.png", "delete", 16, 16);
	private static final Cursor deleteActionCursor = loadCursor("cursorDeleteAction.png",
				"deleteAction", 16, 16);
	private static final Cursor textCursor = loadCursor("cursorText.png", "text", 16, 20);
	private static final Cursor textEditCursor = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
	private static final Cursor infoCursor = loadCursor("cursorInfo.png", "info", 16, 16);
	private static final Cursor tagPointerCursor = loadCursor("cursorTagPointer.png", "info", 10, 10);
	private static final Cursor tagPointerEditAreaCursor = loadCursor("cursorTagPointerEditArea.png", "info", 10, 10);

	public static Cursor loadCursor(String fileName, String name, int x, int y)
	{
		ImageIcon aIcon = getImageIcon(fileName);
		return Toolkit.getDefaultToolkit()
					.createCustomCursor(aIcon.getImage(), new Point(x, y), name);
	}

	public static ImageIcon getImageIcon(String name)
	{
		URL url = Cursors.class.getResource(cursorLocation + name);
		if (url == null)
		{
			logger.warning("cannot find icon resource " + name);
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
		// System.out.println("Failed to find url for " + name);
		return null;
	}

	public static String getCursorLocation()
	{
		return cursorLocation;
	}

	public static Cursor getPenCursor()
	{
		return penCursor;
	}

	public static Cursor getMoveCursor()
	{
		return moveCursor;
	}

	public static Cursor getRotateCursor()
	{
		return rotateCursor;
	}

	public static Cursor getDeleteCursor()
	{
		return deleteCursor;
	}

	public static Cursor getDeleteActionCursor()
	{
		return deleteActionCursor;
	}

	public static Cursor getTextCursor()
	{
		return textCursor;
	}

	public static Cursor getTextEditCursor()
	{
		return textEditCursor;
	}

	public static Cursor getInfoCursor()
	{
		return infoCursor;
	}

	public static Cursor getTagPointerCursor()
	{
		return tagPointerCursor;
	}
	
	public static Cursor getTagPointerEditAreaCursor()
	{
		return tagPointerEditAreaCursor;
	}
	
	public static Cursor getScaleCursor(double angle)
	{
		int sector = (int) Math.round((angle) / (Math.PI / 4));
		sector = sector % 8;
		switch (sector)
		{
			case 0:
				return Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
			case 1:
				return Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
			case 2:
				return Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
			case 3:
				return Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
			case 4:
				return Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
			case 5:
				return Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
			case 6:
				return Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
			case 7:
				return Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
			default:
				logger.warning("Unexpected angle: " + angle + ", sector: " + sector);
		}
		return null;
	}
}
