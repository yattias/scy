package eu.scy.collaborationservice.adapter;

import org.apache.log4j.Logger;

public class ScyCommunicationAdapterHelper {
    
    public static final Logger logger = Logger.getLogger(ScyCommunicationAdapterHelper.class.getName());
    private static ScyCommunicationAdapter scyCommunicationAdapter;
    
    /**
     * Gets an instance of adapter
     * 
     * @return
     */
    public static ScyCommunicationAdapter getInstance() {
        if (scyCommunicationAdapter == null) {
            logger.debug("create the scyAdapter");
            scyCommunicationAdapter = new ScyCommunicationAdapter();
        }
        return scyCommunicationAdapter;
    }
}
