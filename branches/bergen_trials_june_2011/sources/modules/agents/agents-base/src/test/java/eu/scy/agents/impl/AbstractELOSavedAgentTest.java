package eu.scy.agents.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.Configuration.Database;
import info.collide.sqlspaces.server.Server;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.scy.agents.api.AgentLifecycleException;

public class AbstractELOSavedAgentTest extends AbstractELOSavedAgent {

  private static final String ELO_TYPE = "scysim/simconfig";

  private static final long TIME_IN_MILLIS = 666;

  private static final String UUID1234 = "uuid1234";

  private static final int TSPORT = 2525;

  // private static final String TSHOST = "scy.collide.info";
  private static final String TSHOST = "localhost";

  private static final String ELO_URI = "roolo://memory/1/testElo.scysimconfig";

  private AbstractELOSavedAgent agent;

  private boolean processActionCalled;

  private boolean errorOccurred;

  public AbstractELOSavedAgentTest() {
    super(AbstractELOSavedAgentTest.class.getName(), "id1", TSHOST, TSPORT);
  }

  // @Override
  // protected void processELOSavedAction(IAction action) {
  // processActionCalled = true;
  // try {
  // assertEquals("SomeMission", action.getContext(ContextConstants.mission));
  // assertEquals("TestSession", action.getContext(ContextConstants.session));
  // assertEquals("SomeTool", action.getContext(ContextConstants.tool));
  // assertEquals(UUID1234, action.getId());
  // assertEquals("testUser", action.getUser());
  // assertEquals(TIME_IN_MILLIS, action.getTimeInMillis());
  // assertEquals("elo_saved", action.getType());
  //
  // assertEquals(ELO_URI, action.getAttribute("elouri"));
  // assertEquals(ELO_TYPE, action.getAttribute("type"));
  // } catch (AssertionError e) {
  // e.printStackTrace();
  // errorOccurred = true;
  // }
  // }

  @BeforeClass
  public static void startTS() {
    if (!Server.isRunning()) {
      Configuration conf = Configuration.getConfiguration();
      conf.setNonSSLPort(TSPORT);
      conf.setSSLEnabled(false);
      conf.setDbType(Database.HSQL);
      conf.setWebEnabled(false);
      // conf.setWebServicesEnabled(false);
      conf.setRemoteAdminEnabled(false);
      conf.setLocal(false);
      Server.startServer();
    }
  }

  @AfterClass
  public static void stopTS() {
    Server.stopServer();
  }

  @Before
  public void setUp() throws AgentLifecycleException {
    processActionCalled = false;
    errorOccurred = false;

    agent = this;
    agent.start();
  }

  @Test
  public void test() throws TupleSpaceException, InterruptedException {
    Tuple tuple = new Tuple("action", UUID1234, TIME_IN_MILLIS, ActionConstants.ACTION_ELO_SAVED,
                            "testUser", "SomeTool", "SomeMission", "TestSession", ELO_URI,
                            "type=" + ELO_TYPE);
    getActionSpace().write(tuple);
    Thread.sleep(1500);
    assertTrue("Action not processed", processActionCalled);
    assertFalse("An error has occured", errorOccurred);
  }

  @After
  public void tearDown() throws AgentLifecycleException {
    agent.stop();
  }

  @Test
  public void testWrongTuple() throws TupleSpaceException, InterruptedException {
    Tuple tuple = new Tuple("action_nothing", UUID1234, TIME_IN_MILLIS, "elo_saved", "testUser",
                            "SomeTool", "SomeMission", "TestSession",
                            "roolo://memory/1/testElo.scysimconfig", "type=scysim/simconfig");
    tuple.setExpiration(10000);
    getActionSpace().write(tuple);
    Thread.sleep(500);
    assertFalse("Action not processed", processActionCalled);
    assertFalse("An error has occured", errorOccurred);
  }

  @Override
  public void processELOSavedAction(String actionId, String user, long timeInMillis, String tool,
                                    String mission, String session, String eloUri, String eloType) {
    processActionCalled = true;
    try {
      assertEquals("SomeMission", mission);
      assertEquals("TestSession", session);
      assertEquals("SomeTool", tool);
      assertEquals(UUID1234, actionId);
      assertEquals("testUser", user);
      assertEquals(TIME_IN_MILLIS, timeInMillis);

      assertEquals(ELO_URI, eloUri);
      assertEquals(ELO_TYPE, eloType);
    } catch (AssertionError e) {
      e.printStackTrace();
      errorOccurred = true;
    }

  }

  @Override
  protected Tuple getListParameterTuple(String queryId) {
    List<String> emptyList = Collections.emptyList();
    return AgentProtocol.getListParametersTupleResponse(getName(), queryId, emptyList);
  }
}
