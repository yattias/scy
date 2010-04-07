package eu.scy.webservices.mobileservice;

import roolo.elo.BasicELO;
import roolo.elo.api.IELO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
  * Date: 24.mar.2009
 * Time: 12:53:10
 * To change this template use File | Settings | File Templates.
 */
public class EloConverter {
	public static IELO convert(MobileELO elo) {
		String fname = "c:\\" + elo.getTitle() + ".jpg";
		File f = new File(fname);
		try {
			InputStream in = new ByteArrayInputStream(elo.getImage());
			BufferedImage image = ImageIO.read(in);
			FileOutputStream fos = new FileOutputStream(f);
			ImageIO.write(image, "jpeg", fos);
			in.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BasicELO basicELO = new BasicELO();

		//ImageContent ic = new ImageContent(elo.getTitle(), elo.getDescription(), elo.getImage());

		//basicELO.setContent(ic);
		return basicELO;
	}
	public static MobileELO convert(IELO elo) {
		return new MobileELO();
	}
}
