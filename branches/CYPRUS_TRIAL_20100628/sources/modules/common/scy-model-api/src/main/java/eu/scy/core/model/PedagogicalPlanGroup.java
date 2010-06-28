package eu.scy.core.model;

import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:00:03
 * A group in the SCY system linked to a specific pedagogical plan. Groups can contain users.
 */
public interface PedagogicalPlanGroup extends Group {
    public PedagogicalPlan getPedagogicalPlan();
    public void setPedagogicalPlan(PedagogicalPlan pedagogicalPlan);
}