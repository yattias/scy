package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.ScenarioImpl;
import eu.scy.core.persistence.ScenarioDAO;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.okt.2009
 * Time: 21:24:04
 * To change this template use File | Settings | File Templates.
 */
public class ScenarioDAOHibernateTest extends AbstractTransactionalSpringContextTests {

    private ScenarioDAO scenarioDAO;

    protected String[] getConfigLocations() {
        return new String[]{"classpath:/eu/scy/core/persistence/hibernate/applciationContext-hibernate-OnlyForTesting.xml"};
    }


    public ScenarioDAO getScenarioDAO() {
        return scenarioDAO;
    }

    public void setScenarioDAO(ScenarioDAO scenarioDAO) {
        this.scenarioDAO = scenarioDAO;
    }

    @Test
    public void testBeanDefined() {
        assert(getScenarioDAO() != null);
    }

    @Test
    public void testSaveScenario() {
        ScenarioImpl scenario = new ScenarioImpl();
        scenario.setName("first scenario");
        assertNull(scenario.getId());
        ScenarioImpl newScenario = (ScenarioImpl) getScenarioDAO().save(scenario);
        assertNotNull(scenario.getId());
    }

}
