package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.Assessment;
import eu.scy.core.persistence.AssessmentDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.jan.2010
 * Time: 05:43:32
 * To change this template use File | Settings | File Templates.
 */
public class AssessmentServiceImpl extends BaseServiceImpl implements AssessmentService{

    private AssessmentDAO assessmentDAO;

    public AssessmentDAO getAssessmentDAO() {
        return (AssessmentDAO) getScyBaseDAO();
    }

    public void setAssessmentDAO(AssessmentDAO assessmentDAO) {
        this.assessmentDAO = assessmentDAO;
    }

    @Override
    public Assessment findAssessmentByName(String name) {
        return getAssessmentDAO().findAssessmentByName(name);
    }

    @Override
    @Transactional
    public void addCriteria(Assessment assessment) {
        getAssessmentDAO().addCriteria(assessment);
    }

    @Override
    @Transactional
    public void addAssessment(AnchorELO anchorELO) {
        getAssessmentDAO().addAssessment(anchorELO);
    }

    @Override
    @Transactional
    public void addScoreDefinition(Assessment assessment) {
        getAssessmentDAO().addScoreDefinition(assessment);
    }
}
