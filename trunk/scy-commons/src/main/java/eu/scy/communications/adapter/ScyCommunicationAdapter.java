package eu.scy.communications.adapter;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import eu.scy.communications.adapter.sqlspaces.SQLSpaceAdapter;
import eu.scy.communications.message.ScyMessage;

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
    
    public ScyMessage getScyMessage(String description) {
        ScyMessage sm = new ScyMessage();
        sm.setId("54321");
        sm.setName("a nice name for the object");
        sm.setDescription(description);
        return sm;
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
    public String create(ScyMessage sm) {
        logger.debug("create");
        return getTupleAdapter().write("openfire", sm);
    }
    
    @Override
    public String createWithExpiration(ScyMessage sm, long expiration) {
        logger.debug("create");
        return getTupleAdapter().write("openfire", sm, expiration);
    }
    
    @Override
    public String delete(String id) {
        logger.debug("create");
        return getTupleAdapter().delete(id);
    }
    
    @Override
    public ScyMessage read(String id) {
        logger.debug("read");
        return getTupleAdapter().readById(id);
    }
    
    @Override
    public String update(ScyMessage sm, String id) {
        logger.debug("update with expiration");
        return getTupleAdapter().write(id, "openfire", sm);
    }
    
    @Override
    public String updateWithExpiration(ScyMessage sm, String id, long expiration) {
        logger.debug("update with expiration");
        return getTupleAdapter().write(id, "openfire", sm, expiration);
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
