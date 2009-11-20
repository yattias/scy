/**
 * 
 */
package eu.scy.server.externalcomponents.scyhub;

import java.util.logging.Logger;

import org.jivesoftware.whack.ExternalComponentManager;
import org.xmpp.component.ComponentException;

import eu.scy.common.configuration.Configuration;
import eu.scy.scyhub.SCYHubComponent;
import eu.scy.server.externalcomponents.ExternalComponentFailedException;
import eu.scy.server.externalcomponents.IExternalComponent;

/**
 * @author giemza
 *
 */
public class SCYHubExternalComponent implements IExternalComponent {
	
	private SCYHubComponent scyhub;
	private ExternalComponentManager manager;
	private Logger log = Logger.getLogger("SCYHubExternalComponent.class");

	/* (non-Javadoc)
	 * @see eu.scy.server.externalcomponents.IExternalComponent#startComponent()
	 */
	@Override
	public void startComponent() throws ExternalComponentFailedException {
	log.info("Initializing SCYHUB");
        try {
        	manager = new ExternalComponentManager(Configuration.getInstance().getDatasyncExternalComponentHost(), Configuration.getInstance().getDatasyncExternalComponentPort());
        	manager.setSecretKey(Configuration.getInstance().getDatasyncMessageHub(), Configuration.getInstance().getDatasyncExternalComponentSecretKey());
        	manager.setMultipleAllowed("scyhub", true);
        	scyhub = new SCYHubComponent();
            manager.addComponent(Configuration.getInstance().getDatasyncMessageHub(), scyhub);
        } catch (ComponentException e) {
            throw new ExternalComponentFailedException("Could not start SCY-Hub!", e);
        }
	}

	/* (non-Javadoc)
	 * @see eu.scy.server.externalcomponents.IExternalComponent#stopComponent()
	 */
	@Override
	public void stopComponent() throws ExternalComponentFailedException {
	        log.info("-----> STOPPING: SCYHUB");
		try {
			manager.removeComponent(Configuration.getInstance().getDatasyncMessageHub());
		} catch (ComponentException e) {
			throw new ExternalComponentFailedException("Could not start SCY-Hub!", e);
		}
	}

}
