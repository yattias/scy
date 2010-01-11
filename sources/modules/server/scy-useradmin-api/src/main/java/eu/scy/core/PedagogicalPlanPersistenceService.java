package eu.scy.core;

import eu.scy.core.model.ScyBase;
import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlanTemplate;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2009
 * Time: 05:36:46
 * To change this template use File | Settings | File Templates.
 */
public interface PedagogicalPlanPersistenceService {

    public List<PedagogicalPlanTemplate> getPedagogicalPlanTemplates(); 

    public void save(ScyBase scyBaseObject);

    PedagogicalPlan getPedagogicalPlanByName(String name);
}
