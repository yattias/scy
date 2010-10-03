package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.Agent;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpaceAgentConfiguration;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.jan.2010
 * Time: 14:34:02
 * To change this template use File | Settings | File Templates.
 */
@Entity
@DiscriminatorValue(value= "agentconfiguration")
public class LearningActivitySpaceAgentConfigurationImpl extends LASConfiguration  implements LearningActivitySpaceAgentConfiguration {

    private Agent agent;

    @Override
    @ManyToOne(targetEntity = AgentImpl.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "agent_primKey")
    public Agent getAgent() {
        return agent;
    }

    @Override
    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
