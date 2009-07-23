package eu.scy.agents.impl.manager;

import static org.junit.Assert.assertTrue;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.server.Server;

import java.rmi.dgc.VMID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.ThreadedAgentMock;

public class AgentManagerTest {

    private static AgentManager agentManager;

    private static final int TS_PORT = 2525;

    private static final String TS_HOST = "localhost";

    private static final boolean standalone = false;

    @BeforeClass
    public static void setUp() {

        if (!Server.isRunning() && standalone) {
            Configuration.getConfiguration().setSSLEnabled(false);
            Server.startServer();
        }

        agentManager = new AgentManager(TS_HOST, TS_PORT);
    }

    @AfterClass
    public static void tearDown() {
        agentManager.cleanUp();
        if (standalone) {
            Server.stopServer();
        }
    }

    @Test
    public void testStartStopAgent() throws InterruptedException, AgentLifecycleException {
        IThreadedAgent agent = agentManager.startAgent(ThreadedAgentMock.NAME, null);
        // agentManager.startAgent(ThreadedAgentMock.NAME);
        Thread.sleep(5000);

        assertTrue("Agent not started", agent.isRunning());
        agentManager.stopAgent(agent.getId());
        System.out.println("AgentID(testStartStopAgent) : " + agent.getId());
        Thread.sleep(1000);

        assertTrue("agent not stopped", !agent.isRunning());
    }

    @Test
    public void testIdentify() throws AgentLifecycleException, InterruptedException, TupleSpaceException {
        IThreadedAgent agent = agentManager.startAgent(ThreadedAgentMock.NAME, null);
        Thread.sleep(1000);
        assertTrue("Agent not started", agent.isRunning());
        TupleSpace ts = new TupleSpace(TS_HOST, TS_PORT, AgentProtocol.COMMAND_SPACE_NAME);
        VMID vmid = new VMID();
        String queryId = vmid.toString();
        Tuple requestIdent = (AgentProtocol.getIdentifyTuple(agent.getId(), agent.getName(), vmid));
        requestIdent.setExpiration(3*1000);
        ts.write(requestIdent);
        Tuple tuple = ts.waitToTake(new Tuple(AgentProtocol.RESPONSE, queryId, agent.getId(), agent.getName(), AgentProtocol.MESSAGE_IDENTIFY, Field.createWildCardField()));
        assertTrue("No ident-tuple taken...", tuple != null);
        agentManager.stopAgent(agent.getId());
        System.out.println("AgentID(testIdentify) : " + agent.getId());
        Thread.sleep(1000);
        assertTrue("agent not stopped", !agent.isRunning());
    }

    @Test
    public void testAlive() throws AgentLifecycleException, InterruptedException, TupleSpaceException {
        IThreadedAgent agent = agentManager.startAgent(ThreadedAgentMock.NAME, null);
        Thread.sleep(1000);
        assertTrue("Agent not started", agent.isRunning());
        TupleSpace ts = new TupleSpace(TS_HOST, TS_PORT, AgentProtocol.COMMAND_SPACE_NAME);
        Tuple t = new Tuple(AgentProtocol.COMMAND_LINE, String.class, agent.getId(), agent.getName(), AgentProtocol.ALIVE);
        Tuple aliveTuple = ts.read(t);
        assertTrue("No alive-tuple taken...", aliveTuple != null);
        agentManager.stopAgent(agent.getId());
        System.out.println("AgentID(testAlive) : " + agent.getId());
        Thread.sleep(1000);
        assertTrue("agent not stopped", !agent.isRunning());
    }

}
