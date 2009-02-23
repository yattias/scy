//package eu.scy.collaborationservice.tuplespaceconnector;
package eu.scy.collaborationservice;

import org.apache.log4j.Logger;

import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.tuple.TupleAdapter;


public class CollaborationService implements ICollaborationService {
    
    public static final Logger logger = Logger.getLogger(CollaborationService.class.getName());
    private static final long DEFAULT_EXPIRATION_TIME = 30*1000;
    private TupleAdapter tupleAdapter;
    private static CollaborationService collaborationService;


    public CollaborationService() {
        logger.debug("Empty Constructor Collaboration created");
    }

    public static CollaborationService getInstance() {
        if (collaborationService == null) {
            logger.debug("Created Tuple Spaces");
            collaborationService = new CollaborationService();
        }
        return collaborationService;
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
    
    
    private TupleAdapter getTupleAdapter() {
        if (tupleAdapter == null) {
            logger.debug("Created Tuple Spaces");
            tupleAdapter = TupleAdapter.createTupleAdapter(this.getClass().getName(), TupleAdapter.AWARENESS_SERVICE_SPACE, this);
        }
        return tupleAdapter;
    }

}
