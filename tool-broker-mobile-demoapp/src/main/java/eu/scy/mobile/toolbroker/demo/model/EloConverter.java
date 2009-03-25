package eu.scy.mobile.toolbroker.demo.model;

import com.sun.midp.io.Base64;
import eu.scy.mobile.toolbroker.demo.client.MobileELO;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 25.mar.2009
 * Time: 14:14:56
 */
public class EloConverter {
	public static ImageELO convert(MobileELO me) {

		ImageELO ie = new ImageELO();
		ie.setTitle(me.getTitle());
		ie.setComment(me.getDescription());
		try {
			ie.setImage(Base64.decode(me.getB64Image()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ie;
	}
	public static MobileELO convert(ImageELO ie) {
		MobileELO me = new MobileELO();
		me.setDescription(ie.getComment());
		me.setTitle(ie.getTitle());
		me.setB64Image(Base64.encode(ie.getImage(), 0, ie.getImage().length));
		return me;
	}


}

