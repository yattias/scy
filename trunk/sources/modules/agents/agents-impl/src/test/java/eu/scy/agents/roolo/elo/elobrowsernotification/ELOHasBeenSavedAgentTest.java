package eu.scy.agents.roolo.elo.elobrowsernotification;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.rmi.dgc.VMID;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;

public class ELOHasBeenSavedAgentTest extends AbstractTestFixture {

	private ELOHasBeenSavedAgent agent;

	@BeforeClass
	public static void startTS() {
		startTupleSpaceServer();
	}

	@AfterClass
	public static void stopTS() {
		stopTupleSpaceServer();
	}

	@Override
    @Before
	public void setUp() throws Exception {
		super.setUp();

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(AgentProtocol.PARAM_AGENT_ID, new VMID().toString());
		agent = new ELOHasBeenSavedAgent(params);
	}

	@Override
    @After
	public void tearDown() throws AgentLifecycleException {
		super.tearDown();
	}

	@Test
	public void testProcessELOSavedAction() throws TupleSpaceException {
		agent.processELOSavedAction("actionId", "florian", 123, "bla",
				MISSION1, "no", "roolo://memory/1/Something.xml", "scy/form");
		Tuple tuple = getCommandSpace()
				.waitToTake(
						new Tuple(AgentProtocol.NOTIFICATION,
								Field.createWildCardField()),
						AgentProtocol.SECOND * 10);
		assertNotNull(tuple);

		agent.processELOSavedAction("actionId", "florian", 123, "bla",
				MISSION1, "no", "roolo://memory/1/Something.xml",
				"scy/webresourcer");
		Tuple tuple2 = getCommandSpace()
				.waitToTake(
						new Tuple(AgentProtocol.NOTIFICATION,
								Field.createWildCardField()),
						AgentProtocol.SECOND * 10);
		assertNotNull(tuple2);

		agent.processELOSavedAction("actionId", "florian", 123, "bla",
				MISSION1, "no", "roolo://memory/1/Something.xml",
				"scy/feedback");
		Tuple tuple3 = getCommandSpace()
				.waitToTake(
						new Tuple(AgentProtocol.NOTIFICATION,
								Field.createWildCardField()),
						AgentProtocol.SECOND * 10);
		assertNull(tuple3);
	}
}
