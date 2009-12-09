package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.PedagogicalPlanImpl;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlanTemplate;
import eu.scy.core.persistence.PedagogicalPlanPersistenceDAO;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2009
 * Time: 05:49:09
 * To change this template use File | Settings | File Templates.
 */
public class PedagogicalPlanPersistenceDAOHibernate extends ScyBaseDAOHibernate implements PedagogicalPlanPersistenceDAO {

    public List<PedagogicalPlanTemplate> getPedagogicalPlanTemplates() {
        return getSession().createQuery("select pedagogicalPlanTemplate from PedagogicalPlanTemplateImpl as pedagogicalPlanTemplate order by name")
                .list();
    }

    @Override
    public PedagogicalPlan createPedagogicalPlan(PedagogicalPlanTemplate template) {
        PedagogicalPlan plan = new PedagogicalPlanImpl();
        template.addPedagogicalPlan(plan);
        save(plan);
        return plan;
    }
}
