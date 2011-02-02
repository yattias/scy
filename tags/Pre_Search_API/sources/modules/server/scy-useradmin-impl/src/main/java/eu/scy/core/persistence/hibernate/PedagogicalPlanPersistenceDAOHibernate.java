package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.PedagogicalPlanAnchorEloConnectionImpl;
import eu.scy.core.model.impl.pedagogicalplan.PedagogicalPlanImpl;
import eu.scy.core.model.pedagogicalplan.*;
import eu.scy.core.persistence.PedagogicalPlanPersistenceDAO;

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
        List<Scenario> scenarios = getSession().createQuery("select plan.scenario from PedagogicalPlanImpl as plan where plan.mission = :mission")
                .setEntity("mission", mission)
                .list();

        if (!scenarios.isEmpty()) {
            scenarios = getSession().createQuery("select distinct (scenario) from ScenarioImpl as scenario where scenario in(:scenarios)")
                    .setParameterList("scenarios", scenarios)
                    .list();
        }

        return scenarios;
    }

    @Override
    public List<Mission> getCompatibleMissions(Scenario scenario) {
        return getSession().createQuery("select distinct(plan.mission) from PedagogicalPlanImpl as plan where plan.scenario = :scenario")
                .setEntity("scenario", scenario)
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
        if (pedagogicalPlans.size() > 0) return (PedagogicalPlan) pedagogicalPlans.get(0);
        return null;

    }

    @Override
    public List<PedagogicalPlan> getPedagogicalPlans() {
        return getSession().createQuery("from PedagogicalPlanImpl order by name")
                .list();
    }

    @Override
    public PedagogicalPlan getPedagogicalPlan(String id) {
        return (PedagogicalPlan) getSession().createQuery("from PedagogicalPlanImpl where id like :id")
                .setString("id", id)
                .uniqueResult();

    }

    @Override
    public List<LearningActivitySpace> getLearningActivitySpaces(PedagogicalPlan pedagogicalPlan) {
        pedagogicalPlan = (PedagogicalPlan) getHibernateTemplate().merge(pedagogicalPlan);
        log.info("GOT PED PLAN: " + pedagogicalPlan);
        Scenario scenario = (Scenario) getHibernateTemplate().merge(pedagogicalPlan.getScenario());
        log.info("GOT SCENARIO: " + scenario);
        return getSession().createQuery("from LearningActivitySpaceImpl as las where las.participatesIn = :scenario")
                .setEntity("scenario", scenario)
                .list();

    }

    @Override
    public void addAnchorEloToPedagogicalPlan(PedagogicalPlan pedagogicalPlan, AnchorELO anchorELO) {
        if(pedagogicalPlan == null || anchorELO == null) throw new RuntimeException("Illegal null value: pedagogical plan: " + pedagogicalPlan + " anchor ELO: " + anchorELO);
        PedagogicalPlanAnchorEloConnection connection = new PedagogicalPlanAnchorEloConnectionImpl();
        connection.setPedagogicalPlan(pedagogicalPlan);
        connection.setAnchorELO(anchorELO);
        save(connection);
    }

    @Override
    public List getAnchorELOs(PedagogicalPlan pedagogicalPlan) {
        return getSession().createQuery("select connection.anchorELO from PedagogicalPlanAnchorEloConnectionImpl as connection where connection.pedagogicalPlan = :pedagogicalPlan")
                .setEntity("pedagogicalPlan", pedagogicalPlan)
                .list();
    }

    @Override
    public PedagogicalPlan getOrCreatePedagogicalPlanFromURI(String missionURI) {
        PedagogicalPlan pedPlan = (PedagogicalPlan) getSession().createQuery("from PedagogicalPlanImpl where missionURI like :missionURI")
                .setString("missionURI", missionURI)
                .uniqueResult();

        if(pedPlan  == null) {
            logger.info("DID NOT FIND A PLAN - CREATING ONE!!!");
            pedPlan = new PedagogicalPlanImpl();
            pedPlan.setMissionURI(missionURI);
            save(pedPlan);
        }

        return pedPlan;

    }

}
