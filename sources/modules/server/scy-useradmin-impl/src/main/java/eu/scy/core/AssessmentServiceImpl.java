package eu.scy.core;

import eu.scy.core.model.AssessmentCriteria;
import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.Assessment;
import eu.scy.core.model.pedagogicalplan.AssessmentCriteriaExperience;
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


    public Assessment findAssessmentByName(String name) {
        return getAssessmentDAO().findAssessmentByName(name);
    }


    @Transactional
    public void addCriteria(Assessment assessment) {
        getAssessmentDAO().addCriteria(assessment);
    }


    @Transactional
    public void addAssessment(AnchorELO anchorELO) {
        getAssessmentDAO().addAssessment(anchorELO);
    }


    @Transactional
    public void addScoreDefinition(Assessment assessment) {
        getAssessmentDAO().addScoreDefinition(assessment);
    }


    public AssessmentCriteria getAssessmentCriteria(String parameter) {
        return getAssessmentDAO().getAssessmentCriteria(parameter);
    }


    public AssessmentCriteriaExperience getAssessmentCriteriaExperience(User user, AssessmentCriteria criteria) {
        return getAssessmentDAO().getAssessmentCriteriaExperience(user, criteria);
    }


    @Transactional
    public void createOrUpdateAssessmentCriteriaExperience(User user, AssessmentCriteria criteria, String criteriaText, int score, String comment) {
        getAssessmentDAO().createOrUpdateAssessmentCriteriaExperience(user, criteria, criteriaText, score, comment);    
    }
}
