package eu.scy.core.model.pedagogicalplan;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 12:10:19
 * To change this template use File | Settings | File Templates.
 */
public interface PedagogicalPlanTemplate extends PedagogicalPlanBase, Template{

    public List<PedagogicalPlan> getPedagogicalPlans();

    void addPedagogicalPlan(PedagogicalPlan plan);
}
