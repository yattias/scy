package eu.scy.collaborationservice.adapter.sqlspaces;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.ArrayList;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;
import eu.scy.communications.datasync.properties.CommunicationProperties;


public class SQLSpaceAdapter implements Callback {

    private final static Logger logger = Logger.getLogger(SQLSpaceAdapter.class.getName());
    public static final String COLLABORATION_SERVICE_SPACE = "COLLABORATION_SERVICE_SPACE";
    public static final String AWARENESS_SERVICE_SPACE = "AWARENESS_SERVICE_SPACE";
    public static final String WRITE = "WRITE";
    public static final String DELETE = "DELETE";
    public static final String UPDATE = "UPDATE";
    private TupleSpace tupleSpace;
    private String userName = "unregistered_user";
    private ArrayList<ISQLSpaceAdapterListener> sqlSpaceAdapterListeners = new ArrayList<ISQLSpaceAdapterListener>();
	private Properties props;

    public static String serverIp;
    public static int serverPort;


	public SQLSpaceAdapter() {
        logger.debug("SQLSpaceAdapter.SQLSpaceAdapter()");
		try {
			props = new Properties();
            props.load(SQLSpaceAdapter.class.getResourceAsStream("collaboration.server.properties"));
			serverIp = props.getProperty("collaborationservice.address");
			serverPort = new Integer(props.getProperty("collaborationservice.port"));
        } catch (FileNotFoundException e) {
            logger.fatal("Could locate collaboration server properties file", e);
        } catch (IOException e) {
            logger.fatal("Could locate collaboration server properties file", e);
        }
    }
    
	public SQLSpaceAdapter(String serverIp, Integer serverPort) {
        logger.debug("SQLSpaceAdapter.SQLSpaceAdapter()");
		try {
			//props = new Properties();
            //props.load(SQLSpaceAdapter.class.getResourceAsStream("collaboration.server.properties"));
			this.serverIp = serverIp;
			this.serverPort = serverPort;
        } catch (Exception e) {
            logger.fatal("Could locate collaboration server properties file", e);
        }
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
            this.tupleSpace = new TupleSpace(serverIp, serverPort, sqlSpaceName);
            // setup the events that client will use
            this.tupleSpace.eventRegister(Command.WRITE, template, cb, false);
            this.tupleSpace.eventRegister(Command.DELETE, template, cb, false);
            this.tupleSpace.eventRegister(Command.UPDATE, template, cb, false);
        } catch (TupleSpaceException e) {
            logger.error("Tuplespace error", e);
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
                logger.info("Trying to update tuple: [" + tid + "] " + tuple);
                this.tupleSpace.update(tid, tuple);
                logger.info("Done updating!");
            }
            logger.debug("Wrote tuple with tid: " + tid.getID());
        } catch (TupleSpaceException e) {
            //logger.error("Trouble while writing or updating touple " + e);
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
    public ArrayList<IScyMessage> readAll(IScyMessage scyMessage) {        
        Field f1 = scyMessage.getUserName() == null ? new Field(String.class) : new Field(scyMessage.getUserName());
        Field f2 = scyMessage.getToolName() == null ? new Field(String.class) : new Field(scyMessage.getToolName());
        Field f3 = new Field(String.class);
        Field f4 = new Field(String.class);
        Field f5 = new Field(String.class);
        Field f6 = new Field(String.class);
        Field f7 = new Field(String.class);
        Field f8 = new Field(String.class);
        Field f9 = new Field(String.class);
        Field f10 = scyMessage.getSession() == null ? new Field(String.class) : new Field(scyMessage.getSession());
        
        Tuple tupleTemplate = new Tuple(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10);
        
        Tuple returnTuple[] = null;
        try {
            returnTuple = this.tupleSpace.readAll(tupleTemplate);
        } catch (TupleSpaceException e) {
            logger.error("Trouble while reading touples " + e);
            return null;
        }
        
        ArrayList<IScyMessage> messages = new ArrayList<IScyMessage>();
        if (returnTuple != null && returnTuple.length > 0) {            
            for (Tuple tuple : returnTuple) {
                messages.add(convertTupleToScyMessage(tuple));
            }
        } else {
            logger.error("readAll found no tuples matching " + scyMessage.toString());
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
     * @return IScyMessage
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
     * @return IScyMessage
     */
    public IScyMessage takeById(String id) {
        Tuple returnTuple = null;
        try {
            returnTuple = tupleSpace.takeTupleById(new TupleID(id));
        } catch (TupleSpaceException e) {
            logger.error("Trouble while take touple " + e);
        }
        return convertTupleToScyMessage(returnTuple);
    }
    
    /**
     * Convert tuple to a scymessage
     * 
     * @param tuple to be converted
     * @return
     */
    private IScyMessage convertTupleToScyMessage(Tuple tuple) {
        if (tuple == null) {
            return null;
        }
        Field[] fields = tuple.getFields();
        //FIXME: ultra-hack! over-writing id on the way back in order to add the tuple id. this needs to be solved differently.
        return ScyMessage.createScyMessage((String) fields[0].getValue(), (String) fields[1].getValue(), tuple.getTupleID().toString(), (String) fields[3].getValue(), (String) fields[4].getValue(), (String) fields[5].getValue(), (String) fields[6].getValue(), (String) fields[7].getValue(), (String) fields[8].getValue(), 0, (String) fields[9].getValue());
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
                        sqlSpacesAdapterEvent = new SQLSpaceAdapterEvent(this, convertTupleToScyMessage(beforeCmd), DELETE);
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
