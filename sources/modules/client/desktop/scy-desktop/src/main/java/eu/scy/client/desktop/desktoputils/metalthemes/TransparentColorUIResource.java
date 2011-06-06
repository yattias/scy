package eu.scy.client.desktop.desktoputils.metalthemes;

import java.awt.Color;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import javax.swing.plaf.ColorUIResource;

/**
 * <p>Title: Colab layout test</p>
 * <p>Description: </p>
 * The standard
 * ColorUIResource does not handle transparent color (the alpha is not copied). This class does it best to handle the alpha component as well. This is being done by coping all the function of java.awt.Color, which have something to do with transparency.
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: University of Twente</p>
 * @author Jakob Sikken
 * @version 1.0
 */

public class TransparentColorUIResource extends ColorUIResource
{
	static final long serialVersionUID = -1582302340739804913L;
	private Color realColor;

	public TransparentColorUIResource(Color color)
	{
		super(color);
		realColor = new Color(color.getRed(), color.getGreen(), color.getBlue(),
									 color.getAlpha());
	}

	public int getAlpha()
	{
		return realColor.getAlpha();
	}

	public int getRGB()
	{
		return realColor.getRGB();
	}

	public int hashCode()
	{
		return realColor.hashCode();
	}

	public boolean equals(Object obj)
	{
		return realColor.equals(obj);
	}

	public float[] getRGBColorComponents(float[] compArray)
	{
		return realColor.getRGBColorComponents(compArray);
	}

	public float[] getComponents(float[] compArray)
	{
		return realColor.getComponents(compArray);
	}

	public float[] getColorComponents(float[] compArray)
	{
		return realColor.getColorComponents(compArray);
	}

	public float[] getComponents(ColorSpace cspace, float[] compArray)
	{
		return realColor.getComponents(cspace, compArray);
	}

	public float[] getColorComponents(ColorSpace cspace, float[] compArray)
	{
		return realColor.getColorComponents(cspace, compArray);
	}

	public Color brighter()
	{
		return realColor.brighter();
	}

	public Color darker()
	{
		return realColor.darker();
	}

	public int getTransparency()
	{
		return realColor.getTransparency();
	}

	public synchronized PaintContext createContext(ColorModel cm, Rectangle r,
																  Rectangle2D r2d,
																  AffineTransform xform,
																  RenderingHints hints)
	{
		return realColor.createContext(cm, r, r2d, xform, hints);
	}
}