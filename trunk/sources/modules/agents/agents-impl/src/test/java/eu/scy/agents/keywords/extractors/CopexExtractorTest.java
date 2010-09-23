/*
 * Created on 21.09.2010
 */
package eu.scy.agents.keywords.extractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
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
import de.fhg.iais.kd.tm.obwious.system.documentfrequency.DocumentFrequencyModel;
import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.keywords.ExtractKeywordsAgent;
import eu.scy.agents.keywords.ExtractTfIdfKeywordsAgent;
import eu.scy.agents.keywords.ExtractTopicModelKeywordsAgent;
import eu.scy.agents.keywords.workflow.KeywordWorkflowConstants;

public class CopexExtractorTest extends AbstractTestFixture {

  private IELO elo;

  private CopexExtractor copexExtractor;

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
    // agentMap.put(OntologyLookupAgent.class.getName(), params);
    this.startAgentFramework(this.agentMap);

    InputStream inStream = this.getClass().getResourceAsStream("/copexExampleElo.xml");
    String eloContent = readFile(inStream);
    elo = createNewElo("TestCopex", "scy/copex");
    elo.setContent(new BasicContent(eloContent));
    copexExtractor = new CopexExtractor();
    copexExtractor.setTupleSpace(getCommandSpace());
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

  private void initDfModel() throws ClassNotFoundException, IOException {
    InputStream inStream = this.getClass().getResourceAsStream("/models/df.out");
    ObjectInputStream in = new ObjectInputStream(inStream);
    DocumentFrequencyModel dfModel = (DocumentFrequencyModel) in.readObject();
    this.getPersistentStorage().put(KeywordWorkflowConstants.DOCUMENT_FREQUENCY_MODEL, dfModel);
  }

  @Test
  public void testGetKeywords() {
    List<String> keywords = copexExtractor.getKeywords(elo);
    assertEquals(12, keywords.size());
    assertTrue(hasItems(keywords, "ingredients", "nontoxic", "binder", "solvent", "labels",
                        "toxic", "chemical", "voc", "paint", "health", "natural", "pigment"));
  }

}
