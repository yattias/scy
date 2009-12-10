package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlanTemplate;

import javax.persistence.*;

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

    @ManyToOne(targetEntity = PedagogicalPlanTemplateImpl.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="pedagogicalPlanTemplate_primKey")
    public PedagogicalPlanTemplate getTemplate() {
        return pedagogicalPlanTemplate;
    }

    public void setTemplate(PedagogicalPlanTemplate pedagogicalPlanTemplate) {
        this.pedagogicalPlanTemplate = pedagogicalPlanTemplate;
    }
}
