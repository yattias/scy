package eu.scy.agents.impl.manager;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.client.mock.MockTupleSpace;
import eu.scy.agents.api.TupleSpaceFactory;

public class MockTSFactory implements TupleSpaceFactory {

	private MockTupleSpace mockTS;

	MockTSFactory() {
		mockTS = new MockTupleSpace();
	}

	@Override
	public TupleSpace createTupleSpace(String user, String host, int port,
			String space) {
		return mockTS;
	}

	@Override
	public TupleSpace createAutonomousTupleSpace(String name, String host,
			int port, String commandSpaceName) {
		return mockTS;
	}

	public void clearTupleSpace() {
		mockTS.clear();
	}
}
