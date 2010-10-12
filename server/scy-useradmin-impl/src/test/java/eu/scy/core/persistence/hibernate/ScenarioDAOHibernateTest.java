package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.*;
import eu.scy.core.model.impl.pedagogicalplan.ToolImpl;
import eu.scy.core.model.pedagogicalplan.*;
import eu.scy.core.persistence.ScenarioDAO;
import org.junit.Test;


import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.okt.2009
 * Time: 21:24:04
 * To change this template use File | Settings | File Templates.
 */
public class ScenarioDAOHibernateTest extends AbstractDAOTest {

    private ScenarioDAO scenarioDAO;


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
        Scenario scenario = createScenario();
        getScenarioDAO().save(scenario);

        LearningActivitySpace las = scenario.getLearningActivitySpace();

        getScenarioDAO().save(scenario);
        assertNotNull(((ScenarioImpl) scenario).getId());
        assertNotNull(((LearningActivitySpaceImpl) las).getId());

    }

    @Test
    public void testSaveScenarioWithLASAndActivity() {
        Scenario scenario = createScenario();
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
            assertNotNull(((AnchorELOImpl) elo).getId());
            assert (elo.getInputTo() != null);
            assertNotNull((((LearningActivitySpaceImpl) elo.getInputTo()).getId()));


            /*Iterator it = activity.getLearningActivitySpaceToolConfigurations().iterator();
            assert(it.hasNext());
            while (it.hasNext()) {
                LearningActivitySpaceToolConfiguration learningActivitySpaceToolConfiguration = (LearningActivitySpaceToolConfiguration) it.next();
                assert(learningActivitySpaceToolConfiguration.getTool() != null);
                ToolImpl tool = (ToolImpl) learningActivitySpaceToolConfiguration.getTool();
                assert(tool.getId() != null);
            } */

        }

    }


    private Scenario createScenario() {
        Scenario scenario = new ScenarioImpl();
        scenario.setName("Freakin Scenario");

        getScenarioDAO().save(scenario);

        LearningActivitySpace las = createLAS("LAS 1");
        scenario.setLearningActivitySpace(las);
        las.setParticipatesIn(scenario);


        LearningActivitySpace las2 = createLAS("LAS 2");
        las2.setParticipatesIn(scenario);

        AnchorELO anchorElo = new AnchorELOImpl();
        anchorElo.setInputTo(las2);


        Activity firstActivity = createActivity("THis is the first activity", "DESC: THis is the first activity");
        firstActivity.setName("THis is the first activity");
        las.addActivity(firstActivity);
        firstActivity.setAnchorELO(anchorElo);

        
        getScenarioDAO().save(scenario);
        return scenario;


    }

    private Activity createActivity(String name, String description) {
        Activity activity = new ActivityImpl();
        activity.setName(name);
        activity.setDescription(description);

        LearningActivitySpaceToolConfiguration learningActivitySpaceToolConfiguration = new SCYMapperConfiguration();
        learningActivitySpaceToolConfiguration.setName("SCYMapperConfiguration-" + name);
        activity.addLearningActivitySpaceToolConfiguration(learningActivitySpaceToolConfiguration);

        eu.scy.core.model.pedagogicalplan.Tool scyMapper = new ToolImpl();
        scyMapper.setName("SCYMapper");
        scyMapper.setDescription("A CONCEPTMAPPING TOOL");
        learningActivitySpaceToolConfiguration.setTool(scyMapper);


        return activity;
    }

    @Test
    public void testLoadAllActivititesForLAS() {
        Scenario scenario = createScenario();
        getScenarioDAO().save(scenario);
        LearningActivitySpace las = scenario.getLearningActivitySpace();


        List <Activity> activities = getScenarioDAO().getAllActivitiesForLAS(las);
    }


}
