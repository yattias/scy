package eu.scy.datasync.adapter;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import eu.scy.datasync.adapter.sqlspaces.ISQLSpaceAdapterListener;
import eu.scy.datasync.adapter.sqlspaces.SQLSpaceAdapter;
import eu.scy.datasync.adapter.sqlspaces.SQLSpaceAdapterEvent;
import eu.scy.datasync.adapter.sqlspaces.SQLSpacesAdapterHelper;
import eu.scy.datasync.api.ISyncMessage;
import eu.scy.datasync.impl.SyncMessage;

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
    private ArrayList<IScyCommunicationListener> communicationListeners = new ArrayList<IScyCommunicationListener>();
    
    public ScyCommunicationAdapter() {
        logger.debug("Empty Constructor Collaboration created");
    }
    
    public void actionUponDelete(ISyncMessage syncMessage) {
        logger.debug("something was deleted in the tuple space");
        sendCallBack(syncMessage);
    }
    
    public void actionUponWrite(ISyncMessage syncMessage) {
        logger.debug("something was written in the tuple space");
        sendCallBack(syncMessage);        
    }
    
    public void actionUponUpdate(ISyncMessage syncMessage) {
        logger.debug("something was been updated in the tuple space");
        sendCallBack(syncMessage);        
    }
    
    private SQLSpaceAdapter getTupleAdapter() {
        if (tupleAdapter == null) {
            // TODO: SQLSpaceAdapter.COLLABORATION_SERVICE_SPACE shouldn't be
            // hardcoded here, but be passed from the openfire plugin
            tupleAdapter = SQLSpacesAdapterHelper.getInstance();
            tupleAdapter.initialize(this.getClass().getName(), SQLSpaceAdapter.DATA_SYNCHRONIZATION_SPACE);
            tupleAdapter.addSQLSpacesAdapterListener(this);
            logger.debug("Created Tuple Spaces");
        }
        return tupleAdapter;
    }
    
    @Override
    public String create(ISyncMessage syncMessage) {
        logger.debug("create");
        return getTupleAdapter().write(syncMessage);
    }
    
    @Override
    public String delete(String id) {
        logger.debug("delete");
        return getTupleAdapter().delete(id);
    }
    
    @Override
    public ISyncMessage read(String id) {
        logger.debug("read");
        return getTupleAdapter().readById(id);
    }
    
    @Override
    public String update(ISyncMessage syncMessage) {
        logger.debug("update");
        return getTupleAdapter().write(syncMessage.getPersistenceId(), syncMessage);
    }
    
    @Override
    public void addScyCommunicationListener(IScyCommunicationListener listener) {
        this.communicationListeners.add(listener);
    }
    
    public ArrayList<IScyCommunicationListener> getScyCommunicationListeners() {
        return this.communicationListeners;
    }
    
    public void sendCallBack(ISyncMessage syncMessage) {
        for (IScyCommunicationListener cl : this.communicationListeners) {
            if (cl != null) {
                ScyCommunicationEvent scyCommunicationEvent = new ScyCommunicationEvent(this, syncMessage);
                cl.handleCommunicationEvent(scyCommunicationEvent);
            }
        }
    }

    public ArrayList<ISyncMessage> doQuery(ISyncMessage queryMessage) {
        if(SyncMessage.MESSAGE_TYPE_QUERY.equals(queryMessage.getEvent())) {
            return getTupleAdapter().readAll(queryMessage);
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
