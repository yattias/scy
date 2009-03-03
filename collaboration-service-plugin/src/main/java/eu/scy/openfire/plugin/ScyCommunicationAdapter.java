//package eu.scy.collaborationservice.tuplespaceconnector;
package eu.scy.openfire.plugin;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import eu.scy.core.model.impl.ScyBaseObject;



public class ScyCommunicationAdapter implements IScyCommunicationAdapter {
    
    public static final Logger logger = Logger.getLogger(ScyCommunicationAdapter.class.getName());
    private static final long DEFAULT_EXPIRATION_TIME = 30*1000;
    private SQLSpaceAdapter tupleAdapter;
    private static ScyCommunicationAdapter communicator;
    private ArrayList<IScyCommunicationListener> scyCommunicationListeners = new ArrayList<IScyCommunicationListener>();


    public ScyCommunicationAdapter() {
        logger.debug("Empty Constructor Collaboration created");
    }

    public static ScyCommunicationAdapter getInstance() {
        if (communicator == null) {
            logger.debug("Created Tuple Spaces");
            communicator = new ScyCommunicationAdapter();
        }
        return communicator;
    }


    public void actionUponDelete(String username) {
        // TODO Auto-generated method stub
    }
    
    public void actionUponWrite(String username) {
        // TODO Auto-generated method stub
    }
    
    
    public String userPing(String jabberUser) {
        logger.debug("Attempting to write PING to CS");
        String id = write(jabberUser, "ping");
        logger.debug("ID returned: " + id);
        return id;
    }
    
    
    public String userUnavailable(String jabberUser) {
        logger.debug("Attempting to write UNAVAILABLE to CS");
        String id = write(jabberUser, "unavailable");
        logger.debug("ID returned: " + id);
        return id;
    }
    
    
    public String write(String username, String status) {
        logger.debug("switchboard says it's about to write: " + username + "/" + status);
        ScyBaseObject sbo = new ScyBaseObject();
        sbo.setId("54321");
        sbo.setName("a nice name for the object");
        sbo.setDescription(status);
        String id = getTupleAdapter().write(null, "openfire", sbo, DEFAULT_EXPIRATION_TIME); // if documentSqlSpaceId != null this will update the tuple
        if (id != null) {
            logger.debug("Write to CS ok");
        } else {
            logger.error("Trouble while writing to CS");
        }
        return id;
    }
    
    
    private SQLSpaceAdapter getTupleAdapter() {
        if (tupleAdapter == null) {
            //TODO: SQLSpaceAdapter.COLLABORATION_SERVICE_SPACE shouldn't be hardcoded here
            tupleAdapter = SQLSpaceAdapter.createAdapter(this.getClass().getName(), SQLSpaceAdapter.COLLABORATION_SERVICE_SPACE, this);
            logger.debug("Created Tuple Spaces");
        }
        return tupleAdapter;
    }
    
    
    public void sendCallBack(String something) { 
        for (IScyCommunicationListener cl : scyCommunicationListeners) {
            if (cl != null){
                ScyCommunicationEvent scyCommunicationEvent = new ScyCommunicationEvent(this, something, something);
                cl.handleCommunicationEvent(scyCommunicationEvent);
            }
        }
    }

}
