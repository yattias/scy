package eu.scy.agents.impl;

import info.collide.sqlspaces.client.SQLTupleSpace;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import eu.scy.agents.api.IPersistentStorage;

/**
 * The implementation of {@link IPersistentStorage}. Saves things in the
 * TupleSpace.
 * 
 * @author Florian Schulz
 * 
 */
public class PersistentStorage implements IPersistentStorage {

	// private static final String TSHOST = "localhost";
	private static final String TSHOST = "localhost";
	private static final int TSPORT = 2525;
	private static final String PERSISTENT_STORAGE = "persistent_storage_1_0";
	private TupleSpace tupleSpace;

	/**
	 * Create a new {@link PersistentStorage} instance.
	 */
	public PersistentStorage() {
		this(TSHOST, TSPORT);
	}

	public PersistentStorage(String host, int port) {
		try {
			// TODO Configuration
			tupleSpace = new SQLTupleSpace(new User(PERSISTENT_STORAGE), host,
					port, PERSISTENT_STORAGE);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key) {
		try {
			Tuple t = tupleSpace.read(new Tuple(PERSISTENT_STORAGE, key, Field
					.createWildCardField()));
			byte[] byteArray = (byte[]) t.getField(2).getValue();
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteArray);
			ObjectInputStream objectIn = new ObjectInputStream(byteIn);
			return (T) objectIn.readObject();
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
		try {
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			ObjectOutputStream objectOut = new ObjectOutputStream(byteArray);
			objectOut.writeObject(value);
			objectOut.close();
			tupleSpace.write(new Tuple(PERSISTENT_STORAGE, key, byteArray
					.toByteArray()));
		} catch (TupleSpaceException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
