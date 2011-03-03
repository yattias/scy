/*
 * Created on 21.09.2010
 */
package eu.scy.agents.keywords.extractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.rmi.dgc.VMID;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import roolo.elo.api.IELO;
import roolo.elo.content.BasicContent;
import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.keywords.ExtractKeyphrasesAgent;
import eu.scy.agents.keywords.ExtractKeywordsAgent;
import eu.scy.agents.keywords.ExtractTfIdfKeywordsAgent;
import eu.scy.agents.keywords.ExtractTopicModelKeywordsAgent;
import eu.scy.agents.keywords.workflow.KeywordWorkflowConstants;

public class WebresourceExtractorTest extends AbstractTestFixture {

  private IELO elo;

  private KeywordExtractor webresourceExtractor;

  @BeforeClass
  public static void startTS() {
    startTupleSpaceServer();
  }

  @AfterClass
  public static void stopTS() {
    stopTupleSpaceServer();
  }

  @Before
  public void setup() throws Exception {
    // super.setUp();

    this.initTopicModel();
    this.initDfModel();

    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put(AgentProtocol.PARAM_AGENT_ID, new VMID());
    params.put(AgentProtocol.TS_HOST, TSHOST);
    params.put(AgentProtocol.TS_PORT, TSPORT);
    this.agentMap.put(ExtractKeywordsAgent.NAME, params);
    this.agentMap.put(ExtractTfIdfKeywordsAgent.NAME, params);
    this.agentMap.put(ExtractKeyphrasesAgent.NAME, params);
    this.agentMap.put(ExtractTopicModelKeywordsAgent.NAME, params);
    // agentMap.put(OntologyLookupAgent.class.getName(), params);
    this.startAgentFramework(this.agentMap);

    InputStream inStream = this.getClass().getResourceAsStream("/scyLighterExample.xml");
    String eloContent = readFile(inStream);
    inStream.close();
    elo = createNewElo("TestWebresource", "scy/webresource");
    elo.setContent(new BasicContent(eloContent));
    webresourceExtractor = new WebresourceExtractor();
    webresourceExtractor.setMission("co2");
    webresourceExtractor.setTupleSpace(getCommandSpace());
  }

  @Override
  @After
  public void tearDown() {
    try {
      if (this.getPersistentStorage() != null) {
        this.getPersistentStorage().remove(KeywordWorkflowConstants.DOCUMENT_FREQUENCY_MODEL);
        this.removeTopicModel();
      } else {
        System.out.println("break");
      }
      this.stopAgentFrameWork();
      super.tearDown();
    } catch (AgentLifecycleException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetKeywords() {
    List<String> keywords = webresourceExtractor.getKeywords(elo);
    assertEquals(15, keywords.size());
    assertTrue(hasItems(keywords, "ecological", "expressed", "carbon footprint", "strategy",
                        "ecological footprint", "measured", "footprint", "private", "organization",
                        "sneaked", "carbon", "capture", "undertaking", "known", "assessment"));
  }

}
