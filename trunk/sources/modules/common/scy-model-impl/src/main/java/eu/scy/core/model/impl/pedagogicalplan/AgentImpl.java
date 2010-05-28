package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.Agent;
import eu.scy.core.model.pedagogicalplan.AgentProperty;

import javax.persistence.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.jan.2010
 * Time: 13:55:48
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="agent")
public class AgentImpl extends BaseObjectImpl implements Agent {

    private String className;
    private List agentProperties;

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "agent", targetEntity = AgentPropertyImpl.class, fetch = FetchType.LAZY)
    public List getAgentProperties() {
        return agentProperties;
    }

    @Override
    public void setAgentProperties(List agentProperties) {
        this.agentProperties = agentProperties;
    }

    @Override
    public void addAgentProperty(AgentProperty agentProperty) {
        agentProperties.add(agentProperty);
        agentProperty.setAgent(this);
    }
}
