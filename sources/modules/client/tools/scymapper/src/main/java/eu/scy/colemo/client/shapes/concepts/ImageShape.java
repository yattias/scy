package eu.scy.colemo.client.shapes.concepts;

import eu.scy.colemo.client.shapes.ConceptShape;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;

import org.w3c.dom.Element;
import org.apache.batik.bridge.ViewBox;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 25.jun.2009
 * Time: 12:38:07
 */
public class ImageShape extends ConceptShape {
    private BufferedImage image;

    public ImageShape(BufferedImage i) {
        image = i;
    }

    @Override
    public Point getConnectionPoint(Point point, Rectangle bounds) {
        double x = point.x;
        double y = point.y;
        if (point.x > bounds.getMaxX()) x = bounds.getMaxX();
        else if (point.x < bounds.getMinX()) x = bounds.getMinX();

        if (point.y > bounds.getMaxY()) y = bounds.getMaxY();
        else if (point.y < bounds.getMinY()) y = bounds.getMinY();

        return new Point((int) x, (int)y);
    }

    /**
     * @param bounds The bounds of the resulting shape
     * @return The factor by which the shape are scaled along the X axis direction
     */
    private double getSX(Rectangle bounds) {
        return bounds.width / (double)image.getWidth();
    }

    /**
     * @param bounds The bounds of the resulting shape
     * @return The factor by which the shape are scaled along the Y axis direction
     */
    private double getSY(Rectangle bounds) {
        return bounds.height / (double)image.getHeight();
    }
    @Override
    public synchronized void paint(Graphics g, Rectangle bounds) {

        Graphics2D g2d = (Graphics2D) g.create();
        AffineTransform at = AffineTransform.getScaleInstance(getSX(bounds), getSY(bounds));

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.drawRenderedImage(image, at);

        g2d.dispose();
    }
}
