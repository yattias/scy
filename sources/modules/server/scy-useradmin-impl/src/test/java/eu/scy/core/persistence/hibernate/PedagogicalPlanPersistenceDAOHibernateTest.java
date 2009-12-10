package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.*;
import eu.scy.core.model.pedagogicalplan.*;
import eu.scy.core.persistence.PedagogicalPlanPersistenceDAO;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2009
 * Time: 05:55:01
 * To change this template use File | Settings | File Templates.
 */
public class PedagogicalPlanPersistenceDAOHibernateTest extends AbstractDAOTest {

    private PedagogicalPlanPersistenceDAO pedagogicalPlanPersistenceDAO = null;

    public PedagogicalPlanPersistenceDAO getPedagogicalPlanPersistenceDAO() {
        return pedagogicalPlanPersistenceDAO;
    }

    public void setPedagogicalPlanPersistenceDAO(PedagogicalPlanPersistenceDAO pedagogicalPlanPersistenceDAO) {
        this.pedagogicalPlanPersistenceDAO = pedagogicalPlanPersistenceDAO;
    }

    @Test
    public void testSetup() {
        assertNotNull(getPedagogicalPlanPersistenceDAO());
    }

    @Test
    public void testPersistPedagogicalPlanTemplate() {
        PedagogicalPlanTemplateImpl template = new PedagogicalPlanTemplateImpl();
        assert (template.getId() == null);
        getPedagogicalPlanPersistenceDAO().save(template);
        assert (template.getId() != null);
    }

    @Test
    public void getCascadingSaveOfPedagogicalPlanTemplate() {
        PedagogicalPlanTemplateImpl template = new PedagogicalPlanTemplateImpl();
        ScenarioImpl scenario = new ScenarioImpl();
        assertNull(scenario.getId());
        template.setScenario(scenario);
        getPedagogicalPlanPersistenceDAO().save(template);
        assertNotNull(scenario.getId());
    }

    @Test
    public void testRetrievingPedagogicalPlans() {
        PedagogicalPlanTemplateImpl template1 = createPedagogicalPlanTemplate("Template 1");
        getPedagogicalPlanPersistenceDAO().save(template1);

        PedagogicalPlanTemplateImpl template2 = createPedagogicalPlanTemplate("Template 2");
        getPedagogicalPlanPersistenceDAO().save(template2);

        assertTrue(getPedagogicalPlanPersistenceDAO().getPedagogicalPlanTemplates().size() == 2);

    }

    @Test
    public void testPersistingPedagogicalPlan() {
        PedagogicalPlanTemplate template = createPedagogicalPlanTemplate("A pedagogical plan");
        PedagogicalPlanImpl plan = (PedagogicalPlanImpl) createPedagogicalPlan(template);
        assertTrue(plan.getId() != null);
        assertTrue(((PedagogicalPlanTemplateImpl) plan.getTemplate()).getId() != null);
    }

    @Test
    public void testGetPlansThatExtendsATemplate() {
        PedagogicalPlanTemplateImpl template = createPedagogicalPlanTemplate("A template with a bunch of plans");
        assertNull(template.getId());

        PedagogicalPlanImpl plan1 = (PedagogicalPlanImpl) createPedagogicalPlan(template);
        PedagogicalPlanImpl plan2 = (PedagogicalPlanImpl) createPedagogicalPlan(template);
        PedagogicalPlanImpl plan3 = (PedagogicalPlanImpl) createPedagogicalPlan(template);

        getPedagogicalPlanPersistenceDAO().save(template);
        assertNotNull(template.getId());

        assertNotNull(plan1.getId());
        assertNotNull(plan2.getId());
        assertNotNull(plan3.getId());

        assertNotNull(plan1.getTemplate());
        assertNotNull(plan2.getTemplate());
        assertNotNull(plan3.getTemplate());

    }

    @Test
    public void testInheritedValuesFromTemplate() {

        String plan1Name = "A freakin overridden name!";

        PedagogicalPlanTemplateImpl template = createPedagogicalPlanTemplate("A template with a bunch of plans");
        assertNull(template.getId());

        PedagogicalPlanImpl plan1 = (PedagogicalPlanImpl) createPedagogicalPlan(template);
        PedagogicalPlanImpl plan2 = (PedagogicalPlanImpl) createPedagogicalPlan(template);
        PedagogicalPlanImpl plan3 = (PedagogicalPlanImpl) createPedagogicalPlan(template);

        plan1.setName(plan1Name);

        getPedagogicalPlanPersistenceDAO().save(template);
        assertNotNull(template.getId());

        assertNotNull(plan1.getId());
        assertNotNull(plan2.getId());
        assertNotNull(plan3.getId());

        assertTrue(plan1.getTemplate() != null);
        assertTrue(plan2.getTemplate() != null);
        assertTrue(plan3.getTemplate() != null);

        assertTrue(plan1.getName().equals(plan1Name));
        assertTrue(plan2.getName().equals(plan2.getTemplate().getName()));
        assertTrue(plan3.getName().equals(plan3.getTemplate().getName()));

    }

    @Test
    public void testStoringTools() {
        ToolImpl tool = createTool("SCYMapper");
        getPedagogicalPlanPersistenceDAO().save(tool);
        assertNotNull(tool.getId());
    }

    private ToolImpl createTool(String name) {
        ToolImpl tool = new ToolImpl();
        tool.setName(name);
        tool.setDescription("A concptmapping tool");
        return tool;
    }

    @Test
    public void testAddToolConfigurationToActivity() {
        LearningActivitySpaceToolConfigurationImpl config = new LearningActivitySpaceToolConfigurationImpl();
        ToolImpl mapper = createTool("SCYMapper");
        assertNull(mapper.getId());
        config.setTool(mapper);
        assertNotNull(config.getTool());
        getPedagogicalPlanPersistenceDAO().save(config);
        assertNotNull(config.getId());
        assertNotNull(mapper.getId());
    }

    @Test
    public void testStoringAssessments() {
        AssessmentImpl assessment = (AssessmentImpl) createAssessment("Default Assessment");
        assertNotNull(assessment);
        assessment.setAssessmentStrategy(createAssessmentStrategy());
        getPedagogicalPlanPersistenceDAO().save(assessment);
        assertNotNull(assessment.getId());
        assertNotNull(((AssessmentStrategyImpl)assessment.getAssessmentStrategy()).getId());

    }

    private AssessmentStrategy createAssessmentStrategy() {
        PeerToPeerAssessmentStrategy strategy = new PeerToPeerAssessmentStrategyImpl();
        return strategy;
    }

    private Assessment createAssessment(String name) {
        Assessment assessment = new AssessmentImpl();
        assessment.setName(name);
        return assessment;
    }


    private PedagogicalPlanTemplateImpl createPedagogicalPlanTemplate(String name) {
        PedagogicalPlanTemplateImpl template1 = new PedagogicalPlanTemplateImpl();
        template1.setName(name);
        return template1;
    }

    private PedagogicalPlan createPedagogicalPlan(PedagogicalPlanTemplate template) {
        PedagogicalPlan plan = getPedagogicalPlanPersistenceDAO().createPedagogicalPlan(template);
        template.addPedagogicalPlan(plan);
        return plan;
    }

}
