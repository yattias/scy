package eu.scy.scymapper.impl.shapes.nodes;

import eu.scy.scymapper.api.shapes.INodeShape;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 25.jun.2009
 * Time: 12:38:07
 */
public class ImageShape extends INodeShape {
	private transient BufferedImage image;
	private URL url;

	public ImageShape(String url) throws IOException {
		this(ImageShape.class.getResource(url));
	}

	public ImageShape(URL url) throws IOException {
		this.url = url;
		image = ImageIO.read(url);
	}

	private ImageShape(BufferedImage i) {
		image = i;
	}

	private Object readResolve() throws IOException {
		image = ImageIO.read(url);
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
}
