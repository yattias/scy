package eu.scy.datasync.adapter.sqlspaces;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import eu.scy.datasync.api.ISyncMessage;
import eu.scy.datasync.impl.SyncMessage;



public class SQLSpaceAdapter implements Callback {
    
    private final static Logger logger = Logger.getLogger(SQLSpaceAdapter.class.getName());
    //public static final String SERVER_IP = "129.240.212.15";
    public static final String SERVER_IP = "scy.collide.info";
    public static final int SERVER_PORT = 2525;
//    public static final String COLLABORATION_SERVICE_SPACE = "COLLABORATION_SERVICE_SPACE";
//    public static final String AWARENESS_SERVICE_SPACE = "AWARENESS_SERVICE_SPACE";
    public static final String DATA_SYNCHRONIZATION_SPACE = "DATA_SYNCHRONIZATION_SPACE";
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
    
    /**
     * intialize the tuple space
     * 
     * @param userName
     * @param sqlSpaceName
     */
    public void initialize(String userName, String sqlSpaceName) {
        this.userName = userName;
        try {
            Callback cb = this;
            this.tupleSpace = new TupleSpace(SERVER_IP, SERVER_PORT, sqlSpaceName);
            // setup the events that client will use
            this.tupleSpace.eventRegister(Command.WRITE, tupleSpaceTemplate, cb, false);
            this.tupleSpace.eventRegister(Command.DELETE, tupleSpaceTemplate, cb, false);
            this.tupleSpace.eventRegister(Command.UPDATE, tupleSpaceTemplate, cb, false);
            logger.debug("Successfully created Tuple Space " + sqlSpaceName);
        } catch (TupleSpaceException e) {
            logger.error("Tuplespace fluke " + e);
        }
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
        logger.debug("About to write tuple: " + tuple);
        
        if (expiration > 0) {
            tuple.setExpiration(expiration);
            logger.debug(" ... with expiration: " + expiration);
        }
        TupleID tid = null;
        try {
            if (tupleId == null) {
                tid = this.tupleSpace.write(tuple);
            } else {
                tid = new TupleID(Long.valueOf(tupleId));
                logger.info("Trying to update tuple: [" + tid + "] " + tuple);
                this.tupleSpace.update(tid, tuple);
                logger.info("Done updating!");
            }
            logger.debug("Wrote tuple with tid: " + tid.getID());
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
        Field f4 = new Field(String.class);
        Field f5 = new Field(String.class);
        
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
                logger.debug("Deleted tuple with id: " + id);
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
            logger.error("got tuple " + returnTuple);
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
        return SyncMessage.createSyncMessage((String) fields[0].getValue(), (String) fields[1].getValue(), tuple.getTupleID().toString(), (String) fields[3].getValue(), (String) fields[4].getValue(), tuple.getTupleID().toString());
    }

    
    /**
     * This is the sqlspaces callback
     * 
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
