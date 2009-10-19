package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.ScenarioImpl;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.core.persistence.ScenarioDAO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.okt.2009
 * Time: 21:18:17
 * To change this template use File | Settings | File Templates.
 */
public class ScenarioDAOHibernate extends ScyBaseDAOHibernate implements ScenarioDAO {


    public void createScenario(Scenario scenario) {
        logger.info("BEFORE:" + getScenarios().size());
        super.save(scenario);
        logger.info("AFTER:" + getScenarios().size());
    }


    public List<Scenario> getScenarios() {
        return getSession().createQuery("from ScenarioImpl order by name")
                .list();
    }

    
}
