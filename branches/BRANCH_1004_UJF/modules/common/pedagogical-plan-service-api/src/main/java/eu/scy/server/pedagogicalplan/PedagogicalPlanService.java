package eu.scy.server.pedagogicalplan;

import eu.scy.core.model.auth.SessionInfo;
import eu.scy.core.model.pedagogicalplan.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 06.nov.2009
 * Time: 05:47:26
 * To change this template use File | Settings | File Templates.
 */
public interface PedagogicalPlanService {

    public SessionInfo login(String username, String password);

    public List <Tool> getTools();

    /**
     * Returns all available scenarios
     * @return
     */
    public List<Scenario> getScenarios();

    /**
     * returns all available missions
     * @return
     */
    public List<Mission> getMissions();

    public List<PedagogicalPlanTemplate> getPedagogicalPlanTemplates();

    public PedagogicalPlan createPedagogicalPlan(PedagogicalPlanTemplate template);

    public List <Scenario> getCompatibleScenarios(Mission mission);

    public List <Mission> getCompatibleMissions(Scenario scenario);

    /**
     * retrieves the pedagogical plan that is composed of the mission and scenario
     * @param mission
     * @param scenario
     * @return
     */
    public PedagogicalPlan getPedagogicalPlan(Mission mission, Scenario scenario);

    public List <LearningActivitySpaceToolConfiguration> getToolConfigurations(LearningActivitySpace learningActivitySpace);

    public List <PedagogicalPlan> getPedagogicalPlans();

    public List <AnchorELO> getAnchorELOsProducedBy(LearningActivitySpace learningActivitySpace);
}
