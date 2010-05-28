package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.Agent;
import eu.scy.core.model.pedagogicalplan.AgentProperty;
import eu.scy.core.model.pedagogicalplan.AgentPropertyValue;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 28.mai.2010
 * Time: 12:46:32
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="agentproperty")
public class AgentPropertyImpl extends BaseObjectImpl implements AgentProperty {

    private Agent agent;
    private List agentPropertyValues = new LinkedList();


    @Override
    @ManyToOne(targetEntity = AgentImpl.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "agent_fk")
    public Agent getAgent() {
        return agent;
    }

    @Override
    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    @Override
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "agentProperty", targetEntity = AgentPropertyValueImpl.class, fetch = FetchType.LAZY)
    public List getAgentPropertyValues() {
        return agentPropertyValues;
    }

    @Override
    public void setAgentPropertyValues(List agentPropertyValues) {
        this.agentPropertyValues = agentPropertyValues;
    }

    @Override
    public void addAgentPropertyValue(AgentPropertyValue agentPropertyValue) {
        getAgentPropertyValues().add(agentPropertyValue);
        agentPropertyValue.setAgentProperty(this);
    }

}
