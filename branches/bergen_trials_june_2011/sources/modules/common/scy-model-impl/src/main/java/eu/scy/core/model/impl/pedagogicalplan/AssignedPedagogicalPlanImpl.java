package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.pedagogicalplan.AssignedPedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 25.mar.2010
 * Time: 12:30:07
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "assignedpedagogicalplan")
public class AssignedPedagogicalPlanImpl extends BaseObjectImpl implements AssignedPedagogicalPlan{

    private PedagogicalPlan pedagogicalPlan;
    private User user;
    private Boolean useCriteriaBasedAssessment;

    @Override
    @ManyToOne(targetEntity = PedagogicalPlanImpl.class,  fetch = FetchType.LAZY)
	@JoinColumn(name = "pedagogicalPlan_primKey")
    public PedagogicalPlan getPedagogicalPlan() {
        return pedagogicalPlan;
    }

    @Override
    public void setPedagogicalPlan(PedagogicalPlan pedagogicalPlan) {
        this.pedagogicalPlan = pedagogicalPlan;
    }

    @Override
    @ManyToOne(targetEntity = SCYUserImpl.class,  fetch = FetchType.LAZY)
	@JoinColumn(name = "user_primKey")
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Boolean getUseCriteriaBasedAssessment() {
        return useCriteriaBasedAssessment;
    }

    @Override
    public void setUseCriteriaBasedAssessment(Boolean useCriteriaBasedAssessment) {
        this.useCriteriaBasedAssessment = useCriteriaBasedAssessment;
    }
}
