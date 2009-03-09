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
import eu.scy.communications.message.ScyMessage;

public class SQLSpaceAdapter implements Callback {
    
    private final static Logger logger = Logger.getLogger(SQLSpaceAdapter.class.getName());
    public static final String SERVER_IP = "129.240.212.15";
    public static final int SERVER_PORT = 2525;
    public static final String COLLABORATION_SERVICE_SPACE = "COLLABORATION_SERVICE_SPACE";
    public static final String AWARENESS_SERVICE_SPACE = "AWARENESS_SERVICE_SPACE";
    
    private TupleSpace tupleSpace;
    private String userName = "unregistered_user";
    
    private IScyCommunicationAdapter client;

    
    
    public SQLSpaceAdapter() {
    }
    
    
    public static SQLSpaceAdapter createAdapter(String userName, String sqlSpaceName, IScyCommunicationAdapter ca) {
        SQLSpaceAdapter cs = null;
        cs = new SQLSpaceAdapter();
        cs.client = ca;
        cs.userName = userName;
        TupleSpace ts;
        Tuple template = new Tuple(String.class, String.class, String.class, String.class, String.class, String.class);
        try {
            ts = new TupleSpace(SERVER_IP, SERVER_PORT, sqlSpaceName);            
            //setup the events that client will use
            ts.eventRegister(Command.WRITE, template, cs, true);
            ts.eventRegister(Command.DELETE, template, cs, true);
        } catch (TupleSpaceException e) {
            logger.error("Tupplespace pb " + e);
            return null;
        }
        cs.tupleSpace = ts;
        return cs;
    }
    
    
    public String write(ScyMessage scyMessage, long expiration) {
        return write(null, scyMessage, expiration);
    }
    
    
    public String write(ScyMessage scyMessage) {
        return write(null, scyMessage, 0);
    }
    
    
    public String write(String sqlSpaceId, ScyMessage scyMessage) {
        return write(sqlSpaceId, scyMessage, 0);
    }
    
    public String write(String sqlSpaceId, ScyMessage scyMessage, long expiration) {        
        Tuple tuple = new Tuple(scyMessage.getUserName(), scyMessage.getToolName(), scyMessage.getId(), scyMessage.getObjectType(), scyMessage.getName(), scyMessage.getDescription());
        if (expiration > 0) {
            tuple.setExpiration(expiration);
        }
        TupleID tid = null;
        try {
            if (sqlSpaceId == null) {
                tid = this.tupleSpace.write(tuple);                
            } else {
                tid = new TupleID(Long.valueOf(sqlSpaceId));
                this.tupleSpace.update(tid, tuple);                
            }
        } catch (TupleSpaceException e) {
            logger.error("Trouble while writing or updating touple " + e);
        }
        return String.valueOf(tid.getID());
    }
    
    
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
    
    
    public String delete(String id) {
        Tuple returnTuple = null;
        try {
            returnTuple = tupleSpace.takeTupleById(new TupleID(id));
        } catch (TupleSpaceException e) {
            logger.error("Trouble while taking touple " + e);
        }
        return returnTuple == null ? null : returnTuple.getTupleID().toString();
    }
    
    
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
    
    
//    public ArrayList<String> readById(String id) {
//        Tuple returnTuple = null;
//        try {
//            returnTuple = tupleSpace.readTupleById(new TupleID(id));
//        } catch (TupleSpaceException e) {
//            logger.error("Trouble while reading touple " + e);
//        }
//        return convertTupleToStringArray(returnTuple);
//    }

    
    public ScyMessage readById(String id) {
        Tuple returnTuple = null;
        try {
            returnTuple = tupleSpace.readTupleById(new TupleID(id));
        } catch (TupleSpaceException e) {
            logger.error("Trouble while reading touple " + e);
        }
        return convertTupleToScyMessage(returnTuple);
    }
    
    
    public ArrayList<String> takeById(String id) {
        Tuple returnTuple = null;
        try {
            returnTuple = tupleSpace.takeTupleById(new TupleID(id));
        } catch (TupleSpaceException e) {
            logger.error("Trouble while take touple " + e);
        }
        return convertTupleToStringArray(returnTuple);
    }
    
    
    private ScyMessage convertTupleToScyMessage(Tuple tuple) {
        ScyMessage sm = null;
        if (tuple != null) {
            sm = new ScyMessage();
            sm.setId(tuple.getTupleID().toString());
            sm.setName("name is name is name");
            sm.setDescription("dezkriptchawn");
        }
        return sm;
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


	public void call(Command cmd, int seq, Tuple afterCmd, Tuple beforeCmd) {
		switch (cmd) {
		case WRITE:
			client.actionUponWrite(afterCmd.getField(0).getValue().toString());
			break;
		case DELETE:
			client.actionUponDelete(beforeCmd.getField(0).getValue().toString());
			break;
		}
	}

}
