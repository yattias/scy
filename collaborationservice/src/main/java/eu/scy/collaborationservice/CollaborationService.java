package eu.scy.collaborationservice;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.ArrayList;

import org.apache.log4j.Logger;

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
}
