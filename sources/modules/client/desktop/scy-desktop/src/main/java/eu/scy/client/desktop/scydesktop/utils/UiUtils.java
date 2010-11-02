package eu.scy.client.desktop.scydesktop.utils;

import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

/**
 *
 * @author lars
 */
public class UiUtils {
   private final static Logger logger = Logger.getLogger(UiUtils.class);

    public static BufferedImage createThumbnail(Container container, Dimension originalSize, Dimension targetSize) {
        try {
            BufferedImage bi = new BufferedImage(originalSize.width, originalSize.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bi.createGraphics();
            container.paint(g2d);
            g2d.dispose();
            return resizeBufferedImage(bi,targetSize);
        } catch (Exception ex) {
            // something went wrong...
            logger.warn(ex.getMessage());
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
            AffineTransformOp op = new AffineTransformOp(scale, AffineTransformOp.TYPE_BILINEAR);
            image = op.filter(image, null);
            return image;
        } catch (Exception ex) {
            // something went wrong...
            logger.warn(ex.getMessage());
            return null;
        }
    }
}
