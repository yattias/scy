package eu.scy.collaborationservice.adapter;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import eu.scy.collaborationservice.adapter.sqlspaces.ISQLSpaceAdapterListener;
import eu.scy.collaborationservice.adapter.sqlspaces.SQLSpaceAdapter;
import eu.scy.collaborationservice.adapter.sqlspaces.SQLSpaceAdapterEvent;
import eu.scy.collaborationservice.adapter.sqlspaces.SQLSpacesAdapterHelper;
import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;

/**
 * TODO replace this with a helper for the singleton
 * 
 * 
 * @author anthonyp
 *
 */
public class ScyCommunicationAdapter implements IScyCommunicationAdapter, ISQLSpaceAdapterListener {
    
    public static final Logger logger = Logger.getLogger(ScyCommunicationAdapter.class.getName());
    public static final long DEFAULT_EXPIRATION_TIME = 30 * 1000;
    private SQLSpaceAdapter tupleAdapter;
    private ArrayList<IScyCommunicationListener> scyCommunicationListeners = new ArrayList<IScyCommunicationListener>();
    
    public ScyCommunicationAdapter() {
        logger.debug("Empty Constructor Collaboration created");
    }
    
    public void actionUponDelete(IScyMessage scyMessage) {
        logger.debug("something was deleted in the tuple space");
        sendCallBack(scyMessage);
    }
    
    public void actionUponWrite(IScyMessage scyMessage) {
        logger.debug("something was written in the tuple space");
        sendCallBack(scyMessage);        
    }
    
    public void actionUponUpdate(IScyMessage scyMessage) {
        logger.debug("something was been updated in the tuple space");
        sendCallBack(scyMessage);        
    }
    
    private SQLSpaceAdapter getTupleAdapter() {
        if (tupleAdapter == null) {
            // TODO: SQLSpaceAdapter.COLLABORATION_SERVICE_SPACE shouldn't be
            // hardcoded here, but be passed from the openfire plugin
            tupleAdapter = SQLSpacesAdapterHelper.getInstance();
            tupleAdapter.initialize(this.getClass().getName(), SQLSpaceAdapter.COLLABORATION_SERVICE_SPACE);
            tupleAdapter.addSQLSpacesAdapterListener(this);
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
    
    public ArrayList<IScyCommunicationListener> getScyCommunicationListeners() {
        return this.scyCommunicationListeners;
    }
    
    public void sendCallBack(IScyMessage scyMessage) {
        for (IScyCommunicationListener cl : this.scyCommunicationListeners) {
            if (cl != null) {
                ScyCommunicationEvent scyCommunicationEvent = new ScyCommunicationEvent(this, scyMessage);
                cl.handleCommunicationEvent(scyCommunicationEvent);
            }
        }
    }

    public ArrayList<IScyMessage> doQuery(IScyMessage queryMessage) {
        if(ScyMessage.MESSAGE_TYPE_QUERY.equals(queryMessage.getName())) {
            if(ScyMessage.QUERY_TYPE_ALL.equals(queryMessage.getDescription())) {
                return getTupleAdapter().readAll(queryMessage);
            }
        }
        return null;
    }

    @Override
    public void handleSQLSpacesEvent(SQLSpaceAdapterEvent sqlSpaceEvent) {
        if( sqlSpaceEvent.getAction().equals(SQLSpaceAdapter.WRITE)) {
            this.actionUponWrite(sqlSpaceEvent.getScyMessage());
        } else if(sqlSpaceEvent.getAction().equals(SQLSpaceAdapter.DELETE)){
            this.actionUponDelete(sqlSpaceEvent.getScyMessage());
        }  else if(sqlSpaceEvent.getAction().equals(SQLSpaceAdapter.UPDATE)){
            this.actionUponUpdate(sqlSpaceEvent.getScyMessage());
        }
    }
    
}
