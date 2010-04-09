package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.Agent;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.persistence.AgentDAO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.jan.2010
 * Time: 14:02:48
 * To change this template use File | Settings | File Templates.
 */
public class AgentServiceImpl extends BaseServiceImpl implements AgentService{

    private AgentDAO agentDAO;

    public AgentDAO getAgentDAO() {
        return (AgentDAO) getScyBaseDAO();
    }

    @Override
    public Agent getAgentByName(String name) {
        return getAgentDAO().getAgentByName(name);
    }

    @Override
    public void addAgentToLearningActivitySpace(LearningActivitySpace las, Agent a) {
        getAgentDAO().addAgentToLearningActivitySpace(las, a);
    }

    @Override
    public List getConnectedAgents(LearningActivitySpace las) {
        return getAgentDAO().getConnectedAgents(las);
    }
}
