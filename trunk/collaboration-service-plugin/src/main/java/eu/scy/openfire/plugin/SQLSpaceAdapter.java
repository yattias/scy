package eu.scy.openfire.plugin;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import eu.scy.core.model.impl.ScyBaseObject;

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
    
    
    public String write(String tool, ScyBaseObject object, long expiration) {
        return write(null, tool, object, expiration);
    }
    
    
    public String write(String tool, ScyBaseObject object) {
        return write(null, tool, object, 0);
    }
    
    
    public String write(String sqlSpaceId, String tool, ScyBaseObject object) {
        return write(sqlSpaceId, tool, object, 0);
    }
    
    public String write(String sqlSpaceId, String tool, ScyBaseObject object, long expiration) {
        // sqlSpaceId == tupleId
        Tuple tuple = new Tuple(this.userName, tool, object.getId(), object.getClass().getName(), object.getName(), object.getDescription());
        if (expiration > 0) {
            tuple.setExpiration(expiration);
        }
        TupleID tid = null;
        try {
            if (sqlSpaceId == null) {
                tid = this.tupleSpace.write(tuple);                
            } else {
                tid = new TupleID(Long.valueOf(sqlSpaceId));
                //tid = new TupleID(sqlSpaceId);
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
    
    
    public ArrayList<String> readById(String id) {
        Tuple returnTuple = null;
        try {
            returnTuple = tupleSpace.readTupleById(new TupleID(id));
        } catch (TupleSpaceException e) {
            logger.error("Trouble while reading touple " + e);
        }
        return convertTupleToStringArray(returnTuple);
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
    
    
    private ScyBaseObject convertTupleToScyBaseObject(Tuple tuple) {
        ScyBaseObject sbo = null;
        if (tuple != null) {
            sbo = new ScyBaseObject();
            sbo.setId(tuple.getTupleID().toString());
            sbo.setName("name is name is name");
            sbo.setDescription("dezkriptchawn");
        }
        return sbo;
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
