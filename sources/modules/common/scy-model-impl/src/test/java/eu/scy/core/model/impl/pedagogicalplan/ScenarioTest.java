package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.Scenario;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.okt.2009
 * Time: 14:29:31
 * To change this template use File | Settings | File Templates.
 */
@Test
public class ScenarioTest {

    @Test
    public void setupPedagogicalPlan() {

        Scenario scenario = new ScenarioImpl();
        scenario.setName("This is the default scenario");

        LearningActivitySpace learningActivitySpace = new LearningActivitySpaceImpl();
        learningActivitySpace.setName("Exploration LAS");
        scenario.setLearningActivitySpace(learningActivitySpace);

        learningActivitySpace.addActivity(createActivity("Activity 1", "Use your SCYMapper here"));
        learningActivitySpace.addActivity(createActivity("Activity 2", "Here we go - create a presentation with your personal SCYPresentation Tool"));

        AnchorELO anchorElo = new AnchorELOImpl();
        learningActivitySpace.addAnchorELO(anchorElo);

        assert(scenario.getLearningActivitySpace() != null);
        assert(learningActivitySpace.getActivities().size() ==2);

        assert(learningActivitySpace.getProduces().contains(anchorElo));



    }


    @Test
    public void testTraversingScenario() {

        Scenario scenario = createScenario();

        LearningActivitySpace firstSpace = scenario.getLearningActivitySpace();
        assert(firstSpace != null);

        List activities = firstSpace.getActivities();

        boolean foundAnAnchorElo = false;

        for (int i = 0; i < activities.size(); i++) {
            Activity activity = (Activity) activities.get(i);
            assert(activity.getLearningActivitySpace() != null);

            AnchorELO anchorELO = activity.getAnchorELO();

            if(anchorELO != null) {
                assert(anchorELO.getInputTo() != null);
                foundAnAnchorElo = true;
            }


        }

        //doing this to check that in fact we have checked that anchorelos are connected to LAS's
        assert(foundAnAnchorElo);



    }


    private Activity createActivity(String name, String description) {
        Activity activity = new ActivityImpl();
        activity.setName(name);
        activity.setDescription(description);

        return activity;
    }

    private Scenario createScenario() {
        Scenario scenario = new ScenarioImpl();
        scenario.setName("DA SCENARIO");

        LearningActivitySpace planning = new LearningActivitySpaceImpl();
        planning.setName("Planning");
        scenario.setLearningActivitySpace(planning);


        Activity firstActivity = new ActivityImpl();
        firstActivity.setName("Gather in the big hall and listen to your teacher");
        planning.addActivity(firstActivity);

        Activity conceptMappingSession = new  ActivityImpl();
        conceptMappingSession.setName("Concept mapping");
        planning.addActivity(conceptMappingSession);

        AnchorELO conceptMap = new AnchorELOImpl();
        conceptMap.setName("Expected concept map");
        conceptMappingSession.setAnchorELO(conceptMap);

        LearningActivitySpace lastSpace = new LearningActivitySpaceImpl();
        lastSpace.setName("Evaluation");
        conceptMap.setInputTo(lastSpace);

        return scenario;
    }

}
