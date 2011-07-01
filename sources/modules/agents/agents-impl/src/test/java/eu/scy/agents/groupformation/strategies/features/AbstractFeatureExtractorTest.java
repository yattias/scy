package eu.scy.agents.groupformation.strategies.features;

import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;

import roolo.elo.api.IELO;
import roolo.elo.content.BasicContent;
import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.api.AgentLifecycleException;

public class AbstractFeatureExtractorTest extends AbstractTestFixture {


    public static void startTS() {
        startTupleSpaceServer();
    }

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

}