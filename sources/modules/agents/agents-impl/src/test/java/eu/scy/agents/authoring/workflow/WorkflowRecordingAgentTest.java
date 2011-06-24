package eu.scy.agents.authoring.workflow;

import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.authoring.workflow.paths.MockTimer;
import eu.scy.agents.impl.AgentProtocol;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.rmi.dgc.VMID;
import java.util.HashMap;

/** @author fschulz */
public class WorkflowRecordingAgentTest extends AbstractTestFixture {

	private static final String CONCEPTUALIZATION_DESIGN = "conceptualizationDesign";
	private static final String START_PAGE = "startPage";
	private MockTimer timer;

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
		timer = new MockTimer();
		params.put("Timer", timer);
		params.put("CheckPeriod", 1);
		agentMap.put(WorkflowRecordingAgent.NAME, params);
		startAgentFramework(agentMap);
	}

	@Override
	@After
	public void tearDown() throws AgentLifecycleException {
		stopAgentFrameWork();
		super.tearDown();
	}

	@Test
	public void run() throws InterruptedException, TupleSpaceException {
		timer.setTime(0);
		getCommandSpace()
				.write(lasChangeTuple("User1", MISSION1, START_PAGE,
						START_PAGE, "n/a"));
		// Thread.sleep(4 * AgentProtocol.SECOND);
		timer.addTime(60 * AgentProtocol.MINUTE);
		getCommandSpace().write(
				lasChangeTuple("User1", MISSION1, CONCEPTUALIZATION_DESIGN,
						START_PAGE, "n/a"));
		// Thread.sleep(4 * AgentProtocol.SECOND);
		timer.addTime(60 * AgentProtocol.MINUTE);
		getCommandSpace().write(
				lasChangeTuple("User1", MISSION1, START_PAGE,
						CONCEPTUALIZATION_DESIGN, "n/a"));
		// Thread.sleep(4 * AgentProtocol.SECOND);
		timer.addTime(60 * AgentProtocol.MINUTE);
		getCommandSpace().write(
				lasChangeTuple("User1", MISSION1, CONCEPTUALIZATION_DESIGN,
						START_PAGE, "n/a"));
		// Thread.sleep(4 * AgentProtocol.SECOND);
		timer.addTime(65 * AgentProtocol.MINUTE);

		Thread.sleep(1 * AgentProtocol.MINUTE);
		Tuple[] excesses = getSessionSpace().readAll(new Tuple());
		System.out.println(excesses);
	}

}
