package eu.scy.core;

import eu.scy.core.model.ScyBase;
import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.core.model.pedagogicalplan.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2009
 * Time: 05:36:46
 */
public interface PedagogicalPlanPersistenceService extends BaseService{

    public List<PedagogicalPlanTemplate> getPedagogicalPlanTemplates();

    public PedagogicalPlan createPedagogicalPlan(PedagogicalPlanTemplate template);

    /**
     * retrieves a pedagogical plan based on its name
     * @param name
     * @return
     */

    public PedagogicalPlan getPedagogicalPlanByName(String name);

    PedagogicalPlan getPedagogicalPlan(String id);

    public List<Scenario> getCompatibleScenarios(Mission mission);

    public List<Mission> getCompatibleMissions(Scenario scenario);

    public PedagogicalPlan getPedagogicalPlan(Mission mission, Scenario scenario);

    /**
     * retrieves all pedagogical plans the current user has access to
     * @return
     */
    public List<PedagogicalPlan> getPedagogicalPlans();

    List<LearningActivitySpace> getLearningActivitySpaces(PedagogicalPlan pedagogicalPlan);

    void addAnchorEloToPedagogicalPlan(PedagogicalPlan pedagogicalPlan, AnchorELO anchorELO);

    public <AnchorELO> List getAnchorELOs(PedagogicalPlan pedagogicalPlan);

    PedagogicalPlan getOrCreatePedagogicalPlanFromURI(String missionURI);
}
