package eu.scy.core.persistence;

import eu.scy.core.model.pedagogicalplan.Mission;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlanTemplate;
import eu.scy.core.model.pedagogicalplan.Scenario;

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
}
