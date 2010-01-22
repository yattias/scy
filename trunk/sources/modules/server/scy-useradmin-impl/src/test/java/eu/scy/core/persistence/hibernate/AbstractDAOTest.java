package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.AssessmentImpl;
import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceImpl;
import eu.scy.core.model.impl.pedagogicalplan.PeerToPeerAssessmentStrategyImpl;
import eu.scy.core.model.impl.pedagogicalplan.ScenarioImpl;
import eu.scy.core.model.pedagogicalplan.*;
import org.springframework.test.AbstractTransactionalSpringContextTests;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2009
 * Time: 05:55:18
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractDAOTest extends AbstractTransactionalSpringContextTests {
    protected String[] getConfigLocations() {
        return new String[]{"classpath:/eu/scy/core/persistence/hibernate/applciationContext-hibernate-OnlyForTesting.xml"};
    }

    protected Scenario createScenario(String name) {
        ScenarioImpl scenario = new ScenarioImpl();
        scenario.setName(name);
        return scenario;
    }

    protected LearningActivitySpace createLAS(String name) {
        Scenario scenario = createScenario("Scenario for " + name);

        LearningActivitySpace las = new LearningActivitySpaceImpl();
        las.setName(name);
        las.setParticipatesIn(scenario);
        return las;
    }

    protected Assessment createAssessment(String name) {
        Assessment assessment = new AssessmentImpl();
        assessment.setName(name);
        assessment.setAssessmentStrategy(createAssessmentStrategy());
        return assessment;
    }

    protected AssessmentStrategy createAssessmentStrategy() {
        PeerToPeerAssessmentStrategy strategy = new PeerToPeerAssessmentStrategyImpl();
        return strategy;
    }
}
