package eu.scy.agents.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import eu.scy.agents.keywords.workflow.KeywordWorkflowConstants;

public class UserLanguageAgentTest extends AbstractTestFixture {

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
		agentMap.put(UserLanguageAgent.NAME, params);
		startAgentFramework(agentMap);
	}

	@Override
	@After
	public void tearDown() throws AgentLifecycleException {
		try {
			getCommandSpace().take(
					new Tuple("persistent_storage_1_0",
							KeywordWorkflowConstants.DOCUMENT_FREQUENCY_MODEL,
							Field.createWildCardField()));
			removeTopicModel();
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
		stopAgentFrameWork();
		super.tearDown();
	}

	@Test
	public void testRun() throws TupleSpaceException {
		Tuple tuple = new Tuple(AgentProtocol.ACTION, new VMID().toString(),
				System.currentTimeMillis(), "type", "harald", "tool",
				"mission1", "session", "eloUri", "language=de");
		getActionSpace().write(tuple);
		Tuple tuple2 = new Tuple(AgentProtocol.ACTION, new VMID().toString(),
				System.currentTimeMillis(), "type", "sophie", "tool",
				"mission1", "session", "eloUri", "language=en");
		getActionSpace().write(tuple2);

		String queryId = new VMID().toString();
		getCommandSpace().write(
				new Tuple(UserLanguageAgent.LANGUAGE, AgentProtocol.QUERY,
						queryId, "mission1", "harald"));
		Tuple response = getCommandSpace().waitToTake(
				new Tuple(UserLanguageAgent.LANGUAGE, AgentProtocol.RESPONSE,
						queryId, String.class),
				AgentProtocol.ALIVE_INTERVAL * 3);
		assertNotNull(response);
		assertEquals("de", response.getField(3).getValue());

		String queryId2 = new VMID().toString();
		getCommandSpace().write(
				new Tuple(UserLanguageAgent.LANGUAGE, AgentProtocol.QUERY,
						queryId2, "mission1", "sophie"));
		Tuple response2 = getCommandSpace().waitToTake(
				new Tuple(UserLanguageAgent.LANGUAGE, AgentProtocol.RESPONSE,
						queryId2, String.class),
				AgentProtocol.ALIVE_INTERVAL * 3);
		assertNotNull(response2);
		assertEquals("en", response2.getField(3).getValue());
	}
}