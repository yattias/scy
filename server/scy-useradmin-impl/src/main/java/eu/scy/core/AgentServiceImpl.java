package eu.scy.core;

import eu.scy.core.model.impl.pedagogicalplan.AgentImpl;
import eu.scy.core.model.pedagogicalplan.Agent;
import eu.scy.core.model.pedagogicalplan.AgentProperty;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.persistence.AgentDAO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.jan.2010
 * Time: 14:02:48
 * To change this template use File | Settings | File Templates.
 */
public class AgentServiceImpl extends BaseServiceImpl implements AgentService {

    
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

    @Override
    @Transactional
    public void registerAgent(String className) {
        getAgentDAO().registerAgent(className);
    }

    @Override
    public List getAgents() {
        return getAgentDAO().getAgents();
    }

    @Override
    @Transactional
    public void addAgentProperty(Agent agent) {
        getAgentDAO().addAgentProperty(agent);
    }

    @Override
    @Transactional
    public void addAgentPropertyValue(AgentProperty agentProperty) {
        getAgentDAO().addAgentPropertyValue(agentProperty);
    }

    @Override
    public Agent getAgent(String id) {
        return (Agent) getAgentDAO().getObject(AgentImpl.class, id);
    }

    @Override
    @Transactional
    public void addAgentPropertyValueLevel() {
        getAgentDAO().addAgentPropertyValueLevel();
    }

    @Override
    public List getAgentPropertyValueLevels() {
        return getAgentDAO().getAgentPropertyValueLevels();
    }

    @Override
    public AgentProperty getAgentProperty(String id) {
        return getAgentDAO().getAgentProperty(id);
    }

    @Override
    @Transactional
    public void addPropertyValue(AgentProperty agentProperty) {
        getAgentDAO().addPropertyValue(agentProperty);
    }

}
