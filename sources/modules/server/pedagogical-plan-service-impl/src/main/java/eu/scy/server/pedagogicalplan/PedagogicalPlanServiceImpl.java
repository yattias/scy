package eu.scy.server.pedagogicalplan;

import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.ScenarioService;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.model.auth.SessionInfo;
import eu.scy.core.model.impl.auth.SessionInfoImpl;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlanTemplate;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.core.model.pedagogicalplan.Tool;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 06.nov.2009
 * Time: 05:51:54
 * To change this template use File | Settings | File Templates.
 */
public class PedagogicalPlanServiceImpl extends AbstractPedagogicalPlanService implements PedagogicalPlanService {

    private static Logger log = Logger.getLogger("PedagogicalPlanServiceImpl.class");

    private ScenarioService scenarioService = null;
    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;

    @Override
    public List<Tool> getTools() {
        return null;
    }

    public List<Scenario> getScenarios() {
        return getScenarioService().getScenarios();
    }

    public List<PedagogicalPlanTemplate> getPedagogicalPlanTemplates() {
        return getPedagogicalPlanPersistenceService().getPedagogicalPlanTemplates();
    }

    public ScenarioService getScenarioService() {
        return scenarioService;
    }

    public void setScenarioService(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
    }

}
