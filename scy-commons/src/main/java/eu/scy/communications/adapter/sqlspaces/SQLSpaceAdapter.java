package eu.scy.communications.adapter.sqlspaces;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import eu.scy.communications.adapter.IScyCommunicationAdapter;
import eu.scy.communications.adapter.IScyCommunicationListener;
import eu.scy.communications.adapter.ScyCommunicationEvent;
import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;

public class SQLSpaceAdapter implements Callback {
    
    private final static Logger logger = Logger.getLogger(SQLSpaceAdapter.class.getName());
    public static final String SERVER_IP = "129.240.212.15";
    public static final int SERVER_PORT = 2525;
    public static final String COLLABORATION_SERVICE_SPACE = "COLLABORATION_SERVICE_SPACE";
    public static final String AWARENESS_SERVICE_SPACE = "AWARENESS_SERVICE_SPACE";
    public static final String WRITE = "WRITE";
    public static final String DELETE = "DELETE";
    public static final String UPDATE = "UPDATE";
    private TupleSpace tupleSpace;
    private String userName = "unregistered_user";
    private ArrayList<ISQLSpaceAdapterListener> sqlSpaceAdapterListeners = new ArrayList<ISQLSpaceAdapterListener>();
    
    public SQLSpaceAdapter() {
        System.out.println("SQLSpaceAdapter.SQLSpaceAdapter()");
    }
    
    /**
     * intialize the tuple space
     * 
     * @param userName
     * @param sqlSpaceName
     */
    public void initialize(String userName, String sqlSpaceName) {
        logger.debug("Created Tuple Spaces");
        this.userName = userName;
        Tuple template = new Tuple(String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class);
        try {
            Callback cb = this;
            this.tupleSpace = new TupleSpace(SERVER_IP, SERVER_PORT, sqlSpaceName);
            // setup the events that client will use
            this.tupleSpace.eventRegister(Command.WRITE, template, cb, false);
            this.tupleSpace.eventRegister(Command.DELETE, template, cb, false);
            this.tupleSpace.eventRegister(Command.UPDATE, template, cb, false);
        } catch (TupleSpaceException e) {
            logger.error("Tupplespace pb " + e);
        }
    }
    
    
    /**
     * write
     */
    public String write(IScyMessage scyMessage) {
        return write(null, scyMessage);
    }
    
    /**
     * write the tuple to a message
     * 
     * @param tupleId
     * @param sm
     * @return
     */
    public String write(String tupleId, IScyMessage sm) {
        String user = sm.getUserName();
        String tool = sm.getToolName();
        String id = sm.getId();
        String type = sm.getObjectType();
        String name = sm.getName();
        String description = sm.getDescription();
        String to = sm.getTo();
        String from = sm.getFrom();
        String purpose = sm.getMessagePurpose();
        String session = sm.getSession();
        long expiration = sm.getExpiraton();
        // username, toolName, id, objectType, name, description, to, from,
        // messagePurpose, session
        // Tuple tuple = new Tuple(String.class, tool, String.class,
        // String.class, String.class, String.class, String.class, String.class,
        // String.class);
        Tuple tuple = new Tuple(user != null ? user : "", tool != null ? tool : "", id != null ? id : "", type != null ? type : "", name != null ? name : "", description != null ? description : "", to != null ? to : "", from != null ? from : "", purpose != null ? purpose : "", session != null ? session : "");
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
                this.tupleSpace.update(tid, tuple);
            }
            logger.debug("Wrote tuple with tid: " + tid.getID());
            // force callback SUPERHACK
//            call(Command.WRITE, 0, tuple, null);
        } catch (TupleSpaceException e) {
            logger.error("Trouble while writing or updating touple " + e);
        }
        return String.valueOf(tid.getID());
    }
    
    /**
     * reads a tuple from params
     * 
     * @param userName
     * @param tool
     * @return
     */
    public ArrayList<String> read(String userName, String tool) {
        Tuple tuple;
        if (userName == null) {
            tuple = new Tuple(String.class, tool, String.class, String.class, String.class, String.class);
        } else {
            tuple = new Tuple(userName, tool, String.class, String.class, String.class, String.class);
        }
        Tuple returnTuple = null;
        try {
            returnTuple = this.tupleSpace.read(tuple);
        } catch (TupleSpaceException e) {
            logger.error("Trouble while reading touple " + e);
            return null;
        }
        return convertTupleToStringArray(returnTuple);
    }
    
    /**
     * Read all the tuples
     * 
     * @param scyMessage
     * @return
     */
    public ArrayList<IScyMessage> readAll(IScyMessage scyMessage) {
        
        Tuple tupleTemplate;
        if (scyMessage.getSession() != null) {
            tupleTemplate = new Tuple(String.class, scyMessage.getToolName(), String.class, String.class, String.class, String.class, String.class, String.class, String.class, scyMessage.getSession());
        } else {
            tupleTemplate = new Tuple(String.class, scyMessage.getToolName(), String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class);
        }
        
        Tuple returnTuple[] = null;
        try {
            returnTuple = this.tupleSpace.readAll(tupleTemplate);
        } catch (TupleSpaceException e) {
            logger.error("Trouble while reading touples " + e);
            return null;
        }
        ArrayList<IScyMessage> messages = new ArrayList<IScyMessage>();
        for (Tuple tuple : returnTuple) {
            messages.add(convertTupleToScyMessage(tuple));
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
            returnTuple = tupleSpace.takeTupleById(new TupleID(id));
        } catch (TupleSpaceException e) {
            logger.error("Trouble while taking touple " + e);
        }
     
        return returnTuple == null ? null : returnTuple.getTupleID().toString();
    }
    
   
    
    /**
     * take
     * 
     * @param tool
     * @return
     */
    public ArrayList<String> take(String tool) {
        Tuple tuple = new Tuple(String.class, tool, String.class, String.class, String.class, String.class);
        Tuple returnTuple = null;
        try {
            returnTuple = tupleSpace.take(tuple);
        } catch (TupleSpaceException e) {
            logger.error("Trouble while taking touple " + e);
            return null;
        }
        return convertTupleToStringArray(returnTuple);
    }
    
    /**
     * Read by id
     * 
     * @param id
     * @return
     */
    public IScyMessage readById(String id) {
        Tuple returnTuple = null;
        try {
            returnTuple = tupleSpace.readTupleById(new TupleID(id));
            logger.error("got tuple " + returnTuple);
        } catch (TupleSpaceException e) {
            logger.error("Trouble while reading touple " + e);
        }
        return convertTupleToScyMessage(returnTuple);
    }
    
    /**
     * Take by id
     * 
     * @param id
     * @return
     */
    public ArrayList<String> takeById(String id) {
        Tuple returnTuple = null;
        try {
            returnTuple = tupleSpace.takeTupleById(new TupleID(id));
        } catch (TupleSpaceException e) {
            logger.error("Trouble while take touple " + e);
        }
        return convertTupleToStringArray(returnTuple);
    }
    
    /**
     * Convert tuple to a scymessage
     * 
     * @param tuple to be converted
     * @return
     */
    private IScyMessage convertTupleToScyMessage(Tuple tuple) {
        // username, toolName, id, objectType, name, description, to, from,
        // messagePurpose
        if (tuple == null) {
            return null;
        }
        Field[] fields = tuple.getFields();
        return ScyMessage.createScyMessage((String) fields[0].getValue(), (String) fields[1].getValue(), (String) fields[2].getValue(), (String) fields[3].getValue(), (String) fields[4].getValue(), (String) fields[5].getValue(), (String) fields[6].getValue(), (String) fields[7].getValue(), (String) fields[8].getValue(), 0, (String) fields[9].getValue());
    }
    
    private ArrayList<String> convertTupleToStringArray(Tuple tuple) {
        ArrayList<String> returnValues = null;
        if (tuple != null) {
            returnValues = new ArrayList<String>();
            Field field;
            returnValues.add(String.valueOf(tuple.getTupleID()));
            for (int i = 0; i < tuple.getFields().length; i++) {
                field = tuple.getFields()[i];
                returnValues.add(field.getValue().toString());
            }
        }
        return returnValues;
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
                        sqlSpacesAdapterEvent = new SQLSpaceAdapterEvent(this, convertTupleToScyMessage(afterCmd), WRITE);
                        break;
                    case DELETE:
                        sqlSpacesAdapterEvent = new SQLSpaceAdapterEvent(this, convertTupleToScyMessage(afterCmd), DELETE);
                        break;
                    case UPDATE:
                        sqlSpacesAdapterEvent = new SQLSpaceAdapterEvent(this, convertTupleToScyMessage(afterCmd), UPDATE);
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
