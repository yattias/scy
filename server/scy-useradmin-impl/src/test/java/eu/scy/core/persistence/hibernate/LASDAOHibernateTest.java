package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.*;
import eu.scy.core.model.pedagogicalplan.Assessment;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.persistence.LASDAO;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.jan.2010
 * Time: 11:35:57
 * To change this template use File | Settings | File Templates.
 */
public class LASDAOHibernateTest extends AbstractDAOTest{

    private LASDAO lasdao;

    public LASDAO getLasdao() {
        return lasdao;
    }

    public void setLasdao(LASDAO lasdao) {
        this.lasdao = lasdao;
    }

    @Test
    public void testSetup() {
        assertNotNull(getLasdao());
    }

    @Test
    public void testAddToolToLAS() {
        ToolImpl tool = new ToolImpl();

        LearningActivitySpaceImpl las = (LearningActivitySpaceImpl) createLAS("A LAS");
        lasdao.addToolToLAS(tool, las);
        assertNotNull(tool.getId());
        assertNotNull(las.getId());
    }

    @Test
    public void testAddAssessmentToLAS() {
        LearningActivitySpaceImpl las = (LearningActivitySpaceImpl) createLAS("LAS with assessmnet");
        AssessmentImpl assessment = (AssessmentImpl) createAssessment(las.getName());
        las.setAssessment(assessment);
        assertNull(assessment.getId());
        lasdao.save(las);
        assertNotNull(las.getId());
        assertNotNull(assessment.getId());

    }

}
