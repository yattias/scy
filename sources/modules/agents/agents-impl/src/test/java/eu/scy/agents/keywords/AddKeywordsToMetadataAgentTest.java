package eu.scy.agents.keywords;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.rmi.dgc.VMID;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.content.BasicContent;
import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;

public class AddKeywordsToMetadataAgentTest extends AbstractTestFixture {

  private static final String WEBRESOURCE_ELO_TYPE = "scy/webresourcer";

  private static final String COPEX_ELO_TYPE = "scy/copex";

  private IELO copexElo, webResourceElo;

  private String copexEloPath, webResourceEloPath;

  private URI copexEloUri, webResourceEloUri;

  private static final long TIME_IN_MILLIS = 666;

  private static final String UUID1234 = "uuid1234";

  private AddKeywordsToMetadataAgent addMetadataAgent;

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
    this.agentMap.put(ExtractKeywordsAgent.NAME, params);
    this.agentMap.put(ExtractTfIdfKeywordsAgent.NAME, params);
    this.agentMap.put(ExtractTopicModelKeywordsAgent.NAME, params);
    this.agentMap.put(ExtractKeyphrasesAgent.NAME, params);
    this.agentMap.put(OntologyKeywordsAgent.NAME, params);

    this.startAgentFramework(this.agentMap);

    InputStream inStream = this.getClass().getResourceAsStream("/copexExampleElo.xml");
    String eloContent = readFile(inStream);
    inStream.close();
    copexElo = createNewElo("TestCopex", COPEX_ELO_TYPE);
    copexElo.setContent(new BasicContent(eloContent));
    IMetadata metadata = repository.addNewELO(copexElo);
    copexEloUri = (URI) metadata.getMetadataValueContainer(this.typeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER)).getValue();
    this.copexEloPath = copexEloUri.toString();

    inStream = this.getClass().getResourceAsStream("/scyLighterExample.xml");
    eloContent = readFile(inStream);
    inStream.close();
    webResourceElo = createNewElo("TestWebResource", WEBRESOURCE_ELO_TYPE);
    webResourceElo.setContent(new BasicContent(eloContent));
    metadata = repository.addNewELO(webResourceElo);
    webResourceEloUri = (URI) metadata.getMetadataValueContainer(this.typeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER)).getValue();
    this.webResourceEloPath = webResourceEloUri.toString();

    addMetadataAgent = new AddKeywordsToMetadataAgent(params);
    addMetadataAgent.setRepository(repository);
    addMetadataAgent.setMetadataTypeManager(typeManager);

    System.out.println(copexEloUri.toString());
    System.out.println(webResourceEloUri.toString());
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

  @SuppressWarnings("unchecked")
  @Test
  public void testRun() throws InterruptedException, TupleSpaceException, IOException {

    addMetadataAgent.processELOSavedAction(AgentProtocol.ACTION_ELO_SAVED, UUID1234,
                                           TIME_IN_MILLIS, "copex", "SomeMission", "TestSession",
                                           copexEloPath, COPEX_ELO_TYPE);

    IELO retrievedELO = this.repository.retrieveELOLastVersion(copexEloUri);
    IMetadata metadata = retrievedELO.getMetadata();
    IMetadataKey keywordKey = typeManager.getMetadataKey(eu.scy.agents.keywords.KeywordConstants.AGENT_KEYWORDS);
    IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(keywordKey);
    List<String> keywords = (List<String>) metadataValueContainer.getValueList();
    assertEquals(26, keywords.size());
    assertTrue(hasItems(keywords, "binder", "solvent", "voc", "natural paints", "modern paints",
                        "ingredients", "nontoxic", "Natural paints", "Environmentally Friendly",
                        "toxic", "chemical", "conventional paint companies", "conventional paint",
                        "non-toxic paints", "VOC content", "used as solvent", "labels", "paint",
                        "pre-set operation", "natural paint", "health", "chemical paints",
                        "paint companies", "still contain", "natural", "pigment"));

    addMetadataAgent.processELOSavedAction(AgentProtocol.ACTION_ELO_SAVED, UUID1234,
                                           TIME_IN_MILLIS, "webresourcer", "SomeMission",
                                           "TestSession", webResourceEloPath, WEBRESOURCE_ELO_TYPE);
    retrievedELO = this.repository.retrieveELOLastVersion(webResourceEloUri);
    metadata = retrievedELO.getMetadata();
    keywordKey = typeManager.getMetadataKey(eu.scy.agents.keywords.KeywordConstants.AGENT_KEYWORDS);
    metadataValueContainer = metadata.getMetadataValueContainer(keywordKey);
    keywords = (List<String>) metadataValueContainer.getValueList();
    assertEquals(16, keywords.size());
    assertTrue(hasItems(keywords, "ecological", "development", "expressed", "carbon footprint",
                        "measured", "strategy", "ecological footprint", "footprint", "private",
                        "organization", "sneaked", "carbon", "capture", "undertaking", "known",
                        "assessment"));

  }
}
