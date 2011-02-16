package eu.scy.core.model.impl;

import eu.scy.core.model.PedagogicalPlanGroup;
import eu.scy.core.model.impl.pedagogicalplan.PedagogicalPlanImpl;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:00:11
 */
@Entity
@DiscriminatorValue(value = "PedagogicalPlanGroup")
public class PedagogicalPlanGroupImpl extends GroupImpl implements PedagogicalPlanGroup {

    private PedagogicalPlan pedagogicalPlan;

    @ManyToOne(targetEntity = PedagogicalPlanImpl.class)
    @JoinColumn(name = "pedagogicalPlan_fk")
    public PedagogicalPlan getPedagogicalPlan() {
        return pedagogicalPlan;
    }

    public void setPedagogicalPlan(PedagogicalPlan pedagogicalPlan) {
        this.pedagogicalPlan = pedagogicalPlan;
    }
}