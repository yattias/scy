package eu.scy.core.persistence;

import eu.scy.core.model.pedagogicalplan.Agent;
import eu.scy.core.model.pedagogicalplan.AgentProperty;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.jan.2010
 * Time: 14:02:22
 * To change this template use File | Settings | File Templates.
 */
public interface AgentDAO extends SCYBaseDAO {

    public Agent getAgentByName(String name);

    public void addAgentToLearningActivitySpace(LearningActivitySpace las, Agent a);

    public List getConnectedAgents(LearningActivitySpace las);

    void registerAgent(String className);

    public List getAgents();

    void addAgentProperty(Agent agent);

    void addAgentPropertyValueLevel();


    List getAgentPropertyValueLevels();

    void addAgentPropertyValue(AgentProperty agentProperty);

    AgentProperty getAgentProperty(String id);

    void addPropertyValue(AgentProperty agentProperty);
}
