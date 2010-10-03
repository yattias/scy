package eu.scy.agents.authoring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

public class NotifyUserAgentTest extends AbstractTestFixture {

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
		params.put(AgentProtocol.PARAM_AGENT_ID, new VMID());
		params.put(AgentProtocol.TS_HOST, TSHOST);
		params.put(AgentProtocol.TS_PORT, TSPORT);
		this.agentMap.put(NotifyUserAgent.NAME, params);
		this.startAgentFramework(this.agentMap);
	}

	@Override
	@After
	public void tearDown() {
		try {
			this.stopAgentFrameWork();
			super.tearDown();
		} catch (AgentLifecycleException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testRun() throws TupleSpaceException {
		this.getCommandSpace().write(
				new Tuple(NotifyUserAgent.SEND_NOTIFICATION, "FromTeacher",
						"ToUser", "InMission1", "STFU"));

		Tuple notificationTuple = this.getCommandSpace().waitToTake(
				new Tuple(AgentProtocol.NOTIFICATION, String.class, "ToUser",
						"growl", "FromTeacher", "InMission1", String.class,
						"type=message_notification", String.class),
				AgentProtocol.ALIVE_INTERVAL);

		assertNotNull("no notification received", notificationTuple);
		assertEquals("STFU", notificationTuple.getField(8).getValue());
	}
}
