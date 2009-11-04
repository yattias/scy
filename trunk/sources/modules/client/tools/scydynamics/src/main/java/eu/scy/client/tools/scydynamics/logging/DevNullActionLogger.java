package eu.scy.client.tools.scydynamics.logging;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;

public class DevNullActionLogger implements IActionLogger {

	@Override
	public void log(String username, String source, IAction action) {
		// doing absolutely nothing here		
	}
	
}
