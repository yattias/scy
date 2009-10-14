package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.core.persistence.ScenarioDAO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.okt.2009
 * Time: 21:18:17
 * To change this template use File | Settings | File Templates.
 */
public class ScenarioDAOHibernate extends ScyBaseDAOHibernate implements ScenarioDAO {

    public List<Scenario> getScenarios() {
        return getSession().createQuery("from ScenarioImpl order by name")
                .list();
    }
}
