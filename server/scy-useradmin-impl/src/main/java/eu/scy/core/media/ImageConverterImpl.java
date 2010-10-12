package eu.scy.core.media;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.mar.2010
 * Time: 10:10:15
 * To change this template use File | Settings | File Templates.
 */
public class ImageConverterImpl implements ImageConverter{

    protected static transient Logger log = Logger.getLogger(ImageConverterImpl.class);

    public static final String ICON_POSTFIX = ".jpg";
    public static final int DEFAULT_ICON_SIZE = 70;
//	public MediaTracker mediaTracker;        mave


    public ImageConverterImpl() {
        ImageIO.setUseCache(false);

    }

    /**
     * @throws java.io.IOException
     */
    @Override
    public File handleImageConversion(File imageFile) throws IOException,
            FileNotFoundException {
        BufferedImage image = getImage(imageFile);

//		if(imageFileInfo.getCreateIcon()) {
        return createIcon(imageFile, image);
//		}
        //Add more conversions as needed
    }

    /**
     * Returns a buffered image, or null if not able to create image from file.
     *
     * @return A buffered image created from file
     * @throws IOException
     */
    public static BufferedImage getImage(File imageFile) throws IOException,
            FileNotFoundException {

        BufferedImage image;


        ImageInputStream iis = ImageIO.createImageInputStream(imageFile);

        Iterator<ImageReader> imReaders = ImageIO.getImageReaders(iis);

        //Use the first available reader:
        if (imReaders.hasNext()) {
            ImageReader imageReader = imReaders.next();
            //If image should be used multiple times from the same reader, perhaps forwardSeekOnly should be switched off
            boolean seekForwardOnly = true;
            imageReader.setInput(iis, seekForwardOnly);

            image = imageReader.read(0);
        } else {
            image = null;
            log.warn("No image readers found, corrupted or file of zero length; imagefile: " + imageFile);
        }

        return image;
    }


    private File createIcon(File imageFile, BufferedImage image) throws IOException {

        BufferedImage icon = scaleImage(image, DEFAULT_ICON_SIZE, DEFAULT_ICON_SIZE);

        //Always write thumbnails as jpeg
        Iterator<ImageWriter> i = ImageIO.getImageWritersByFormatName("jpeg");
        ImageWriter writer = i.next();

        File thumbFile = File.createTempFile("scy", "tmp");
        thumbFile.deleteOnExit();
        //Delete if there already exists an icon
        //thumbFile.delete();

        ImageOutputStream ios = ImageIO.createImageOutputStream(thumbFile);

        try {
            //If icon is null, we will get an exception in the writer
            if (icon != null) {
                writer.setOutput(ios);
                writer.write(icon);
            }

            if (log.isDebugEnabled()) {
                log.debug("written file " + thumbFile);
            }


            return thumbFile;
        } catch (IOException e) {
            log.error("Problems creating icon: " + thumbFile, e);
        } finally {
            ios.close();
        }

        return null;


    }

    private static BufferedImage scaleImage(BufferedImage image, double scaleWidth, double scaleHeight) {
        if (image != null) {
            double imageWidth = image.getWidth();
            double imageHeight = image.getHeight();
            double scaleFactor = imageWidth > imageHeight ? scaleWidth / imageWidth : scaleHeight / imageHeight;

            //getDefaultConfiguration returns null if headless
            image = getScaledInstance(image, scaleFactor, getDefaultConfiguration());
        }

        return image;
    }


    //These fabulous utility methods are stolen from
    // http://forum.java.sun.com/thread.jspa?messageID=3565263
    //TODO: Move picture methods to auth class

    public static GraphicsConfiguration getDefaultConfiguration() {
        GraphicsConfiguration gc = null;
        //Check for headlessnes, and return null if decapitated
        if (!GraphicsEnvironment.isHeadless()) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            gc = gd.getDefaultConfiguration();
        }

        return gc;
    }

    public static BufferedImage toBufferedImage(Image image, int type) {
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        BufferedImage result = new BufferedImage(w, h, type);
        Graphics2D g = result.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return result;
    }

    public static BufferedImage copy(BufferedImage source, BufferedImage target, Object interpolationHint) {
        Graphics2D g = target.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interpolationHint);
        double scalex = (double) target.getWidth() / source.getWidth();
        double scaley = (double) target.getHeight() / source.getHeight();
        AffineTransform at = AffineTransform.getScaleInstance(scalex, scaley);
        g.drawRenderedImage(source, at);
        g.dispose();
        return target;
    }

    private static BufferedImage getScaledInstance2D(BufferedImage source, double factor, Object interpolationHint,
                                                     GraphicsConfiguration gc) {

        int w = (int) (source.getWidth() * factor);
        int h = (int) (source.getHeight() * factor);
        int transparency = source.getColorModel().getTransparency();

        return copy(source, gc.createCompatibleImage(w, h, transparency), interpolationHint);
    }

    public static Image getScaledInstanceAWT(BufferedImage source, double factor, int hint) {
        int w = (int) (source.getWidth() * factor);
        int h = (int) (source.getHeight() * factor);
        return source.getScaledInstance(w, h, hint);
    }

    //best of breed

    public static BufferedImage getScaledInstance(BufferedImage source, double factor, GraphicsConfiguration gc) {
        //GraphicsConfiguration is null if headless
        BufferedImage scaled;

        if (factor == 1.0d) {
            scaled = source;
        } else if (factor > 1.0d && gc != null) {
            scaled = getScaledInstance2D(source, factor, RenderingHints.VALUE_INTERPOLATION_BICUBIC, gc);
        } else {
            //Use this method for all calls if gc is nonexistant (i.e. headless)
            scaled = toBufferedImage(
                    getScaledInstanceAWT(source, factor, Image.SCALE_AREA_AVERAGING),
                    BufferedImage.TYPE_INT_RGB
            );
        }

        return scaled;
    }


}
