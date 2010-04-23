package eu.scy.scymapper.impl.shapes.nodes;

import eu.scy.scymapper.api.shapes.INodeShape;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 25.jun.2009
 * Time: 12:38:07
 */
public class ImageShape extends INodeShape {
	private transient BufferedImage image;
	private byte[] imageData;
	private String format;

	public ImageShape(String classPathUrl) throws IOException {
		this(ImageShape.class.getResource(classPathUrl));
	}

	private ImageShape(URL url) throws IOException {
		image = ImageIO.read(url);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		String fn = url.getFile();
		String ext = fn.substring(fn.lastIndexOf('.') + 1, fn.length());

		format = getFormatNameBySuffix(ext);
		if (format == null) format = "jpeg";

		ImageIO.write(image, format, baos);
		imageData = baos.toByteArray();
	}

	private Object readResolve() throws IOException {
		image = ImageIO.read(new ByteArrayInputStream(imageData));
		return this;
	}

	@Override
	public Point getConnectionPoint(Point point, Rectangle bounds) {
		double x = point.x;
		double y = point.y;
		if (point.x > bounds.getMaxX()) x = bounds.getMaxX();
		else if (point.x < bounds.getMinX()) x = bounds.getMinX();

		if (point.y > bounds.getMaxY()) y = bounds.getMaxY();
		else if (point.y < bounds.getMinY()) y = bounds.getMinY();

		return new Point((int) x, (int) y);
	}

	/**
	 * @param bounds The bounds of the resulting shape
	 * @return The factor by which the shape are scaled along the X axis direction
	 */
	private double getSX(Rectangle bounds) {
		return bounds.width / (double) image.getWidth();
	}

	/**
	 * @param bounds The bounds of the resulting shape
	 * @return The factor by which the shape are scaled along the Y axis direction
	 */
	private double getSY(Rectangle bounds) {
		return bounds.height / (double) image.getHeight();
	}

	@Override
	public synchronized void paint(Graphics g, Rectangle bounds) {

		Graphics2D g2d = (Graphics2D) g.create();
		AffineTransform at = AffineTransform.getScaleInstance(getSX(bounds), getSY(bounds));

		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);


		g2d.translate(bounds.x, bounds.y);

		g2d.drawRenderedImage(image, at);

	}

	private static String getFormatNameBySuffix(String suf) {
		try {
			Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suf);
			if (!iter.hasNext()) {
				return null;
			}
			// Use the first reader
			ImageReader reader = iter.next();
			return reader.getFormatName();
		} catch (IOException e) {
		}

		return null;
	}
}
