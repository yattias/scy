package colab.vt.whiteboard.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.jdom.Element;

import colab.utils.GraphicsUtilities;
import colab.vt.whiteboard.utils.XmlUtils;

public class WhiteboardImage extends WhiteboardSimpleShape
{
	private static final long serialVersionUID = 8087214219365846713L;
	private static final Logger logger = Logger.getLogger(WhiteboardImage.class.getName());

	private Image image;
	private String imageBytes64;

	public WhiteboardImage()
	{
		super(XmlNames.image);
	}

	private void setImage(Image image)
	{
		this.image = image;
		setEnd(xBegin + image.getWidth(null), yBegin + image.getHeight(null));
	}

	public void setImageFile(File imageFile) throws IOException
	{
		Image loadedImage = GraphicsUtilities.loadCompatibleImage(imageFile.toURI().toURL());
		if (loadedImage != null)
		{
			setImage(loadedImage);
			imageBytes64 = new String(Base64.encodeBase64(getBytes(imageFile)));
		}
	}
	
	public void setImageBytes(byte[] bytes) throws IOException
	{
		ByteArrayInputStream inputStream = null;
		inputStream = new ByteArrayInputStream(bytes);
      BufferedImage newImage = null;
      newImage = ImageIO.read(inputStream);
		if (newImage!=null)
		{
			image = GraphicsUtilities.toCompatibleImage(newImage);
			imageBytes64 = new String(Base64.encodeBase64(bytes));
		}
	}

	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		Rectangle bounds = getDrawBounds();
		Color fillColor = g2d.getBackground();
		Color lineColor = g2d.getColor();

		if (image != null)
		{
			ImageObserver observer = null;
			g2d.drawImage(image, bounds.x, bounds.y, fillColor, observer);
		}

		g2d.setColor(lineColor);
		g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	@Override
	public Element getStatus()
	{
		Element status = super.getStatus();
//		XmlUtils.addXmlTag(status, XmlNames.image, imageBytes);
		XmlUtils.addXmlTag(status, XmlNames.image, imageBytes64);
		return status;
	}

	@Override
	public void setStatus(Element status)
	{
		super.setStatus(status);
		try
		{
			setImageBytes(XmlUtils.getBytesValueFromXmlTag(status, XmlNames.image));
		}
		catch (IOException e)
		{
			logger.log(Level.WARNING,"failed to load image from status",e);
		}
	}

	private byte[] getBytes(File file) throws IOException
	{
		byte[] bytes = new byte[(int) file.length()];
		FileInputStream stream = null;
		try
		{
			stream = new FileInputStream(file);
			stream.read(bytes);
		}
		finally
		{
			if (stream != null)
			{
				try
				{
					stream.close();
				}
				catch (Exception e1)
				{
				}
			}
		}
		return bytes;
	}

}
