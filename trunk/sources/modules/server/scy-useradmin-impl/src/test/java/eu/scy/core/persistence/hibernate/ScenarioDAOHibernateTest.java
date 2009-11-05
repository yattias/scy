package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.ActivityImpl;
import eu.scy.core.model.impl.pedagogicalplan.AnchorELOImpl;
import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceImpl;
import eu.scy.core.model.impl.pedagogicalplan.ScenarioImpl;
import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.core.persistence.ScenarioDAO;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.testng.annotations.Test;

import java.util.List;

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
        assert (getScenarioDAO() != null);
    }

    @Test
    public void testSaveScenario() {
        int scenarioCount = getScenarioDAO().getScenarios().size();
        ScenarioImpl scenario = new ScenarioImpl();
        scenario.setName("first scenario");
        assertNull(scenario.getId());
        ScenarioImpl newScenario = (ScenarioImpl) getScenarioDAO().save(scenario);
        assertNotNull(scenario.getId());
        int newScenarioCount = getScenarioDAO().getScenarios().size();
        assertEquals(scenarioCount, newScenarioCount - 1);
    }

    @Test
    public void testSaveScenarioWithLAS() {
        Scenario scenario = createPedagogicalPlan();
        LearningActivitySpace las = scenario.getLearningActivitySpace();

        getScenarioDAO().save(scenario);
        assertNotNull(((ScenarioImpl) scenario).getId());
        assertNotNull(((LearningActivitySpaceImpl) las).getId());

    }

    @Test
    public void testSaveScenarioWithLASAndActivity() {
        Scenario scenario = createPedagogicalPlan();
        LearningActivitySpace las = scenario.getLearningActivitySpace();

        getScenarioDAO().save(scenario);
        assertNotNull(((ScenarioImpl) scenario).getId());
        assertNotNull(((LearningActivitySpaceImpl) las).getId());

        List activities = las.getActivities();
        for (int i = 0; i < activities.size(); i++) {
            ActivityImpl activity = (ActivityImpl) activities.get(i);
            assertNotNull(activity.getId());
            AnchorELO elo = activity.getAnchorELO();
            assertNotNull(elo);
            assertNotNull(((AnchorELOImpl)elo).getId());
            assert(elo.getInputTo() != null);
            assertNotNull((((LearningActivitySpaceImpl)elo.getInputTo()).getId()));
        }

    }


    private Scenario createPedagogicalPlan() {
        Scenario scenario = new ScenarioImpl();
        scenario.setName("Freakin Scenario");

        LearningActivitySpace las = new LearningActivitySpaceImpl();
        las.setName("LAS 1");
        scenario.setLearningActivitySpace(las);

        LearningActivitySpace las2 = new LearningActivitySpaceImpl();
        las2.setName("LAS 1");

        AnchorELO anchorElo = new AnchorELOImpl();
        las.addAnchorELO(anchorElo);
        anchorElo.setInputTo(las2);


        Activity firstActivity = new ActivityImpl();
        firstActivity.setName("THis is the first activity");
        las.addActivity(firstActivity);
        firstActivity.setAnchorELO(anchorElo);


        return scenario;


    }

}
