package eu.scy.core.model.pedagogicalplan;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jul.2010
 * Time: 09:03:47
 * To change this template use File | Settings | File Templates.
 */
public interface PedagogicalPlanAnchorEloConnection extends BaseObject{
    PedagogicalPlan getPedagogicalPlan();

    void setPedagogicalPlan(PedagogicalPlan pedagogicalPlan);

    AnchorELO getAnchorELO();

    void setAnchorELO(AnchorELO anchorELO);
}
