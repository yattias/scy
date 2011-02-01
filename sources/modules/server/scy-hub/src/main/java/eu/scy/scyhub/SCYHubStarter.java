package eu.scy.scyhub;

import eu.scy.common.configuration.Configuration;
import org.apache.log4j.Logger;
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

    private static final Logger logger = Logger.getLogger(SCYHubStarter.class.getName());

    private SCYHubComponent scyHubComponent;

    public SCYHubStarter(SCYHubComponent scyHubComponent) {
        logger.info("CREATING SCY HUB OUTSIDE OF SPRING CONTEXT!");
        logger.info("CREATING SCY HUB OUTSIDE OF SPRING CONTEXT!");
        logger.info("CREATING SCY HUB OUTSIDE OF SPRING CONTEXT!");
        logger.info("CREATING SCY HUB OUTSIDE OF SPRING CONTEXT!");
        logger.info("CREATING SCY HUB OUTSIDE OF SPRING CONTEXT!");
        setScyHubComponent(scyHubComponent);
        final ExternalComponentManager manager = new ExternalComponentManager(Configuration.getInstance().getOpenFireHost(), Configuration.getInstance().getOpenFireExternalComponentPort());
        initialize(manager);
    }

    public SCYHubStarter(SCYHubComponent scyHubComponent, String dataSyncExternalComponentHost, Integer datasyncPort) {
        logger.info("CREATING SCY HUB WITH: " + dataSyncExternalComponentHost + " " + datasyncPort);
        setScyHubComponent(scyHubComponent);
        final ExternalComponentManager manager = new ExternalComponentManager(dataSyncExternalComponentHost, datasyncPort);
        initialize(manager);
    }

    private void initialize(ExternalComponentManager manager) {
        manager.setSecretKey(Configuration.getInstance().getSCYHubName(), Configuration.getInstance().getOpenFireExternalComponentSecretKey());
        manager.setMultipleAllowed("scyhub", true);
        // System.out.println("SETTING UP SCY HUB");
        try {
            if(getScyHubComponent() == null) {
                // System.out.println("SCY HUB COMPOMENT IS NULL!");
            }
            if(manager != null) {
            manager.addComponent(Configuration.getInstance().getSCYHubName(), getScyHubComponent());
            } else {
                // System.out.println("MANAGER IS NULLL!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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
