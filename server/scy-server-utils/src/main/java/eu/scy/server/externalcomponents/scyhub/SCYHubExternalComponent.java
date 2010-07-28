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
        	manager = new ExternalComponentManager(Configuration.getInstance().getOpenFireHost(), Configuration.getInstance().getOpenFireExternalComponentPort());
        	manager.setSecretKey(Configuration.getInstance().getSCYHubName(), Configuration.getInstance().getOpenFireExternalComponentSecretKey());
        	manager.setMultipleAllowed(Configuration.getInstance().getSCYHubName(), true);
        	//scyhub = new SCYHubComponent();
            manager.addComponent(Configuration.getInstance().getSCYHubName(), getScyhub());
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
			manager.removeComponent(Configuration.getInstance().getSCYHubName());
			scyhub.shutdown(); // XXX: Adam, I don't think that shutdown is the correct method here. better check that, sw
		} catch (ComponentException e) {
			throw new ExternalComponentFailedException("Could not stop SCY-Hub!", e);
		}
	}

    public SCYHubComponent getScyhub() {
        return scyhub;
    }

    public void setScyhub(SCYHubComponent scyhub) {
        this.scyhub = scyhub;
    }
}
