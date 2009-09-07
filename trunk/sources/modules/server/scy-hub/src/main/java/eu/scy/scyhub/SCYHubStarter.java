package eu.scy.scyhub;

import eu.scy.communications.datasync.properties.CommunicationProperties;
import org.jivesoftware.whack.ExternalComponentManager;
import org.xmpp.component.ComponentException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.sep.2009
 * Time: 07:00:11
 * To change this template use File | Settings | File Templates.
 */
public class SCYHubStarter {
    
    private static CommunicationProperties props = new CommunicationProperties();

    private SCYHubComponent scyHubComponent;

    public SCYHubStarter(SCYHubComponent scyHubComponent) {
        setScyHubComponent(scyHubComponent);
        final ExternalComponentManager manager = new ExternalComponentManager(props.datasyncExternalComponentHost, props.datasyncExternalComponentPort);
        manager.setSecretKey(props.datasyncMessageHub, props.datasyncExternalComponentSecretKey);
        manager.setMultipleAllowed("scyhub", true);
        System.out.println("Setting up scy hub");
        try {
            if(getScyHubComponent() == null) {
                System.out.println("SCY HUB COMPOMENT IS NULL!");
            }
            if(manager != null) {
            manager.addComponent(props.datasyncMessageHub, getScyHubComponent());
            } else {
                System.out.println("MANAGER IS NULLL!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");   
            }

        } catch (ComponentException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public SCYHubComponent getScyHubComponent() {
        return scyHubComponent;
    }

    public void setScyHubComponent(SCYHubComponent scyHubComponent) {
        this.scyHubComponent = scyHubComponent;
    }
}
