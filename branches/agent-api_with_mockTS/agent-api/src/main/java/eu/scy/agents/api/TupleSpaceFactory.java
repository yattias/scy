package eu.scy.agents.api;

import info.collide.sqlspaces.client.TupleSpace;

public interface TupleSpaceFactory {

	public TupleSpace createTupleSpace(String user, String host, int port,
			String space);

	public TupleSpace createAutonomousTupleSpace(String name, String host,
			int port, String space);

}
