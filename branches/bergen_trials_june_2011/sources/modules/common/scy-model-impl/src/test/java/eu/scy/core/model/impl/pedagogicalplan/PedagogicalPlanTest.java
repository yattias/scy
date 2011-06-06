package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.PedagogicalPlanTemplate;
import eu.scy.core.model.pedagogicalplan.Scenario;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2009
 * Time: 05:26:41
 */
public class PedagogicalPlanTest extends AbstractPedagogicalPlanTest {


    protected void setUp() throws Exception {
        PedagogicalPlanTemplate template = new PedagogicalPlanTemplateImpl();
        template.setName("Exploring shit");

        Scenario scenario = new ScenarioImpl();
        scenario.setName("The scenario of exploring shit");
        template.setScenario(scenario);

        assert (template.getScenario().equals(scenario));
    }

    public void testSomething() {

    }

}
