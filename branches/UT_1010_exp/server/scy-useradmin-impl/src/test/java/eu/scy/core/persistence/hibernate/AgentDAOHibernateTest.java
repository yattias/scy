package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.AgentImpl;
import eu.scy.core.model.pedagogicalplan.Agent;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.persistence.AgentDAO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.jan.2010
 * Time: 14:10:33
 * To change this template use File | Settings | File Templates.
 */
public class AgentDAOHibernateTest extends AbstractDAOTest {

    private AgentDAO agentDAO;

    public AgentDAO getAgentDAO() {
        return agentDAO;
    }

    public void setAgentDAO(AgentDAO agentDAO) {
        this.agentDAO = agentDAO;
    }

    public void testSetup() {
        assertNotNull(getAgentDAO());
    }

    public void testSaveAgent() {
        Agent agent  = createAgent("007");
        getAgentDAO().save(agent);
        assertNotNull(((AgentImpl)agent).getId());
    }

    public void testFetchAgentByName() {
        String NAME_1 = "007";
        String NAME_2 = "Q";

        Agent a = createAgent(NAME_1);
        getAgentDAO().save(a);
        String id = ((AgentImpl)a).getId();

        Agent b = createAgent("NAME_2");
        getAgentDAO().save(b);

        Agent loaded = getAgentDAO().getAgentByName(NAME_1);
        assertTrue(((AgentImpl)loaded).getId().equals(id));
    }

    public void testAddAgentToLAS() {
        LearningActivitySpace las = createLAS("A LAS");
        Agent a = createAgent("An AGENT");

        getAgentDAO().addAgentToLearningActivitySpace(las, a);

        List connectedAgents = getAgentDAO().getConnectedAgents(las);
        if(!connectedAgents.contains(a)) fail("Did not find stored agent");

    }

}
