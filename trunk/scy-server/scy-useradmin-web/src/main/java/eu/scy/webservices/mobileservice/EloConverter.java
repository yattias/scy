package eu.scy.webservices.mobileservice;

import roolo.elo.api.IELO;
import roolo.elo.BasicELO;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 24.mar.2009
 * Time: 12:53:10
 * To change this template use File | Settings | File Templates.
 */
public class EloConverter {
	public static IELO convert(MobileELO elo) {
		return new BasicELO();
	}
	public static MobileELO convert(IELO elo) {
		return new MobileELO();
	}
}
