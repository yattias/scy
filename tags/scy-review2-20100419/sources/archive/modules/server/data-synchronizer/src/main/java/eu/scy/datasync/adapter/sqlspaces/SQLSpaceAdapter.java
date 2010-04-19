package eu.scy.datasync.adapter.sqlspaces;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.TupleSpaceException.TupleSpaceError;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import eu.scy.common.configuration.Configuration;
import eu.scy.communications.datasync.properties.CommunicationProperties;
import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessageHelper;


public class SQLSpaceAdapter implements Callback {

    private final static Logger logger = Logger.getLogger(SQLSpaceAdapter.class.getName());

    public static final String WRITE = "WRITE";
    public static final String DELETE = "DELETE";
    public static final String UPDATE = "UPDATE";
    private static final Tuple tupleSpaceTemplate = new Tuple(String.class, String.class, String.class, String.class, String.class);
    private TupleSpace tupleSpace;
    private String userName = "unregistered_user";
    private ArrayList<ISQLSpaceAdapterListener> sqlSpaceAdapterListeners = new ArrayList<ISQLSpaceAdapterListener>();

    public SQLSpaceAdapter() {
        logger.debug("SQLSpaceAdapter.SQLSpaceAdapter()");
    }

    public SQLSpaceAdapter(String serverHost, Integer serverPort, String sqlSpaceName) {
        logger.info("CREATING TUPLESPACE: " + serverHost + " SERVERPORT: " + serverPort + " SQLSPACE NAME: " + sqlSpaceName);
        try {
            this.tupleSpace = new TupleSpace(serverHost, serverPort, sqlSpaceName);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    /**
     * intialize the tuple space
     *
     * @param userName
     * @param sqlSpaceName
     */
    public void initialize(String userName, String sqlSpaceName) throws TupleSpaceException {
        if (this.tupleSpace == null) {
            this.tupleSpace = new TupleSpace(Configuration.getInstance().getSqlSpacesServerHost(), Configuration.getInstance().getSqlSpacesServerPort(), sqlSpaceName);
            if (this.tupleSpace == null) {
                throw new TupleSpaceException(TupleSpaceError.DATABASE, "could not establish connection using host/port/spacename " + Configuration.getInstance().getSqlSpacesServerHost() + "/" + Configuration.getInstance().getSqlSpacesServerPort() + "/" + sqlSpaceName);
            }
        }

        this.userName = userName;
        Callback cb = this;
        // setup the events that client will use
        this.tupleSpace.eventRegister(Command.WRITE, tupleSpaceTemplate, cb, false);
        this.tupleSpace.eventRegister(Command.DELETE, tupleSpaceTemplate, cb, false);
        this.tupleSpace.eventRegister(Command.UPDATE, tupleSpaceTemplate, cb, false);
        logger.debug("Successfully created Tuple Space " + sqlSpaceName);
    }


    /**
     * write
     */
    public String write(ISyncMessage syncMessage) {
        return write(null, syncMessage);
    }

    /**
     * write the tuple to a message
     *
     * @param tupleId
     * @param syncMessage
     * @return
     */
    public String write(String tupleId, ISyncMessage syncMessage) {

        String toolSessionId = syncMessage.getToolSessionId();
        String toolId = syncMessage.getToolId();
        String from = syncMessage.getFrom();
        String content = syncMessage.getContent();
        String event = syncMessage.getEvent();
        long expiration = syncMessage.getExpiration();

        Tuple tuple = new Tuple(toolSessionId != null ? toolSessionId : "", toolId != null ? toolId : "", from != null ? from : "", content != null ? content : "", event != null ? event : "");

        if (expiration > 0) {
            tuple.setExpiration(expiration);
        }
        TupleID tid = null;
        try {
            if (tupleId == null) {
                tid = this.tupleSpace.write(tuple);
            } else {
                tid = new TupleID(Long.valueOf(tupleId));
                this.tupleSpace.update(tid, tuple);
            }
        } catch (TupleSpaceException e) {
            logger.error("Trouble while writing or updating touple " + e);
            e.printStackTrace();
        }
        return String.valueOf(tid.getID());
    }


    /**
     * Read all the tuples
     *
     * @param scyMessage
     * @return
     */
    public ArrayList<ISyncMessage> readAll(ISyncMessage syncMessage) {
        Field f1 = syncMessage.getToolSessionId() == null ? new Field(String.class) : new Field(syncMessage.getToolSessionId());
        Field f2 = syncMessage.getToolId() == null ? new Field(String.class) : new Field(syncMessage.getToolId());
        Field f3 = syncMessage.getFrom() == null ? new Field(String.class) : new Field(syncMessage.getFrom());
        Field f4 = syncMessage.getContent() == null ? new Field(String.class) : new Field(syncMessage.getContent());
        Field f5 = syncMessage.getEvent() == null ? new Field(String.class) : new Field(syncMessage.getEvent());

        Tuple tupleTemplate = new Tuple(f1, f2, f3, f4, f5);

        Tuple returnTuple[] = null;
        try {
            returnTuple = this.tupleSpace.readAll(tupleTemplate);
        } catch (TupleSpaceException e) {
            logger.error("Trouble while reading touples " + e);
            return null;
        }

        ArrayList<ISyncMessage> messages = new ArrayList<ISyncMessage>();
        if (returnTuple != null && returnTuple.length > 0) {
            for (Tuple tuple : returnTuple) {
                messages.add(convertTupleToSyncMessage(tuple));
            }
        } else {
            logger.error("readAll found no tuples matching " + syncMessage.toString());
        }
        return messages;
    }

    /**
     * Delete tuple
     *
     * @param id
     * @return
     */
    public String delete(String id) {
        Tuple returnTuple = null;
        try {
            Long.parseLong(id);
            try {
                returnTuple = tupleSpace.takeTupleById(new TupleID(id));
            } catch (TupleSpaceException e2) {
                logger.error("Trouble while taking touple " + e2);
            }
        } catch (NumberFormatException e1) {
            logger.error("Somebody seems to think a non-number is a number " + e1);
        }

        return returnTuple == null ? null : returnTuple.getTupleID().toString();
    }


    /**
     * Read by id
     *
     * @param id
     * @return ISyncMessage
     */
    public ISyncMessage readById(String id) {
        Tuple returnTuple = null;
        try {
            returnTuple = tupleSpace.readTupleById(new TupleID(id));
        } catch (TupleSpaceException e) {
            logger.error("Trouble while reading touple " + e);
        }
        return convertTupleToSyncMessage(returnTuple);
    }

    /**
     * Take by id
     *
     * @param id
     * @return IScyMessage
     */
    public ISyncMessage takeById(String id) {
        Tuple returnTuple = null;
        try {
            returnTuple = tupleSpace.takeTupleById(new TupleID(id));
        } catch (TupleSpaceException e) {
            logger.error("Trouble while take touple " + e);
        }
        return convertTupleToSyncMessage(returnTuple);
    }

    /**
     * Convert tuple to a ISyncMessage
     *
     * @param tuple to be converted
     * @return ISyncMessage
     */
    private ISyncMessage convertTupleToSyncMessage(Tuple tuple) {
        if (tuple == null) {
            return null;
        }
        Field[] fields = tuple.getFields();
        return SyncMessageHelper.createSyncMessage((String) fields[0].getValue(), (String) fields[1].getValue(), (String) fields[2].getValue(), null, (String) fields[3].getValue(), (String) fields[4].getValue(), tuple.getTupleID().toString(), tuple.getExpiration());
    }


    /**
     * This is the sqlspaces callback
     */
    public void call(Command cmd, int seq, Tuple afterCmd, Tuple beforeCmd) {

        SQLSpaceAdapterEvent sqlSpacesAdapterEvent = null;

        for (ISQLSpaceAdapterListener theListener : sqlSpaceAdapterListeners) {
            if (theListener != null) {

                switch (cmd) {
                    case WRITE:
                        sqlSpacesAdapterEvent = new SQLSpaceAdapterEvent(this, convertTupleToSyncMessage(afterCmd), WRITE);
                        break;
                    case DELETE:
                        sqlSpacesAdapterEvent = new SQLSpaceAdapterEvent(this, convertTupleToSyncMessage(beforeCmd), DELETE);
                        break;
                    case UPDATE:
                        sqlSpacesAdapterEvent = new SQLSpaceAdapterEvent(this, convertTupleToSyncMessage(afterCmd), UPDATE);
                        break;
                }
                theListener.handleSQLSpacesEvent(sqlSpacesAdapterEvent);
            }
        }

    }

    /**
     * Adds a listener
     *
     * @param sqlSpacesAdapterListener
     */
    public void addSQLSpacesAdapterListener(ISQLSpaceAdapterListener sqlSpacesAdapterListener) {
        this.sqlSpaceAdapterListeners.add(sqlSpacesAdapterListener);
    }
}
