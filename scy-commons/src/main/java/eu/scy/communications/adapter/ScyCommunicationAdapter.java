package eu.scy.communications.adapter;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import eu.scy.communications.adapter.sqlspaces.SQLSpaceAdapter;
import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;

public class ScyCommunicationAdapter implements IScyCommunicationAdapter {
    
    public static final Logger logger = Logger.getLogger(ScyCommunicationAdapter.class.getName());
    public static final long DEFAULT_EXPIRATION_TIME = 30 * 1000;
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
    
    public void actionUponDelete(IScyMessage deletedScyMessage) {
        logger.debug("something was deleted in the tuple space");
        sendCallBack(deletedScyMessage);
    }
    
    public void actionUponWrite(IScyMessage writtenScyMessage) {
        logger.debug("something was written in the tuple space");
        sendCallBack(writtenScyMessage);        
    }
    
    public IScyMessage getScyMessage(String description) {
        ScyMessage scyMessage = new ScyMessage();
        scyMessage.setId("54321");
        scyMessage.setName("a nice name for the object");
        scyMessage.setDescription(description);
        return scyMessage;
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
    public String create(IScyMessage scyMessage) {
        logger.debug("create");
        return getTupleAdapter().write(scyMessage);
    }
    
    @Override
    public String delete(String id) {
        logger.debug("delete");
        return getTupleAdapter().delete(id);
    }
    
    @Override
    public IScyMessage read(String id) {
        logger.debug("read");
        return getTupleAdapter().readById(id);
    }
    
    @Override
    public String update(IScyMessage scyMessage, String id) {
        logger.debug("update");
        return getTupleAdapter().write(id, scyMessage);
    }
    
    @Override
    public void addScyCommunicationListener(IScyCommunicationListener listener) {
        this.scyCommunicationListeners.add(listener);
    }
    
    public void sendCallBack(IScyMessage scyMessage) {
        for (IScyCommunicationListener cl : scyCommunicationListeners) {
            if (cl != null) {
                ScyCommunicationEvent scyCommunicationEvent = new ScyCommunicationEvent(this, scyMessage);
                cl.handleCommunicationEvent(scyCommunicationEvent);
            }
        }
    }

    public ArrayList<IScyMessage> doQuery(IScyMessage queryMessage) {
        logger.debug("doing query, mofo " + queryMessage);
        if(ScyMessage.MESSAGE_TYPE_QUERY.equals(queryMessage.getName())) {
            if(ScyMessage.QUERY_TYPE_ALL.equals(queryMessage.getName())) {
                return getTupleAdapter().readAll(queryMessage);                
            }
        }
        return null;
    }
    
}
