package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.Agent;
import eu.scy.core.model.pedagogicalplan.AgentProperty;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.jan.2010
 * Time: 14:02:02
 * To change this template use File | Settings | File Templates.
 */
public interface AgentService extends BaseService {

    public Agent getAgentByName(String name);

    public void addAgentToLearningActivitySpace(LearningActivitySpace las, Agent a);

    public List getConnectedAgents(LearningActivitySpace las);


    void registerAgent(String className);

    public  List getAgents();

    public void addAgentProperty(Agent agent);

    public void addAgentPropertyValue(AgentProperty agentProperty);

    Agent getAgent(String id);

    public void addAgentPropertyValueLevel();

    List getAgentPropertyValueLevels();

    AgentProperty getAgentProperty(String id);

    void addPropertyValue(AgentProperty agentProperty);
}
