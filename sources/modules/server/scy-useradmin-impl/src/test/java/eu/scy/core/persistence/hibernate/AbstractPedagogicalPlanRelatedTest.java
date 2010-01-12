package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.AssessmentImpl;
import eu.scy.core.model.impl.pedagogicalplan.PedagogicalPlanTemplateImpl;
import eu.scy.core.model.impl.pedagogicalplan.PeerToPeerAssessmentStrategyImpl;
import eu.scy.core.model.pedagogicalplan.*;
import eu.scy.core.persistence.PedagogicalPlanPersistenceDAO;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jan.2010
 * Time: 12:12:47
 * To change this template use File | Settings | File Templates.
 */
public class AbstractPedagogicalPlanRelatedTest extends AbstractDAOTest{

    protected PedagogicalPlanPersistenceDAO pedagogicalPlanPersistenceDAO = null;

    protected Assessment createAssessment(String name) {
        Assessment assessment = new AssessmentImpl();
        assessment.setName(name);
        return assessment;
    }

    public PedagogicalPlanPersistenceDAO getPedagogicalPlanPersistenceDAO() {
        return pedagogicalPlanPersistenceDAO;
    }

    public void setPedagogicalPlanPersistenceDAO(PedagogicalPlanPersistenceDAO pedagogicalPlanPersistenceDAO) {
        this.pedagogicalPlanPersistenceDAO = pedagogicalPlanPersistenceDAO;
    }

    protected AssessmentStrategy createAssessmentStrategy() {
        PeerToPeerAssessmentStrategy strategy = new PeerToPeerAssessmentStrategyImpl();
        return strategy;
    }

    protected PedagogicalPlanTemplateImpl createPedagogicalPlanTemplate(String name) {
        PedagogicalPlanTemplateImpl template1 = new PedagogicalPlanTemplateImpl();
        template1.setName(name);
        return template1;
    }

    protected PedagogicalPlan createPedagogicalPlan(PedagogicalPlanTemplate template) {
        PedagogicalPlan plan = getPedagogicalPlanPersistenceDAO().createPedagogicalPlan(template);
        //template.addPedagogicalPlan(plan);
        return plan;
    }

    @Test
    public void testSetup() {
        assertNotNull(getPedagogicalPlanPersistenceDAO());
    }
}
