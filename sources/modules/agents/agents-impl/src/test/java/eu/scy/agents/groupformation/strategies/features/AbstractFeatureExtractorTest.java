package eu.scy.agents.groupformation.strategies.features;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.dgc.VMID;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import roolo.elo.api.IELO;
import roolo.elo.content.BasicContent;

import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.keywords.ExtractKeywordsAgent;
import eu.scy.agents.keywords.ExtractTfIdfKeywordsAgent;
import eu.scy.agents.keywords.ExtractTopicModelKeywordsAgent;
import eu.scy.agents.roolo.rooloaccessor.RooloAccessorAgent;

public class AbstractFeatureExtractorTest extends AbstractTestFixture {

    @BeforeClass
    public static void startTS() {
        startTupleSpaceServer();
    }

    @AfterClass
    public static void stopTS() {
        stopTupleSpaceServer();
    }

    protected IELO elo, referenceElo;

    protected FeatureExtractor extractor;

    public AbstractFeatureExtractorTest() {
        super();
    }

    public AbstractFeatureExtractorTest(String host, int port) {
        super(host, port);
    }

    @Before
    public void startAgents() throws Exception, ClassNotFoundException, IOException {
        this.setUp();

        // HashMap<String, Object> params = new HashMap<String, Object>();
        // params.put(AgentProtocol.TS_HOST, TSHOST);
        // params.put(AgentProtocol.TS_PORT, TSPORT);
        // params.put(AgentProtocol.PARAM_AGENT_ID, new VMID());
        // this.agentMap.put(ExtractKeywordsAgent.NAME, params);
        // params.put(AgentProtocol.PARAM_AGENT_ID, new VMID());
        // this.agentMap.put(ExtractTfIdfKeywordsAgent.NAME, params);
        // params.put(AgentProtocol.PARAM_AGENT_ID, new VMID());
        // this.agentMap.put(ExtractTopicModelKeywordsAgent.NAME, params);
        // params.put(AgentProtocol.PARAM_AGENT_ID, new VMID());
        // agentMap.put(RooloAccessorAgent.class.getName(), params);
        // // agentMap.put(OntologyLookupAgent.class.getName(), params);
        // this.startAgentFramework(this.agentMap);
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

    protected IELO loadElo(String eloContentFile, String eloType, String eloTitle)
            throws IOException {
        IELO elo;
        InputStream inStream = this.getClass().getResourceAsStream(eloContentFile);
        String eloContent = readFile(inStream);
        inStream.close();
        elo = createNewElo(eloTitle, eloType);
        elo.setContent(new BasicContent(eloContent));
        return elo;
    }

}