package eu.scy.core.persistence;

import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlanTemplate;

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
}
