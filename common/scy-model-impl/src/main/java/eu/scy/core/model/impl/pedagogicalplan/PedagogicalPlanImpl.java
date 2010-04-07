package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlanTemplate;

import javax.persistence.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2009
 * Time: 06:27:57
 * To change this template use File | Settings | File Templates.
 */
@Entity
@DiscriminatorValue("pedagogicalPlan")
public class PedagogicalPlanImpl extends PedagogicalPlanBaseImpl implements PedagogicalPlan {

    private PedagogicalPlanTemplate pedagogicalPlanTemplate = null;

    private Boolean published = false;
    private Boolean makeAllAssignedStudentsBuddies = false;


    @ManyToOne(targetEntity = PedagogicalPlanTemplateImpl.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedagogicalPlanTemplate_primKey")
    public PedagogicalPlanTemplate getTemplate() {
        return pedagogicalPlanTemplate;
    }

    public void setTemplate(PedagogicalPlanTemplate pedagogicalPlanTemplate) {
        this.pedagogicalPlanTemplate = pedagogicalPlanTemplate;
    }


    @Override
    public Boolean getPublished() {
        return published;
    }

    @Override
    public void setPublished(Boolean published) {
        this.published = published;
    }

    @Override
    public Boolean getMakeAllAssignedStudentsBuddies() {
        return makeAllAssignedStudentsBuddies;
    }

    @Override
    public void setMakeAllAssignedStudentsBuddies(Boolean makeAllAssignedStudentsBuddies) {
        this.makeAllAssignedStudentsBuddies = makeAllAssignedStudentsBuddies;
    }
}
