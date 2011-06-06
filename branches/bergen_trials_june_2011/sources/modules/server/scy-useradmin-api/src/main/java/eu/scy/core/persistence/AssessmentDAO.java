package eu.scy.core.persistence;

import eu.scy.core.model.AssessmentCriteria;
import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.Assessment;
import eu.scy.core.model.pedagogicalplan.AssessmentCriteriaExperience;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.jan.2010
 * Time: 05:45:08
 * To change this template use File | Settings | File Templates.
 */
public interface AssessmentDAO extends SCYBaseDAO{
    Assessment findAssessmentByName(String s);

    void addCriteria(Assessment assessment);

    void addAssessment(AnchorELO anchorELO);

    void addScoreDefinition(Assessment assessment);

    AssessmentCriteria getAssessmentCriteria(String parameter);

    public void createOrUpdateAssessmentCriteriaExperience(User user, AssessmentCriteria criteria, String criteriaText, int score, String comment);

    AssessmentCriteriaExperience getAssessmentCriteriaExperience(User user, AssessmentCriteria criteria);
}
