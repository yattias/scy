/**
 * 
 */
package eu.scy.server.externalcomponents;

import org.jivesoftware.whack.ExternalComponentManager;
import org.xmpp.component.ComponentException;

import eu.scy.common.configuration.Configuration;
import eu.scy.scyhub.SCYHubComponent;

/**
 * @author giemza
 *
 */
public class SCYHubExternalComponent implements IExternalComponent {
	
	private SCYHubComponent scyhub;
	private ExternalComponentManager manager;

	/* (non-Javadoc)
	 * @see eu.scy.server.externalcomponents.IExternalComponent#getPriority()
	 */
	@Override
	public int getPriority() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see eu.scy.server.externalcomponents.IExternalComponent#startComponent()
	 */
	@Override
	public void startComponent() throws ExternalComponentFailedException {
        try {
        	manager = new ExternalComponentManager(Configuration.getInstance().getDatasyncExternalComponentHost(), Configuration.getInstance().getDatasyncExternalComponentPort());
        	manager.setSecretKey(Configuration.getInstance().getDatasyncMessageHub(), Configuration.getInstance().getDatasyncExternalComponentSecretKey());
        	manager.setMultipleAllowed("scyhub", true);
        	System.out.println("Setting up scy hub");
        	
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
		try {
			manager.removeComponent(Configuration.getInstance().getDatasyncMessageHub());
		} catch (ComponentException e) {
			throw new ExternalComponentFailedException("Could not start SCY-Hub!", e);
		}
	}

}
