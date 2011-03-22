package eu.scy.agents.roolo.rooloaccessor;

import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.rmi.dgc.VMID;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import roolo.elo.api.IMetadata;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.content.BasicContent;

import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.ModelStorage;
import eu.scy.agents.topics.DetectTopicForElos;
import eu.scy.agents.topics.TopicAgents;
import eu.scy.agents.topics.TopicDetector;

public class RooloAcccessorAgentTest extends AbstractTestFixture {

	private String mission = "roolo://memory/0/0/Design+a+CO2-friendly+house.scymissionspecification";

	@BeforeClass
	public static void startTS() {
		startTupleSpaceServer();
	}

	@AfterClass
	public static void stopTS() {
		stopTupleSpaceServer();
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(AgentProtocol.PARAM_AGENT_ID, new VMID());
		params.put(AgentProtocol.TS_HOST, TSHOST);
		params.put(AgentProtocol.TS_PORT, TSPORT);
		agentMap.put(RooloAccessorAgent.class.getName(), params);

		startAgentFramework(agentMap);
	}

	@After
	public void tearDown() throws AgentLifecycleException {
		stopAgentFrameWork();
		super.tearDown();
	}

	@Test
	public void testGetModels() {
		ModelStorage storage = new ModelStorage(getCommandSpace());
		byte[] bs = storage.get(mission, "en", "topicModel");
		assertNotNull(bs);
	}

}
