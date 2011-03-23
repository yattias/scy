package eu.scy.agents.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.dgc.VMID;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import eu.scy.agents.api.IPersistentStorage;

/**
 * The implementation of {@link IPersistentStorage}. Saves things as ELOs
 * 
 * @author Florian Schulz
 */
public class ModelStorage {

	// private RooloServices rooloServices;

	private TupleSpace tupleSpace;

	/**
	 * Create a new {@link ModelStorage} instance.
	 */
	public ModelStorage(TupleSpace space) {
		tupleSpace = space;
	}

	public byte[] get(String mission, String language, String key) {
		try {
			String requestUid = new VMID().toString();
			tupleSpace.write(new Tuple(requestUid, "roolo-agent",
					"agent-model", mission, language, key));
			Tuple response = tupleSpace.waitToTake(new Tuple(requestUid,
					"roolo-response", Field.createWildCardField()),
					AgentProtocol.MINUTE);
			if (response != null) {
				Field field = response.getField(2);
				Object value = field.getValue();
				if (value instanceof String) {
					if ("not_present".equals(value)) {
						return null;
					}
				}
				if (field != null) {
					return (byte[]) value;
				}
			}
		} catch (TupleSpaceException e) {
			// exception is allowed but returns null.

		}
		return null;
	}

	public Object getAsObject(String mission, String language, String key) {
		byte[] bytes = get(mission, language, key);
		if (bytes == null) {
			return null;
		}
		try {
			ObjectInputStream inputStream = new ObjectInputStream(
					new ByteArrayInputStream(bytes));
			return inputStream.readObject();
		} catch (IOException e) {
			// exception is allowed but returns null.
		} catch (ClassNotFoundException e) {
			// exception is allowed but returns null.
		}
		return null;
	}

}
