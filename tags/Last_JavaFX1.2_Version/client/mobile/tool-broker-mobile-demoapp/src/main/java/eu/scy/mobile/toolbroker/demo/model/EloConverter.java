package eu.scy.mobile.toolbroker.demo.model;

import eu.scy.mobile.toolbroker.demo.client.stubs.MobileELO;
import eu.scy.mobile.toolbroker.demo.util.Base64;

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
		ie.setImage(Base64.decode(me.getB64Image()));
		return ie;
	}
	public static MobileELO convert(ImageELO ie) {
		MobileELO me = new MobileELO();
		me.setDescription(ie.getComment());
		me.setTitle(ie.getTitle());
		me.setB64Image(new String(Base64.encode(ie.getImage())));
		return me;
	}


}

