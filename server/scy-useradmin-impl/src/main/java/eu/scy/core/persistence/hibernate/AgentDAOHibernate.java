package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceAgentConfigurationImpl;
import eu.scy.core.model.pedagogicalplan.Agent;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpaceAgentConfiguration;
import eu.scy.core.persistence.AgentDAO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.jan.2010
 * Time: 14:03:34
 * To change this template use File | Settings | File Templates.
 */
public class AgentDAOHibernate extends ScyBaseDAOHibernate implements AgentDAO {
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
}
