package eu.scy.client.desktop.desktoputils;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lars
 */
public class UiUtils {
   private final static Logger LOGGER = Logger.getLogger(UiUtils.class.getName());

    public static BufferedImage createThumbnail(Container container, Dimension originalSize, Dimension targetSize) {
        try {
	    Dimension newSize;
	    if (originalSize.height < originalSize.width) {
		newSize = new Dimension(originalSize.height, originalSize.height);
	    } else {
		newSize = new Dimension(originalSize.width, originalSize.width);
	    }
	    LOGGER.log(Level.INFO, "creating thumbnail, size {0}, will be {1}", new Object[]{originalSize, newSize});
            BufferedImage bi = new BufferedImage(newSize.width, newSize.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bi.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            container.paint(g2d);
            g2d.dispose();
            return resizeBufferedImage(bi,targetSize);
        } catch (Exception ex) {
            // something went wrong...
            LOGGER.warning(ex.getMessage());
            return null;
        }
    }

    public static BufferedImage resizeBufferedImage(BufferedImage image, Dimension targetSize){
        try {
            Double factor;
            // to preserver the aspect ratio:
            // (the result may deviate from the requested target size)
            if ((image.getWidth() - targetSize.width) < (image.getHeight() - targetSize.height)) {
                // the difference in height is larger than in width
                // use the height to calculate the scaling factor
                factor = 1.0 * targetSize.height / image.getHeight();
            } else {
                factor = 1.0 * targetSize.width / image.getWidth();
            }
            AffineTransform scale = AffineTransform.getScaleInstance(factor, factor);
            AffineTransformOp op = new AffineTransformOp(scale, AffineTransformOp.TYPE_BICUBIC);
            image = op.filter(image, null);
            return image;
        } catch (Exception ex) {
            // something went wrong...
            LOGGER.warning(ex.getMessage());
            return null;
        }
    }
}
