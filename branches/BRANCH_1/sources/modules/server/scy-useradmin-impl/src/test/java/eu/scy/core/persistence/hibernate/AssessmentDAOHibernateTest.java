package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.AssessmentImpl;
import eu.scy.core.model.pedagogicalplan.Assessment;
import eu.scy.core.persistence.AssessmentDAO;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.jan.2010
 * Time: 05:49:10
 * To change this template use File | Settings | File Templates.
 */
public class AssessmentDAOHibernateTest extends AbstractDAOTest{

    private AssessmentDAO assessmentDAO;

    public AssessmentDAO getAssessmentDAO() {
        return assessmentDAO;
    }

    public void setAssessmentDAO(AssessmentDAO assessmentDAO) {
        this.assessmentDAO = assessmentDAO;
    }

    @Test
    public void testSetup() {
        assertNotNull(getAssessmentDAO());
    }

    @Test
    public void testSaveAssessment() {
        AssessmentImpl assessment = (AssessmentImpl) createAssessment("A test");
        assertNull(assessment.getId());
        getAssessmentDAO().save(assessment);
        assertNotNull(assessment.getId());
    }

    public void testFindAssessmentByName() {
        Assessment a = createAssessment("A");
        a.setDescription("THE A WAY");
        getAssessmentDAO().save(a);

        Assessment b = createAssessment("B");
        b.setDescription("THE B WAY");
        getAssessmentDAO().save(b);

        Assessment loaded = getAssessmentDAO().findAssessmentByName("A");
    }
}
