/*
 * Created on 21.09.2010
 */
package eu.scy.agents.hypothesis.workflow;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import roolo.elo.api.IELO;
import roolo.elo.content.BasicContent;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.Operator;
import de.fhg.iais.kd.tm.obwious.type.Container;
import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.keywords.KeywordConstants;
import eu.scy.agents.util.Utilities;

public class ComputeKeywordsInSentenceHistogramTest extends AbstractTestFixture {

  public static final String HISTOGRAM = "histogram";

  private static final Logger logger = Logger.getLogger(ComputeKeywordsInSentenceHistogramTest.class.getName());

  private IELO elo;

  private String[] keywords = { "ingredients", "nontoxic", "binder", "solvent", "labels", "toxic",
                               "chemical", "voc", "paint", "health", "natural", "pigment" };

  private ArrayList<String> keywordList;

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

    InputStream inStream = this.getClass().getResourceAsStream("/copexExampleElo.xml");
    String eloContent = readFile(inStream);
    elo = createNewElo("TestCopex", "scy/copex");
    elo.setContent(new BasicContent(eloContent));
    keywordList = new ArrayList<String>();
    for (int i = 0; i < keywords.length; i++) {
      keywordList.add(keywords[i]);
    }
  }

  @Override
  @After
  public void tearDown() {
    try {
      super.tearDown();
    } catch (AgentLifecycleException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testComputeKeywordsInSentenceHistogramTest() {
    String XMLPATH = "//learner_proc/proc_hypothesis/hypothesis";

    String text = Utilities.getEloText(elo, XMLPATH, logger);
    Document doc = Utilities.convertTextToDocument(text);
    doc.setFeature(Features.WORDS, keywordList);

    Operator cmpHistogramOp = new EvalHypothesisWorkflow().getOperator("Main");
    cmpHistogramOp.setInputParameter(ObjectIdentifiers.DOCUMENT, doc);
    Container result = cmpHistogramOp.run();
    Document docResult = (Document) result.get(ObjectIdentifiers.DOCUMENT);
    HashMap<Integer, Integer> hist = docResult.getFeature(KeywordConstants.KEYWORD_SENTENCE_HISTOGRAM);
    String string = hist.toString();
    assertEquals(string, "{0=16, 1=14, 2=7, 3=5, 4=1, 5=1}");
  }

}
