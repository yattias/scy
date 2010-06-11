package eu.scy.agents.impl;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.apache.log4j.Logger;

import eu.scy.agents.api.IPersistentStorage;

/**
 * The implementation of {@link IPersistentStorage}. Saves things in the TupleSpace.
 * 
 * @author Florian Schulz
 */
public class PersistentStorage implements IPersistentStorage {

	private static Logger logger = Logger.getLogger("PersistentStorage");

	// private static final String TSHOST = "localhost";
	private static final String TSHOST = "scy.collide.info";
	private static final int TSPORT = 2525;
	private static final String PERSISTENT_STORAGE = "persistent_storage_1_0";
	private TupleSpace tupleSpace;

	private static HashMap<String, TupleID> key2TupleId = new HashMap<String, TupleID>();

	/**
	 * Create a new {@link PersistentStorage} instance.
	 */
	public PersistentStorage() {
		this(TSHOST, TSPORT);
	}

	public PersistentStorage(String host, int port) {
		try {
			// TODO Configuration
			tupleSpace = new TupleSpace(new User(PERSISTENT_STORAGE), host, port, PERSISTENT_STORAGE);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key) {
		try {
			Tuple t = tupleSpace.read(new Tuple(PERSISTENT_STORAGE, key, Field.createWildCardField()));
			if (t == null) {
				logger.info("Tuple for key " + key + " is null.");
				return null;
			}
			byte[] byteArray = (byte[]) t.getField(2).getValue();
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteArray);
			ObjectInputStream objectIn = new ObjectInputStream(byteIn);
			T object = (T) objectIn.readObject();
			objectIn.close();
			return object;
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public <T> void put(String key, T value) {
		if (!key2TupleId.containsKey(key)) {
			try {
				ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
				ObjectOutputStream objectOut = new ObjectOutputStream(byteArray);
				objectOut.writeObject(value);
				objectOut.close();
				TupleID id = tupleSpace.write(new Tuple(PERSISTENT_STORAGE, key, byteArray.toByteArray()));
				key2TupleId.put(key, id);
			} catch (TupleSpaceException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			try {
				TupleID tupleId = key2TupleId.get(key);
				ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
				ObjectOutputStream objectOut;
				objectOut = new ObjectOutputStream(byteArray);
				objectOut.writeObject(value);
				objectOut.close();
				tupleSpace.update(tupleId, new Tuple(PERSISTENT_STORAGE, key, byteArray.toByteArray()));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TupleSpaceException e) {
				e.printStackTrace();
			}

		}
	}

	public void remove(String key) {
		if (key2TupleId.containsKey(key)) {
			TupleID tupleId = key2TupleId.get(key);
			try {
				tupleSpace.takeTupleById(tupleId);
				key2TupleId.remove(key);
			} catch (TupleSpaceException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		if (tupleSpace.isConnected()) {
			try {
				tupleSpace.disconnect();
			} catch (TupleSpaceException e) {
				e.printStackTrace();
			}
		}
		key2TupleId.clear();
	}

}
