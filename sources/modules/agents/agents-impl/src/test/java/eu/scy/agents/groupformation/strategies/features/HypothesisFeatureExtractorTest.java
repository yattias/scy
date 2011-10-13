package eu.scy.agents.groupformation.strategies.features;

import eu.scy.agents.Mission;
import eu.scy.agents.api.parameter.AgentParameter;
import eu.scy.agents.hypothesis.HypothesisEvaluationAgent2;
import eu.scy.agents.impl.ActionConstants;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.AgentRooloServiceImpl;
import eu.scy.agents.impl.parameter.AgentParameterAPIImpl;
import eu.scy.agents.keywords.ExtractKeyphrasesAgent;
import eu.scy.agents.keywords.ExtractTopicModelKeywordsAgent;
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
import roolo.elo.api.IMetadata;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.content.BasicContent;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.rmi.dgc.VMID;
import java.util.HashMap;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HypothesisFeatureExtractorTest extends AbstractFeatureExtractorTest {

    private static final String ELO_TYPE = "scy/xproc";

    private static final String MISSION = "roolo://memory/0/0/Design+a+CO2-friendly+house.scymissionspecification";

    private static final long TIME_IN_MILLIS = 666;

    private static final String UUID1234 = "uuid1234";

    private String eloPath;

    private URI eloUri;
    private AgentParameterAPIImpl agentParameterAPI;

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
        this.extractor = new HypothesisFeatureExtractor();
        AgentRooloServiceImpl agentRooloService = new AgentRooloServiceImpl(repository);
        agentRooloService.setMetadataTypeManager(typeManager);
        this.extractor.setRepository(agentRooloService); 
        
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
        eloUri = (URI) metadata.getMetadataValueContainer(this.typeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER)).getValue();
        this.eloPath = eloUri.toString();
        agentParameterAPI = new AgentParameterAPIImpl(getCommandSpace());

    }

    @Override
    @After
    public void tearDown() {
//        try {
//            this.stopAgentFrameWork();
            super.tearDown();
//        } catch (AgentLifecycleException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void testRun() throws TupleSpaceException, IOException, ClassNotFoundException, InterruptedException {
        AgentParameter agentParameter = new AgentParameter(Mission.MISSION1.getName(),
                AgentProtocol.GLOBAL_SCAFFOLDING_LEVEL);
        agentParameter.setParameterValue(Integer.toString(AgentProtocol.SCAFFOLD_LEVEL_HIGH));
        agentParameterAPI.setParameter("global", agentParameter);

        this.login("testUser",
                   "roolo://memory/0/0/Design+a+CO2-friendly+house.scymissionspecification",
                   Mission.MISSION1.getName(), "en", "co2_2");

        Thread.sleep(100);

        Tuple tuple = new Tuple("action", UUID1234, TIME_IN_MILLIS,
                                ActionConstants.ACTION_ELO_SAVED, "testUser", "SomeTool", MISSION1,
                                "TestSession", this.eloPath, "elo_type=" + ELO_TYPE, "elo_uri="
                                                                                     + this.eloPath);
        this.getActionSpace().write(tuple);

        Tuple response = this.getCommandSpace().waitToTake(new Tuple(
                                                                     HypothesisEvaluationAgent2.EVAL,
                                                                     String.class, String.class,
                                                                     String.class, String.class,
                                                                     String.class,
                                                                     Field.createWildCardField()),
                                                           AgentProtocol.MINUTE * 1);
        assertNotNull("no response received", response);

        this.elo = repository.retrieveELO(eloUri);

        double[] features = ((HypothesisFeatureExtractor) this.extractor).getFeatures("x", "y",
                                                                                      null,
                                                                                      this.elo);
        assertEquals(4, features.length);
        assertArrayEquals(new double[] { 1.0, 0.0, 3.0, 0.0 }, features, 0.000001);

    }
}
