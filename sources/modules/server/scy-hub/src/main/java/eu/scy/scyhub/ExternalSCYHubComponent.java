package eu.scy.scyhub;

import org.jivesoftware.whack.ExternalComponentManager;
import eu.scy.common.configuration.Configuration;

/**
 * This is an example of how to make a component run as an external component. This examples
 * requires that the server be running in the same computer where this application will run and
 * that it can be located under the name "localhost".
 *
 * @author Gaston Dombiak
 */
public class ExternalSCYHubComponent {
 
    public static void main(String[] args) {
        // Create a manager for the external components that will connect to the server "localhost" at the port 5225
        final ExternalComponentManager manager = new ExternalComponentManager(Configuration.getInstance().getDatasyncExternalComponentHost(), Configuration.getInstance().getDatasyncExternalComponentPort());
        // Set the secret key for this component. The server must be using the same secret key
        // otherwise the component won't be able to authenticate with the server. Check that the
        // server has the properfy "component.external.secretKey" defined and that it is using the
        // same value that we are setting here.
        manager.setSecretKey(Configuration.getInstance().getDatasyncMessageHub(), Configuration.getInstance().getDatasyncExternalComponentSecretKey());
        // Set the manager to tag components as being allowed to connect multiple times to te same JID.
         manager.setMultipleAllowed("scyhub", true);
        try {
            // Register that this component will be serving the given subdomain of the server
       	manager.addComponent(Configuration.getInstance().getDatasyncMessageHub(), new SCYHubComponent());
            // Quick trick to ensure that this application will be running for ever. To stop the
            // application you will need to kill the process
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
//            manager.getLog().error(e);
        }
    }
}
