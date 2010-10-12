package eu.scy.core.model.pedagogicalplan;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 28.mai.2010
 * Time: 12:46:53
 * To change this template use File | Settings | File Templates.
 */
public interface AgentProperty extends BaseObject{


    Agent getAgent();

    void setAgent(Agent agent);

    List getAgentPropertyValues();

    void setAgentPropertyValues(List agentPropertyValues);

    void addAgentPropertyValue(AgentPropertyValue agentPropertyValue);
}