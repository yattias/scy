package eu.scy.collaborationservice.tuplespaceconnector;

import java.io.File;

import org.apache.log4j.Logger;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;


public class TuplespaceConnector implements Plugin {
    
    private static final Logger logger = Logger.getLogger(TuplespaceConnector.class.getName());

    public void destroyPlugin() {
        // TODO Auto-generated method stub
        
    }

    public void initializePlugin(PluginManager arg0, File arg1) {
        // TODO Auto-generated method stub
        logger.debug("You are connected to cheesy connector. Connections make you happy.");
    }

}
