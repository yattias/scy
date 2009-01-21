package eu.scy.collaborationservice;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import eu.scy.core.model.impl.ScyBaseObject;

public class CollaborationService {

	private TupleSpace tupleSpace;
	private final static Logger logger = Logger.getLogger(CollaborationService.class.getName());
	private static final String SERVER_IP = "129.240.212.15";
	private static final int SERVER_PORT = 2525;
	private static final String SQLSPACE_NAME = "COLLABORATION_SERVICE_SPACE";
	private String userName = "unregistered_user";


	public CollaborationService() {
	}


	public static CollaborationService createCollaborationService() {
		CollaborationService cs = null;
		TupleSpace ts;
		try {
			ts = new TupleSpace(SERVER_IP, SERVER_PORT, SQLSPACE_NAME);
		} catch (TupleSpaceException e) {
			logger.error("Tupplespace pb " + e);
			return null;
		}
		cs = new CollaborationService();
		cs.tupleSpace = ts;
		return cs;
	}


	public void write(String tool, ScyBaseObject object) {
		try {
			this.tupleSpace.write(new Tuple(this.userName, tool, object.getId(), object.getClass().getName(), object.getName(), object.getDescription()));
		} catch (TupleSpaceException e) {
			logger.error("Trouble while writing touple " + e);
		}
	}


	public ArrayList<String> read(String tool) {
		return read(tool, false);
	}


	public ArrayList<String> read(String tool, boolean alsoDelete) {
		Tuple tuple = new Tuple(String.class, tool, String.class, String.class, String.class, String.class);
		Tuple returnTuple;
		try {
			if (alsoDelete) {
				returnTuple = this.tupleSpace.take(tuple);
			} else {
				returnTuple = this.tupleSpace.read(tuple);
			}
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


	public void setUserName(String username) {
		this.userName = username;
	}

}
