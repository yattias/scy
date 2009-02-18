package eu.scy.collaborationservice.tuplespaceconnector;

import org.apache.log4j.Logger;

import eu.scy.collaborationservice.CollaborationService;
import eu.scy.collaborationservice.CollaborationServiceClientInterface;
import eu.scy.core.model.impl.ScyBaseObject;


public class TupleSpaceConnectorSwitchboard implements CollaborationServiceClientInterface {
    
    private static final Logger logger = Logger.getLogger(TupleSpaceConnectorSwitchboard.class.getName());
    private static final long DEFAULT_EXPIRATION_TIME = 30*1000;
    private CollaborationService cs;
    //private static TupleSpaceConnectorSwitchboard tupleSpaceConnectorSwitchboard = new TupleSpaceConnectorSwitchboard();


    public TupleSpaceConnectorSwitchboard() {
        logger.debug("TupleSpaceConnectorSwitchboard created");
    }

//    public static TupleSpaceConnectorSwitchboard getInstance() {
//        logger.debug("TupleSpaceConnectorSwitchboard: getInstance()");
//        return tupleSpaceConnectorSwitchboard;
//    }


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
        String id = getCS().write(null, "openfire", sbo, DEFAULT_EXPIRATION_TIME); // if documentSqlSpaceId != null this will update the tuple
        if (id != null) {
            logger.debug("Write to CS ok");
        } else {
            logger.error("Trouble while writing to CS");
        }
        return id;
    }
    
    private CollaborationService getCS() {
        if (cs == null) {
            logger.debug("Created CollaborationService");
            cs = CollaborationService.createCollaborationService(this.getClass().getName(), CollaborationService.AWARENESS_SERVICE_SPACE, this);
        }
        return cs;
    }

}
