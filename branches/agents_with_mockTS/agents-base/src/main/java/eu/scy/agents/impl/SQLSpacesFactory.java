package eu.scy.agents.impl;

import info.collide.sqlspaces.client.SQLTupleSpace;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import eu.scy.agents.api.TupleSpaceFactory;

public class SQLSpacesFactory implements TupleSpaceFactory {

	public TupleSpace createTupleSpace(String user, String host, int port,
			String space) {
		return createSpace(user, host, port, false, space);
	}

	public TupleSpace createAutonomousTupleSpace(String name, String host,
			int port, String space) {
		return createSpace(name, host, port, true, space);
	}

	private TupleSpace createSpace(String user, String host, int port,
			boolean autonomous, String space) {
		try {
			return new SQLTupleSpace(new User(user), host, port, autonomous,
					false, space);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
		return null;
	}

}
