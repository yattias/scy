package eu.scy.colemo.client.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 17.jun.2009
 * Time: 16:24:56
 */
public class ShapedComponent extends JComponent {
    private Shape shape;

    public ShapedComponent(Shape shape) {
        this.shape = shape;
    }

    public ShapedComponent() {
    }

    public void paint(Graphics g) {
        super.paint(g);

        if (shape == null) return;

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(0, 200, 255, 100));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(
                2f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND,
                2f));

        Rectangle shapeBounds = shape.getBounds();

        double sy = getHeight() / (double) shapeBounds.height;
        double sx = getWidth() / (double) shapeBounds.width;

        if (sx == 0) sx = 1;
        if (sy == 0) sy = 1;

        if (shapeBounds.x < 0 || shapeBounds.y < 0) g2.translate(shapeBounds.x * -sx, shapeBounds.y * -sy);
        if (shapeBounds.height == 0) {
            g2.translate(getHeight() / -2, 0);
            g2.draw(shape);
        }
        if (shapeBounds.width == 0) {
            g2.translate(0, getWidth() / -2);
            g2.draw(shape);
        }

        AffineTransform at = AffineTransform.getScaleInstance(sx, sy);
        g2.draw(at.createTransformedShape(shape));

    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
