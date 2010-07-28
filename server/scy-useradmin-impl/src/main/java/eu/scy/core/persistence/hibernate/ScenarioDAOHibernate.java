package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.pedagogicalplan.*;
import eu.scy.core.persistence.ScenarioDAO;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.okt.2009
 * Time: 21:18:17
 * To change this template use File | Settings | File Templates.
 */
public class ScenarioDAOHibernate extends ScyBaseDAOHibernate implements ScenarioDAO {




    public Object createScenario(final Scenario scenario) {
        return getHibernateTemplate().save(scenario);
    }


    public List<Scenario> getScenarios() {
        return getSession().createQuery("from ScenarioImpl order by name")
                .list();
    }

    @Override
    public List<Activity> getAllActivitiesForLAS(LearningActivitySpace las) {
        return getSession().createQuery ("from ActivityImpl where learningActivitySpace = :las")
                .setEntity("las", las)
                .list();
    }

    @Override
    public List<Mission> getMissions() {
        return getSession().createQuery("from MissionImpl order by name")
                .list();
    }

    @Override
    public Scenario getScenario(String scenarioId) {
        return (Scenario) getSession().createQuery("from ScenarioImpl where id=:id")
                .setString("id", scenarioId)
                .uniqueResult();
    }

    @Override
    public List<LearningActivitySpace> getLearningActivitySpaces(Scenario scenario) {
        return getSession().createQuery("from LearningActivitySpaceImpl as las where las.participatesIn = :scenario")
                .setEntity("scenario", scenario)
                .list();

    }


}
