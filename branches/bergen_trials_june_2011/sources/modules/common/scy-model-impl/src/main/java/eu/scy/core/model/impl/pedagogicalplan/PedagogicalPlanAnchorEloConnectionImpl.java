package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.impl.ELORefImpl;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlanAnchorEloConnection;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jul.2010
 * Time: 09:04:10
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table (name = "pedagogicalplananchoreloconnection" )
public class PedagogicalPlanAnchorEloConnectionImpl extends BaseObjectImpl implements PedagogicalPlanAnchorEloConnection {

    private PedagogicalPlan pedagogicalPlan;
    private AnchorELO anchorELO;

    @Override
    @OneToOne(targetEntity = PedagogicalPlanImpl.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedagogicalPlan_primKey")
    public PedagogicalPlan getPedagogicalPlan() {
        return pedagogicalPlan;
    }

    @Override
    public void setPedagogicalPlan(PedagogicalPlan pedagogicalPlan) {
        this.pedagogicalPlan = pedagogicalPlan;
    }

    @Override
    @OneToOne(targetEntity = AnchorELOImpl.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "anchorelo_primKey")
    public AnchorELO getAnchorELO() {
        return anchorELO;
    }

    @Override
    public void setAnchorELO(AnchorELO anchorELO) {
        this.anchorELO = anchorELO;
    }
}
