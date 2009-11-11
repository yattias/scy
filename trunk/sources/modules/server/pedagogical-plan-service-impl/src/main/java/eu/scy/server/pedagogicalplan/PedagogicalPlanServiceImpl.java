package eu.scy.server.pedagogicalplan;

import eu.scy.core.ScenarioService;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.Scenario;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 06.nov.2009
 * Time: 05:51:54
 * To change this template use File | Settings | File Templates.
 */
public class PedagogicalPlanServiceImpl implements PedagogicalPlanService {

    private static Logger log = Logger.getLogger("PedagogicalPlanServiceImpl.class");

    private ScenarioService scenarioService = null;

    public List<Scenario> getScenarios() {
        return getScenarioService().getScenarios();
    }

    public List<PedagogicalPlan> getPedagogicalPlans() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ScenarioService getScenarioService() {
        return scenarioService;
    }

    public void setScenarioService(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }
}
