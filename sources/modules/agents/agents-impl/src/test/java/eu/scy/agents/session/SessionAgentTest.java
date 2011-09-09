package eu.scy.agents.session;

import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.Mission;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.rmi.dgc.VMID;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class SessionAgentTest extends AbstractTestFixture {

    private Session session;

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
        agentMap.put(SessionAgent.NAME, params);
        startAgentFramework(agentMap);

        session = new Session(getSessionSpace());
    }

    @Override
    @After
    public void tearDown() throws AgentLifecycleException {
        stopAgentFrameWork();
        super.tearDown();
    }

    @Test
    public void testRun() throws TupleSpaceException, InterruptedException {
        login("harald", MISSION1, "co2", "de", "co2_2");
        //        Tuple tuple = new Tuple(ActionConstants.ACTION, new VMID().toString(),
        //                System.currentTimeMillis(), ActionConstants.ACTION_LOG_IN,
        //                "harald", "tool", "mission1", "session", "eloUri",
        //                "language=de", "missionSpecification=" + MISSION1,
        //                "missionName=co2", "missionId=co_2");
        //        getActionSpace().write(tuple);
        login("sophie", MISSION1, "co2", "de", "eco");
        //        Tuple tuple2 = new Tuple(ActionConstants.ACTION, new VMID().toString(),
        //                System.currentTimeMillis(), ActionConstants.ACTION_LOG_IN,
        //                "sophie", "tool", "mission1", "session", "eloUri2",
        //                "language=de", "missionSpecification=" + MISSION1,
        //                "missionName=co2", "missionId=co_2");
        //        getActionSpace().write(tuple2);
        Thread.sleep(1000);
        assertEquals("de", session.getLanguage("harald"));
        assertEquals(Mission.MISSION1, session.getMission("harald"));
        assertEquals(Mission.MISSION2, session.getMission("sophie"));
    }
}
