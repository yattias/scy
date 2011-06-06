package eu.scy.mobile.toolbroker.tests;

import eu.scy.mobile.toolbroker.model.IELO;
import eu.scy.mobile.toolbroker.IELOService;
import eu.scy.mobile.toolbroker.client.EloServiceClient;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Created: 13.feb.2009 12:37:39
 *
 * @author Bjørge Næss
 */
public class ToolBrokerMobileAPIImplTest extends MIDlet {
	private IELOService eloClient;
    private final String ELO_SERVICE_URL = "http://129.177.24.191:9998";

	public void testGetELO() {
		IELO elo = eloClient.getELO(1);
		// System.out.println("elo = " + elo);
	}
/*
	public void testGetUser() {
		User user = userServiceClient.getUser(1);
		assertNotNull(elo);
	}*/
	protected void startApp() throws MIDletStateChangeException {
		eloClient = new EloServiceClient(ELO_SERVICE_URL);
		testGetELO();
	}

	protected void pauseApp() {
	}

	protected void destroyApp(boolean b) throws MIDletStateChangeException {
	}
}
