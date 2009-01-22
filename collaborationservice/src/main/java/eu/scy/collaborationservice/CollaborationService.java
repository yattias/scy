package eu.scy.collaborationservice;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.sun.org.apache.xml.internal.serializer.ToUnknownStream;

import eu.scy.core.model.impl.ScyBaseObject;

public class CollaborationService {

	private final static Logger logger = Logger.getLogger(CollaborationService.class.getName());
	private static final String SERVER_IP = "129.240.212.15";
	private static final int SERVER_PORT = 2525;
    public static final String COLLABORATION_SERVICE_SPACE = "COLLABORATION_SERVICE_SPACE";
    public static final String AWARENESS_SERVICE_SPACE = "AWARENESS_SERVICE_SPACE";

	private TupleSpace tupleSpace;
	private String userName = "unregistered_user";

	

	public CollaborationService() {
	}


	public static CollaborationService createCollaborationService(String userName, String sqlSpaceName) {
		CollaborationService cs = null;
		TupleSpace ts;
		try {
			ts = new TupleSpace(SERVER_IP, SERVER_PORT, sqlSpaceName);
		} catch (TupleSpaceException e) {
			logger.error("Tupplespace pb " + e);
			return null;
		}
		cs = new CollaborationService();
		cs.tupleSpace = ts;
		cs.userName = userName;
		return cs;
	}


	public String write(String tool, ScyBaseObject object) {
	    return write(tool, object, 0);
	}
	
	
    public String write(String tool, ScyBaseObject object, long expiration) {
        TupleID tid = null;
        Tuple tuple = new Tuple(this.userName, tool, object.getId(), object.getClass().getName(), object.getName(), object.getDescription());
        if (expiration > 0) {
            tuple.setExpiration(expiration);
        }
        try {
            tid = this.tupleSpace.write(tuple);
        } catch (TupleSpaceException e) {
            logger.error("Trouble while writing touple " + e);
        }
        return String.valueOf(tid.getID());
    }
    

	public ArrayList<String> read(String tool) {
		Tuple tuple = new Tuple(String.class, tool, String.class, String.class, String.class, String.class);
		Tuple returnTuple;
		try {
			returnTuple = this.tupleSpace.read(tuple);
		} catch (TupleSpaceException e) {
			logger.error("Trouble while reading touple " + e);
			return null;
		}
		ArrayList<String> returnValues = null;
		if (returnTuple != null) {
			returnValues = new ArrayList<String>();
			Field field;
			for (int i = 0; i < returnTuple.getFields().length; i++) {
				field = returnTuple.getFields()[i];
				returnValues.add(field.getValue().toString());
			}
		}
		return returnValues;
	}
	
    public ArrayList<String> take(String tool) {
        Tuple tuple = new Tuple(String.class, tool, String.class, String.class, String.class, String.class);
        Tuple returnTuple;
        try {
            returnTuple = this.tupleSpace.take(tuple);
        } catch (TupleSpaceException e) {
            logger.error("Trouble while taking touple " + e);
            return null;
        }
        ArrayList<String> returnValues = null;
        if (returnTuple != null) {
            returnValues = new ArrayList<String>();
            Field field;
            for (int i = 0; i < returnTuple.getFields().length; i++) {
                field = returnTuple.getFields()[i];
                returnValues.add(field.getValue().toString());
            }
        }
        return returnValues;
    }
    
    
    public void takeById(String id) {
        try {
           this.tupleSpace.takeTupleById(new TupleID(id));
        } catch (TupleSpaceException e) {
            logger.error("Trouble while taking touple " + e);
        }
    }
}
