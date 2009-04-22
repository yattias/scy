package eu.scy.mobile.toolbroker.demo.util;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Graphics;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 20.mar.2009
 * Time: 20:39:56
 * To change this template use File | Settings | File Templates.
 */
public final class ImageTools {
	public static Image createThumbnail(Image image, int width, int height) {
		int sourceWidth = image.getWidth();
		int sourceHeight = image.getHeight();

		int thumbWidth = width;
		int thumbHeight = height;

		if (thumbHeight == -1)
			thumbHeight = thumbWidth * sourceHeight / sourceWidth;

		Image thumb = Image.createImage(thumbWidth, thumbHeight);
		Graphics g = thumb.getGraphics();

		for (int y = 0; y < thumbHeight; y++) {
			for (int x = 0; x < thumbWidth; x++) {
				g.setClip(x, y, 1, 1);
				int dx = x * sourceWidth / thumbWidth;
				int dy = y * sourceHeight / thumbHeight;
				g.drawImage(image, x - dx, y - dy, Graphics.LEFT | Graphics.TOP);
			}
		}

		return Image.createImage(thumb);
	}
}
