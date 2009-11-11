package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.ScenarioImpl;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.core.persistence.ScenarioDAO;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
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

    private TransactionTemplate transactionTemplate;


    public Object createScenario(final Scenario scenario) {
        return getHibernateTemplate().save(scenario);
    }


    public List<Scenario> getScenarios() {
        return getSession().createQuery("from ScenarioImpl order by name")
                .list();
    }

    public TransactionTemplate getTransactionTemplate() {
        return transactionTemplate;
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }
}
