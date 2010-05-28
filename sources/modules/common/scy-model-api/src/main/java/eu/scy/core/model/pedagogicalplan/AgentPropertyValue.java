package eu.scy.core.model.pedagogicalplan;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 28.mai.2010
 * Time: 13:25:18
 * To change this template use File | Settings | File Templates.
 */
public interface AgentPropertyValue extends BaseObject {

    AgentProperty getAgentProperty();

    void setAgentProperty(AgentProperty agentProperty);

    AgentPropertyValueLevel getAgentPropertyLevel();

    void setAgentPropertyLevel(AgentPropertyValueLevel agentPropertyLevel);
}
