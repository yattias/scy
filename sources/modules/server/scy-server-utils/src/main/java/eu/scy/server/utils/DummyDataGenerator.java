package eu.scy.server.utils;

import eu.scy.core.ScenarioService;
import eu.scy.core.model.impl.pedagogicalplan.ActivityImpl;
import eu.scy.core.model.impl.pedagogicalplan.AnchorELOImpl;
import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceImpl;
import eu.scy.core.model.impl.pedagogicalplan.ScenarioImpl;
import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.nov.2009
 * Time: 22:59:44
 * To change this template use File | Settings | File Templates.
 */
public class DummyDataGenerator implements InitializingBean {

    private ScenarioService scenarioService;

    private void generateDummyScenario() {
        Scenario scenario = new ScenarioImpl();
        scenario.setName("Design challenge - the freakin scy way");

        LearningActivitySpace las = createLAS("Orientation");
        scenario.setLearningActivitySpace(las);

        Activity activity = createActivity("Use conceptmap");
        las.addActivity(activity);
        AnchorELO elo1 = createAnchorELO("Concept map");
        activity.setAnchorELO(elo1);

        LearningActivitySpace conceptualisation = createLAS("Conceptualization");
        elo1.setInputTo(conceptualisation);

        Activity conceptualizationActivity = createActivity("Conceptualization activity");
        conceptualisation.addActivity(conceptualizationActivity);

        AnchorELO conceptualizationELO = createAnchorELO("ConceptualisationELO");
        conceptualizationActivity.setAnchorELO(conceptualizationELO);

        getScenarioService().createScenario(scenario);



    }

    private LearningActivitySpace createLAS(String name) {
        LearningActivitySpace las = new LearningActivitySpaceImpl();
        las.setName(name);
        return las;
    }

    private Activity createActivity(String name) {
        Activity activity = new ActivityImpl();
        activity.setName(name);
        return activity;
    }

    private AnchorELO createAnchorELO(String name) {
        AnchorELO elo = new AnchorELOImpl();
        elo.setName(name);
        return elo;
    }


    public ScenarioService getScenarioService() {
        return scenarioService;
    }

    public void setScenarioService(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        generateDummyScenario();
    }
}
