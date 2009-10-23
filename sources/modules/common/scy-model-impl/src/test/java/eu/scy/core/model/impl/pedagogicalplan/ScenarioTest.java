package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.Scenario;
import org.testng.annotations.Test;

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

        assert(scenario.getLearningActivitySpace() != null);
        assert(learningActivitySpace.getActivities().size() ==2);



    }


    private Activity createActivity(String name, String description) {
        Activity activity = new ActivityImpl();
        activity.setName(name);
        activity.setDescription(description);

        return activity;
    }

}
