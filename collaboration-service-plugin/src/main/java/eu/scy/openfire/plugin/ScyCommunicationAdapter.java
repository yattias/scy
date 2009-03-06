// package eu.scy.collaborationservice.tuplespaceconnector;
package eu.scy.openfire.plugin;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import eu.scy.core.model.impl.ScyBaseObject;

public class ScyCommunicationAdapter implements IScyCommunicationAdapter {
    
    public static final Logger logger = Logger.getLogger(ScyCommunicationAdapter.class.getName());
    private static final long DEFAULT_EXPIRATION_TIME = 30 * 1000;
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
        logger.debug("something was deleted in the tuple space");
    }
    
    public void actionUponWrite(String username) {
        logger.debug("something was written in the tuple space");
    }
    
    public ScyBaseObject getScyBaseObject(String description) {
        ScyBaseObject sbo = new ScyBaseObject();
        sbo.setId("54321");
        sbo.setName("a nice name for the object");
        sbo.setDescription(description);
        return sbo;
    }
    
    private SQLSpaceAdapter getTupleAdapter() {
        if (tupleAdapter == null) {
            // TODO: SQLSpaceAdapter.COLLABORATION_SERVICE_SPACE shouldn't be
            // hardcoded here, but be passed from the openfire plugin
            tupleAdapter = SQLSpaceAdapter.createAdapter(this.getClass().getName(), SQLSpaceAdapter.COLLABORATION_SERVICE_SPACE, this);
            logger.debug("Created Tuple Spaces");
        }
        return tupleAdapter;
    }
    
    @Override
    public String create(ScyBaseObject sbo) {
        logger.debug("create");
        return getTupleAdapter().write("openfire", sbo);
    }
    
    @Override
    public String createWithExpiration(ScyBaseObject sbo, long expiration) {
        logger.debug("create");
        return getTupleAdapter().write("openfire", sbo, expiration);
    }
    
    @Override
    public String delete(String id) {
        logger.debug("create");
        return getTupleAdapter().delete(id);
    }
    
    @Override
    public ScyBaseObject read(String id) {
        logger.debug("read");
        return getTupleAdapter().readById(id);
    }
    
    @Override
    public String update(ScyBaseObject sbo, String id) {
        logger.debug("update with expiration");
        return getTupleAdapter().write(id, "openfire", sbo);
    }
    
    @Override
    public String updateWithExpiration(ScyBaseObject sbo, String id, long expiration) {
        logger.debug("update with expiration");
        return getTupleAdapter().write(id, "openfire", sbo, expiration);
    }
    
    @Override
    public void addScyCommunicationListener(IScyCommunicationListener listener) {
        this.scyCommunicationListeners.add(listener);
    }
    
    public void sendCallBack(String something) {
        for (IScyCommunicationListener cl : scyCommunicationListeners) {
            if (cl != null) {
                ScyCommunicationEvent scyCommunicationEvent = new ScyCommunicationEvent(this, something, something);
                cl.handleCommunicationEvent(scyCommunicationEvent);
            }
        }
    }
    
}
