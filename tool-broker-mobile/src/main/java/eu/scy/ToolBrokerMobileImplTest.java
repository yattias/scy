package eu.scy;

import eu.scy.toolbrokerapi.ToolBrokerAPI;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Created: 20.jan.2009 14:22:45
 *
 * @author Bjørge
 */
public class ToolBrokerMobileImplTest  extends MIDlet implements CommandListener {
	private ToolBrokerAPI broker;

	public ToolBrokerMobileImplTest() {
		broker = new ToolBrokerMobileImpl();
	}
	protected void startApp() throws MIDletStateChangeException {
		//IRepository repo = broker.getRepository();
		//System.out.println("repo = " + repo);

		//IExtensionManager extmanager = broker.getExtensionManager();
		//System.out.println("extmanager = " + extmanager);

		//IMetadataTypeManager metamanager = broker.getMetaDataTypeManager();
		//System.out.println("metamanager = " + metamanager);
	}

	protected void pauseApp() {
	}

	protected void destroyApp(boolean b) throws MIDletStateChangeException {
	}

	public void commandAction(Command command, Displayable displayable) {
	}
}
