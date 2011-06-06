package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.*;
import eu.scy.core.persistence.PedagogicalPlanPersistenceDAO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2009
 * Time: 05:37:28
 */
public class PedagogicalPlanPersistenceServiceImpl extends BaseServiceImpl implements PedagogicalPlanPersistenceService {

    private static Logger log = Logger.getLogger("PedagogicalPlanPersistenceServiceImpl.class");

    private PedagogicalPlanPersistenceDAO pedagogicalPlanPersistenceDAO;

    public PedagogicalPlanPersistenceDAO getPedagogicalPlanPersistenceDAO() {
        return (PedagogicalPlanPersistenceDAO) getScyBaseDAO();
    }

    public void setPedagogicalPlanPersistenceDAO(PedagogicalPlanPersistenceDAO pedagogicalPlanPersistenceDAO) {
        this.pedagogicalPlanPersistenceDAO = pedagogicalPlanPersistenceDAO;
    }

    public List<PedagogicalPlanTemplate> getPedagogicalPlanTemplates() {
        return pedagogicalPlanPersistenceDAO.getPedagogicalPlanTemplates();
    }

    @Transactional
    public PedagogicalPlan createPedagogicalPlan(PedagogicalPlanTemplate template) {
        return getPedagogicalPlanPersistenceDAO().createPedagogicalPlan(template);
    }

    @Transactional
    public void save(Object scyBaseObject) {
        getPedagogicalPlanPersistenceDAO().save(scyBaseObject);
    }

    public PedagogicalPlan getPedagogicalPlanByName(String name) {
        return getPedagogicalPlanPersistenceDAO().getPedagogicalPlanByName(name);
    }

    @Override
    public PedagogicalPlan getPedagogicalPlan(String id) {
        return getPedagogicalPlanPersistenceDAO().getPedagogicalPlan(id);
    }

    @Override
    public List<Scenario> getCompatibleScenarios(Mission mission) {
        return getPedagogicalPlanPersistenceDAO().getCompatibleScenarios(mission);
    }

    @Override
    public List<Mission> getCompatibleMissions(Scenario scenario) {
        return getPedagogicalPlanPersistenceDAO().getCompatibleMissions(scenario);
    }

    @Override
    public PedagogicalPlan getPedagogicalPlan(Mission mission, Scenario scenario) {
        return getPedagogicalPlanPersistenceDAO().getPedagogicalPlan(mission, scenario);
    }

    @Override
    public List<PedagogicalPlan> getPedagogicalPlans() {
        return getPedagogicalPlanPersistenceDAO().getPedagogicalPlans();
    }

    @Override
    public List<LearningActivitySpace> getLearningActivitySpaces(PedagogicalPlan pedagogicalPlan) {
        return getPedagogicalPlanPersistenceDAO().getLearningActivitySpaces(pedagogicalPlan);
    }

    @Override
    @Transactional
    public void addAnchorEloToPedagogicalPlan(PedagogicalPlan pedagogicalPlan, AnchorELO anchorELO) {
        getPedagogicalPlanPersistenceDAO().addAnchorEloToPedagogicalPlan(pedagogicalPlan, anchorELO);
    }

    @Override
    public <AnchorELO> List getAnchorELOs(PedagogicalPlan pedagogicalPlan) {
        return getPedagogicalPlanPersistenceDAO().getAnchorELOs(pedagogicalPlan);
    }

    @Override
    @Transactional
    public PedagogicalPlan getOrCreatePedagogicalPlanFromURI(String missionURI) {
        return getPedagogicalPlanPersistenceDAO().getOrCreatePedagogicalPlanFromURI(missionURI);
    }
}