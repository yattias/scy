package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.PedagogicalPlanImpl;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlanTemplate;
import eu.scy.core.persistence.PedagogicalPlanPersistenceDAO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2009
 * Time: 05:49:09
 */
public class PedagogicalPlanPersistenceDAOHibernate extends ScyBaseDAOHibernate implements PedagogicalPlanPersistenceDAO {

    public List<PedagogicalPlanTemplate> getPedagogicalPlanTemplates() {
        return getSession().createQuery("select pedagogicalPlanTemplate from PedagogicalPlanTemplateImpl as pedagogicalPlanTemplate order by name")
                .list();
    }

    public PedagogicalPlan createPedagogicalPlan(PedagogicalPlanTemplate template) {
        PedagogicalPlan plan = new PedagogicalPlanImpl();
        template.addPedagogicalPlan(plan);
        save(plan);
        return plan;
    }

    public PedagogicalPlan getPedagogicalPlanByName(String name) {
        return (PedagogicalPlan) getSession().createQuery("select pedagogicalPlanTemplate from PedagogicalPlanImpl as pedagogicalPlanTemplate where name like :name")
                .setString("name", name)
                .setMaxResults(1)
                .uniqueResult();
    }
}
