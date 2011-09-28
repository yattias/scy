package eu.scy.agents.hypothesis;

import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.Mission;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.parameter.AgentParameter;
import eu.scy.agents.impl.ActionConstants;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.parameter.AgentParameterAPIImpl;
import eu.scy.agents.keywords.ExtractKeyphrasesAgent;
import eu.scy.agents.keywords.ExtractTopicModelKeywordsAgent;
import eu.scy.agents.keywords.KeywordConstants;
import eu.scy.agents.keywords.OntologyKeywordsAgent;
import eu.scy.agents.session.SessionAgent;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.content.BasicContent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URI;
import java.rmi.dgc.VMID;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HypothesisEvaluationTest extends AbstractTestFixture {

    private static final String ELO_TYPE = "scy/xproc";
    private static final String MISSION = "roolo://memory/0/0/Design+a+CO2-friendly+house.scymissionspecification";

    private IELO elo;

    // String[] expectedKeywords = new String[] { "ingredients", "nontoxic",
    // "binder", "solvent", "labels", "toxic", "chemical", "voc", "paint",
    // "health", "natural", "pigment" };

    private static final long TIME_IN_MILLIS = 666;

    private static final String UUID1234 = "uuid1234";

    private String eloPath;
    private AgentParameterAPIImpl agentParameterAPI;

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
        this.agentMap.put(ExtractTopicModelKeywordsAgent.NAME, params);
        this.agentMap.put(ExtractKeyphrasesAgent.NAME, params);
        this.agentMap.put(HypothesisEvaluationAgent2.NAME, params);
        this.agentMap.put(OntologyKeywordsAgent.NAME, params);
        this.agentMap.put(SessionAgent.NAME, params);
        this.startAgentFramework(this.agentMap);

        InputStream inStream = this.getClass().getResourceAsStream("/copexExampleElo.xml");
        String eloContent = this.readFile(inStream);
        this.elo = this.createNewElo("TestCopex", ELO_TYPE);
        this.elo.setContent(new BasicContent(eloContent));
        IMetadata metadata = this.repository.addNewELO(this.elo);
        URI eloUri = (URI) metadata.getMetadataValueContainer(
                this.typeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER)).getValue();
        this.eloPath = eloUri.toString();

        IMetadataKey keywordKey = this.typeManager.getMetadataKey(CoreRooloMetadataKeyIds.KEYWORDS.getId());
        keywordKey = this.typeManager.getMetadataKey(KeywordConstants.KEYWORD_SENTENCE_HISTOGRAM);
        agentParameterAPI = new AgentParameterAPIImpl(getCommandSpace());
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
    public void testRunOnEloSaved() throws TupleSpaceException, IOException, ClassNotFoundException {
        AgentParameter agentParameter = new AgentParameter(Mission.MISSION1.getName(), AgentProtocol.GLOBAL_SCAFFOLDING_LEVEL);
        agentParameter.setParameterValue(Integer.toString(AgentProtocol.SCAFFOLD_LEVEL_HIGH));
        agentParameterAPI.setParameter("global", agentParameter);
        this.login("testUser", "roolo://memory/0/0/Design+a+CO2-friendly+house.scymissionspecification",
                Mission.MISSION1.getName(), "en", "co2_2");

        Tuple tuple = new Tuple("action", UUID1234, TIME_IN_MILLIS, ActionConstants.ACTION_ELO_SAVED, "testUser",
                "SomeTool", MISSION1, "TestSession", this.eloPath, "elo_type=" + ELO_TYPE, "elo_uri=" + this.eloPath);
        this.getActionSpace().write(tuple);

        Tuple response = this.getCommandSpace().waitToTake(
                new Tuple(HypothesisEvaluationAgent.EVAL, String.class, String.class, String.class, String.class,
                        String.class, Field.createWildCardField()), AgentProtocol.MINUTE * 1);
        assertNotNull("no response received", response);
        ByteArrayInputStream bytesIn = new ByteArrayInputStream((byte[]) response.getField(6).getValue());
        ObjectInputStream objectIn = new ObjectInputStream(bytesIn);
        HashMap<Integer, Integer> histogram = (HashMap<Integer, Integer>) objectIn.readObject();
        String string = histogram.toString();
        assertEquals("{0=1, 2=3}", string);

    }

    @Test
    public void testRunOnEloFinished() throws TupleSpaceException, IOException, ClassNotFoundException {
        AgentParameter agentParameter = new AgentParameter(Mission.MISSION1.getName(), AgentProtocol.GLOBAL_SCAFFOLDING_LEVEL);
        agentParameter.setParameterValue(Integer.toString(AgentProtocol.SCAFFOLD_LEVEL_MEDIUM));
        agentParameterAPI.setParameter("global", agentParameter);

        this.login("testUser", "roolo://memory/0/0/Design+a+CO2-friendly+house.scymissionspecification",
                Mission.MISSION1.getName(), "en", "co2_2");

        Tuple tuple = new Tuple("action", UUID1234, TIME_IN_MILLIS, ActionConstants.ELO_FINISHED, "testUser",
                "SomeTool", MISSION1, "TestSession", this.eloPath);
        this.getActionSpace().write(tuple);

        Tuple response = this.getCommandSpace().waitToTake(
                new Tuple(HypothesisEvaluationAgent.EVAL, String.class, String.class, String.class, String.class,
                        String.class, Field.createWildCardField()), AgentProtocol.MINUTE * 1);
        assertNotNull("no response received", response);
        ByteArrayInputStream bytesIn = new ByteArrayInputStream((byte[]) response.getField(6).getValue());
        ObjectInputStream objectIn = new ObjectInputStream(bytesIn);
        HashMap<Integer, Integer> histogram = (HashMap<Integer, Integer>) objectIn.readObject();
        String string = histogram.toString();
        assertEquals("{0=1, 2=3}", string);

    }
}
