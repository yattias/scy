package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.*;
import eu.scy.core.model.pedagogicalplan.*;
import eu.scy.core.persistence.AgentDAO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.jan.2010
 * Time: 14:03:34
 * To change this template use File | Settings | File Templates.
 */
public class AgentDAOHibernate extends ScyBaseDAOHibernate implements AgentDAO{
    @Override
    public Agent getAgentByName(String name) {
        return (Agent) getSession().createQuery("From AgentImpl where name like :name")
                .setString("name", name)
                .setMaxResults(1)
                .uniqueResult();
    }

    @Override
    public void addAgentToLearningActivitySpace(LearningActivitySpace las, Agent a) {
        LearningActivitySpaceAgentConfiguration configuration = new LearningActivitySpaceAgentConfigurationImpl();
        configuration.setAgent(a);;
        configuration.setLearningActivitySpace(las);
        save(configuration);
    }

    @Override
    public List getConnectedAgents(LearningActivitySpace las) {
        return getSession().createQuery("select config.agent from LearningActivitySpaceAgentConfigurationImpl as config where config.learningActivitySpace = :las")
                .setEntity("las" , las)
                .list(); 
    }

    @Override
    public void registerAgent(String className) {
        Agent agent = (Agent) getSession().createQuery("from AgentImpl where className like :className")
                .setString("className", className)
                .setMaxResults(1)
                .uniqueResult();
        if(agent == null) {

            agent = new AgentImpl();
            agent.setName(className);
            agent.setClassName(className);
            save(agent);
        }
    }

    @Override
    public List getAgents() {
        return getSession().createQuery("from AgentImpl order by name")
                .list();
    }

    @Override
    public void addAgentProperty(Agent agent) {
        AgentProperty agentProperty = new AgentPropertyImpl();
        agent.addAgentProperty(agentProperty);


        List agentPropertyValueLevels = getAgentPropertyValueLevels();
        for (int i = 0; i < agentPropertyValueLevels.size(); i++) {
            AgentPropertyValueLevel agentPropertyValueLevel = (AgentPropertyValueLevel) agentPropertyValueLevels.get(i);
            AgentPropertyValue agentPropertyValue = new AgentPropertyValueImpl();
            agentProperty.addAgentPropertyValue(agentPropertyValue);
            agentPropertyValue.setAgentPropertyLevel(agentPropertyValueLevel);
            //save(agentPropertyValue);
        }

        save(agent);


    }

    @Override
    public void addAgentPropertyValueLevel() {
        AgentPropertyValueLevel agentPropertyValueLevel = new AgentPropertyValueLevelImpl();
        save(agentPropertyValueLevel);
    }

    @Override
    public List getAgentPropertyValueLevels() {
        return getSession().createQuery("from AgentPropertyValueLevelImpl order by levelIndex")
                .list();
    }

    @Override
    public void addAgentPropertyValue(AgentProperty agentProperty) {
        AgentPropertyValue agentPropertyValue = new AgentPropertyValueImpl();
        agentProperty.addAgentPropertyValue(agentPropertyValue);
    }

    @Override
    public AgentProperty getAgentProperty(String id) {
        return (AgentProperty) getSession().createQuery("from AgentPropertyImpl where id like :id")
                .setString("id", id)
                .uniqueResult();
    }

    @Override
    public void addPropertyValue(AgentProperty agentProperty) {
        AgentPropertyValue agentPropertyValue = new AgentPropertyValueImpl();
        agentProperty.addAgentPropertyValue(agentPropertyValue);
        save(agentPropertyValue);
        save(agentProperty);
    }

}

    
