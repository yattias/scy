package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.PedagogicalPlanImpl;
import eu.scy.core.model.pedagogicalplan.Mission;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlanTemplate;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.core.persistence.PedagogicalPlanPersistenceDAO;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2009
 * Time: 05:49:09
 */
public class PedagogicalPlanPersistenceDAOHibernate extends ScyBaseDAOHibernate implements PedagogicalPlanPersistenceDAO {

    private static Logger log = Logger.getLogger("PedagogicalPlanPersistenceDAOHibernate.class");

    public List<PedagogicalPlanTemplate> getPedagogicalPlanTemplates() {
        return getSession().createQuery("select pedagogicalPlanTemplate from PedagogicalPlanTemplateImpl as pedagogicalPlanTemplate order by name")
                .list();
    }

    public PedagogicalPlan createPedagogicalPlan(PedagogicalPlanTemplate template) {
        PedagogicalPlan plan = new PedagogicalPlanImpl();
        plan.setName(template.getName());
        template.addPedagogicalPlan(plan);
        plan.setTemplate(template);
        plan.setScenario(template.getScenario());
        plan.setMission(template.getMission());
        save(template);
        save(plan);
        return plan;
    }

    public PedagogicalPlan getPedagogicalPlanByName(String name) {
        return (PedagogicalPlan) getSession().createQuery("from PedagogicalPlanImpl where name like :name")
                .setString("name", name)
                .setMaxResults(1)
                .uniqueResult();
    }        

    @Override
    public List<Scenario> getCompatibleScenarios(Mission mission) {
        return getSession().createQuery("select plan.scenario from PedagogicalPlanImpl as plan where plan.mission = :mission")
                .setEntity("mission", mission)
                .list();
    }

    @Override
    public List<Mission> getCompatibleMissions(Scenario scenario) {
        return getSession().createQuery("select plan.mission from PedagogicalPlanImpl as plan where plan.scenario = :scenario")
                .setEntity("scenario" ,scenario)
                .list();
    }

    @Override
    public PedagogicalPlan getPedagogicalPlan(Mission mission, Scenario scenario) {
        log.info("Searching for plan for " + mission + " and " + scenario);
        List pedagogicalPlans = getSession().createQuery("from PedagogicalPlanImpl as plan where plan.scenario = :scenario and plan.mission = :mission order by plan.name")
                .setEntity("mission", mission)
                .setEntity("scenario", scenario)
                .list();
        log.info("===== ====== =======Found " + pedagogicalPlans.size() + " pedagogical plans - returning the first");
        if(pedagogicalPlans.size() > 0) return (PedagogicalPlan) pedagogicalPlans.get(0);
        return null;

    }

    @Override
    public List<PedagogicalPlan> getPedagogicalPlans() {
        return getSession().createQuery("from PedagogicalPlanImpl order by name")
                .list();
    }
}
