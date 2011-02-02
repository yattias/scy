package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.AgentProperty;
import eu.scy.core.model.pedagogicalplan.AgentPropertyValue;
import eu.scy.core.model.pedagogicalplan.AgentPropertyValueLevel;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 28.mai.2010
 * Time: 13:26:17
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="agentpropertyvalue")
public class AgentPropertyValueImpl extends BaseObjectImpl implements AgentPropertyValue {

    private AgentProperty agentProperty;
    private AgentPropertyValueLevel agentPropertyLevel;


    @Override
    @ManyToOne(targetEntity = AgentPropertyImpl.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "agentproperty_fk")
    public AgentProperty getAgentProperty() {
        return agentProperty;
    }

    @Override
    public void setAgentProperty(AgentProperty agentProperty) {
        this.agentProperty = agentProperty;
    }

    @Override
    @ManyToOne(targetEntity = AgentPropertyValueLevelImpl.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "agentpropertyvaluelevel_fk")
    public AgentPropertyValueLevel getAgentPropertyLevel() {
        return agentPropertyLevel;
    }

    @Override
    public void setAgentPropertyLevel(AgentPropertyValueLevel agentPropertyLevel) {
        this.agentPropertyLevel = agentPropertyLevel;
    }
}
