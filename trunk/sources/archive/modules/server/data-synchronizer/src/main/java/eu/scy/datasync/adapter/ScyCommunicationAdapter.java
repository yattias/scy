package eu.scy.datasync.adapter;

import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import eu.scy.common.configuration.Configuration;
import eu.scy.communications.datasync.properties.CommunicationProperties;
import eu.scy.communications.message.ISyncMessage;
import eu.scy.datasync.adapter.sqlspaces.ISQLSpaceAdapterListener;
import eu.scy.datasync.adapter.sqlspaces.SQLSpaceAdapter;
import eu.scy.datasync.adapter.sqlspaces.SQLSpaceAdapterEvent;
import eu.scy.datasync.adapter.sqlspaces.SQLSpacesAdapterHelper;

/**
 * TODO replace this with a helper for the singleton
 *
 * @author anthonyp
 */
public class ScyCommunicationAdapter implements IScyCommunicationAdapter, ISQLSpaceAdapterListener {

    public static final Logger logger = Logger.getLogger(ScyCommunicationAdapter.class.getName());
    public static final long DEFAULT_EXPIRATION_TIME = 30 * 1000;
    private SQLSpaceAdapter tupleAdapter;
    private ArrayList<IScyCommunicationListener> communicationListeners = new ArrayList<IScyCommunicationListener>();

    public ScyCommunicationAdapter() {
    }

    public ScyCommunicationAdapter(SQLSpaceAdapter adapter) {
        logger.info("----------------------------------------------");
        logger.info("-- CREATING SPRING BASED SCY_COMMUNICATION_ADAPTER: " );
        logger.info("----------------------------------------------");
        this.tupleAdapter = adapter;
        try {
            tupleAdapter.initialize(this.getClass().getName(), Configuration.getInstance().getSqlSpacesServerSpaceDatasync());
            tupleAdapter.addSQLSpacesAdapterListener(this);
        } catch (TupleSpaceException e) {
            logger.error("Tuple space fluke " + e);
            e.printStackTrace();
            tupleAdapter = null;
        }
    }


    public void actionUponDelete(ISyncMessage syncMessage) {
        sendCallBack(syncMessage);
    }


    public void actionUponWrite(ISyncMessage syncMessage) {
        sendCallBack(syncMessage);
    }


    public void actionUponUpdate(ISyncMessage syncMessage) {
        sendCallBack(syncMessage);
    }


    private SQLSpaceAdapter getTupleAdapter() {
        if (tupleAdapter == null) {
            tupleAdapter = SQLSpacesAdapterHelper.getInstance();
            try {
                tupleAdapter.initialize(this.getClass().getName(), Configuration.getInstance().getSqlSpacesServerSpaceDatasync());
                tupleAdapter.addSQLSpacesAdapterListener(this);
            } catch (TupleSpaceException e) {
                logger.error("Tuple space fluke " + e);
                e.printStackTrace();
                tupleAdapter = null;
            }
        }
        return tupleAdapter;
    }


    @Override
    public String create(ISyncMessage syncMessage) {
        return getTupleAdapter().write(syncMessage);
    }


    @Override
    public String delete(String id) {
        return getTupleAdapter().delete(id);
    }


    @Override
    public ISyncMessage read(String id) {
        return getTupleAdapter().readById(id);
    }


    @Override
    public String update(ISyncMessage syncMessage) {
        return getTupleAdapter().write(syncMessage.getPersistenceId(), syncMessage);
    }


    @Override
    public void addScyCommunicationListener(IScyCommunicationListener listener) {
        this.communicationListeners.add(listener);
    }


    @Override
    public void handleSQLSpacesEvent(SQLSpaceAdapterEvent sqlSpaceEvent) {
        if (sqlSpaceEvent.getAction().equals(SQLSpaceAdapter.WRITE)) {
            this.actionUponWrite(sqlSpaceEvent.getScyMessage());
        } else if (sqlSpaceEvent.getAction().equals(SQLSpaceAdapter.DELETE)) {
            this.actionUponDelete(sqlSpaceEvent.getScyMessage());
        } else if (sqlSpaceEvent.getAction().equals(SQLSpaceAdapter.UPDATE)) {
            this.actionUponUpdate(sqlSpaceEvent.getScyMessage());
        }
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
        return getTupleAdapter().readAll(queryMessage);
    }


}
