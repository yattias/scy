package eu.scy.mobile.toolbroker.tests;

import eu.scy.mobile.toolbroker.ToolBrokerMobileAPIImpl;
import eu.scy.mobile.toolbroker.model.ELO;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Created: 13.feb.2009 12:37:39
 *
 * @author Bjørge Næss
 */
public class ToolBrokerMobileAPIImplTest extends MIDlet {
	private ToolBrokerMobileAPIImpl toolBroker;

	public void testGetELO() {
		ELO elo = toolBroker.getELO(1);
		System.out.println("elo = " + elo);
	}
/*
	public void testGetUser() {
		User user = userServiceClient.getUser(1);
		assertNotNull(elo);
	}*/
	protected void startApp() throws MIDletStateChangeException {
		toolBroker = new ToolBrokerMobileAPIImpl();
		testGetELO();
	}

	protected void pauseApp() {
	}

	protected void destroyApp(boolean b) throws MIDletStateChangeException {
	}
}
