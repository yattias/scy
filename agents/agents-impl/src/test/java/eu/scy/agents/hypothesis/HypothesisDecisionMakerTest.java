package eu.scy.agents.hypothesis;

import static org.junit.Assert.assertNotNull;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.rmi.dgc.VMID;
import java.util.HashMap;

import org.apache.axis.utils.ByteArrayOutputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.content.BasicContent;
import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;

public class HypothesisDecisionMakerTest extends AbstractTestFixture {

  private static final String ELO_TYPE = "scy/copex";

  private IELO elo;

  private static final long TIME_IN_MILLIS = 666;

  private static final String UUID1234 = "uuid1234";

  private String eloPath;

  private HashMap<Integer, Integer> histogram;

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

    this.initTopicModel();
    this.initDfModel();

    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put(AgentProtocol.PARAM_AGENT_ID, new VMID());
    params.put(AgentProtocol.TS_HOST, TSHOST);
    params.put(AgentProtocol.TS_PORT, TSPORT);
    this.agentMap.put(HypothesisDecisionMakerAgent.NAME, params);

    this.startAgentFramework(this.agentMap);

    InputStream inStream = this.getClass().getResourceAsStream("/copexExampleElo.xml");
    String eloContent = readFile(inStream);
    elo = createNewElo("TestCopex", ELO_TYPE);
    elo.setContent(new BasicContent(eloContent));
    IMetadata metadata = repository.addNewELO(elo);
    URI eloUri = (URI) metadata.getMetadataValueContainer(this.typeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER)).getValue();
    this.eloPath = eloUri.toString();

    System.out.println(eloUri.toString());

    histogram = new HashMap<Integer, Integer>();
    histogram.put(0, 16);
    histogram.put(1, 14);
    histogram.put(2, 7);
    histogram.put(3, 5);
    histogram.put(4, 1);
    histogram.put(5, 1);
  }

  @Override
  @After
  public void tearDown() {
    try {
      removeTopicModel();
      removeDFModel();
      this.stopAgentFrameWork();
      super.tearDown();
    } catch (AgentLifecycleException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testRun() throws InterruptedException, TupleSpaceException, IOException {

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    ObjectOutputStream objectOut = new ObjectOutputStream(bytes);
    objectOut.writeObject(histogram);
    objectOut.close();

    Tuple tuple = new Tuple(HypothesisEvaluationAgent.EVAL, "testUser", "SomeMission",
                            "TestSession", "copex", eloPath, bytes.toByteArray());
    getCommandSpace().write(tuple);
    Tuple response = this.getCommandSpace().waitToTake(new Tuple(
                                                                          AgentProtocol.NOTIFICATION,
                                                                          String.class,
                                                                          String.class,
                                                                          "copex",
                                                                          String.class,
                                                                          String.class,
                                                                          String.class,
                                                                          Field.createWildCardField()),
                                                                AgentProtocol.ALIVE_INTERVAL);
    assertNotNull("no response received", response);
    String message = (String) response.getField(7).getValue();
    assertNotNull(message, "message = ok");
  }
}
