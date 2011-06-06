package eu.scy.core.model.pedagogicalplan;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 12:47:53
 *
 * The SCY framework makes use of a number of agents. The agent class represents one agent that is available for various objects within the pedagogical plan. 
 */
public interface Agent extends BaseObject {

    String getClassName();

    void setClassName(String className);

    List getAgentProperties();

    void setAgentProperties(List agentProperties);

    void addAgentProperty(AgentProperty agentProperty);
}
