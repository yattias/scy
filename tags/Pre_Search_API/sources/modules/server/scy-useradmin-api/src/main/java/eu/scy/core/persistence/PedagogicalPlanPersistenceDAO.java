package eu.scy.core.persistence;

import eu.scy.core.model.pedagogicalplan.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2009
 * Time: 05:48:57
 */
public interface PedagogicalPlanPersistenceDAO extends SCYBaseDAO {

    public List<PedagogicalPlanTemplate> getPedagogicalPlanTemplates();

    PedagogicalPlan createPedagogicalPlan(PedagogicalPlanTemplate template);

    PedagogicalPlan getPedagogicalPlanByName(String name);

    public List<Scenario> getCompatibleScenarios(Mission mission);

    public List<Mission> getCompatibleMissions(Scenario scenario);

    public PedagogicalPlan getPedagogicalPlan(Mission mission, Scenario scenario);

    public List<PedagogicalPlan> getPedagogicalPlans();

    public PedagogicalPlan getPedagogicalPlan(String id);

    public List<LearningActivitySpace> getLearningActivitySpaces(PedagogicalPlan pedagogicalPlan);

    public void addAnchorEloToPedagogicalPlan(PedagogicalPlan pedagogicalPlan, AnchorELO anchorELO);

    public List getAnchorELOs(PedagogicalPlan pedagogicalPlan);

    PedagogicalPlan getOrCreatePedagogicalPlanFromURI(String missionURI);

}
