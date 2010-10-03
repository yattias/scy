package eu.scy.server.pedagogicalplan;

import eu.scy.core.LASService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.ScenarioService;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.model.auth.SessionInfo;
import eu.scy.core.model.impl.auth.SessionInfoImpl;
import eu.scy.core.model.pedagogicalplan.*;

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
    private LASService lasService;

    public LASService getLasService() {
        return lasService;
    }

    public void setLasService(LASService lasService) {
        this.lasService = lasService;
    }

    @Override
    public List<Tool> getTools() {
        return null;
    }

    public List<Scenario> getScenarios() {
        return getScenarioService().getScenarios();
    }

    @Override
    public List<Mission> getMissions() {
        return getScenarioService().getMissions();
    }

    public List<PedagogicalPlanTemplate> getPedagogicalPlanTemplates() {
        return getPedagogicalPlanPersistenceService().getPedagogicalPlanTemplates();
    }

    @Override
    public PedagogicalPlan createPedagogicalPlan(PedagogicalPlanTemplate template) {
        return getPedagogicalPlanPersistenceService().createPedagogicalPlan(template);
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

    public List <Scenario> getCompatibleScenarios(Mission mission) {
        return this.pedagogicalPlanPersistenceService.getCompatibleScenarios(mission);
        
    }

    @Override
    public List<Mission> getCompatibleMissions(Scenario scenario) {
        return this.getPedagogicalPlanPersistenceService().getCompatibleMissions(scenario);
    }

    @Override
    public PedagogicalPlan getPedagogicalPlan(Mission mission, Scenario scenario) {
        return getPedagogicalPlanPersistenceService().getPedagogicalPlan(mission, scenario);
    }

    @Override
    public List<LearningActivitySpaceToolConfiguration> getToolConfigurations(LearningActivitySpace learningActivitySpace) {
        return getLasService().getToolConfigurations(learningActivitySpace);
    }

    @Override
    public List<PedagogicalPlan> getPedagogicalPlans() {
        return getPedagogicalPlanPersistenceService().getPedagogicalPlans();
    }

    @Override
    public List<AnchorELO> getAnchorELOsProducedBy(LearningActivitySpace learningActivitySpace) {
        return getLasService().getAnchorELOsProducedByLAS(learningActivitySpace);
    }

}
