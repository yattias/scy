package eu.scy.agents.authoring;

import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.rmi.dgc.VMID;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    public void testRun() throws TupleSpaceException, InterruptedException {
        this.getCommandSpace().write(
                new Tuple(NotifyUserAgent.SEND_NOTIFICATION, "FromTeacher",
                        "ToUser", "InMission1", "STFU"));

        Thread.sleep(AgentProtocol.SECOND * 5);

        Tuple notificationTuple = this.getCommandSpace().waitToTake(
                new Tuple(AgentProtocol.NOTIFICATION, Field.createWildCardField()), AgentProtocol.ALIVE_INTERVAL);

        assertNotNull("no notification received", notificationTuple);
        assertEquals("text=STFU", notificationTuple.getField(7).getValue());
    }
}
