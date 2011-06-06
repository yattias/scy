package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2009
 * Time: 05:17:03
 */

@Entity
@DiscriminatorValue("template")
public class PedagogicalPlanTemplateImpl extends PedagogicalPlanBaseImpl implements PedagogicalPlanTemplate {

    private List<PedagogicalPlan> pedagogicalPlans;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "template", targetEntity = PedagogicalPlanImpl.class, fetch = FetchType.LAZY)
    public List<PedagogicalPlan> getPedagogicalPlans() {
        if(pedagogicalPlans == null) pedagogicalPlans = new LinkedList<PedagogicalPlan>();
        return pedagogicalPlans;
    }

    public void setPedagogicalPlans(List<PedagogicalPlan> pedagogicalPlans) {
        this.pedagogicalPlans = pedagogicalPlans;
    }

    @Override
    public void addPedagogicalPlan(PedagogicalPlan plan) {
        getPedagogicalPlans().add(plan);
        plan.setTemplate(this);
    }
}