package eu.scy.core;

import eu.scy.core.model.AssessmentCriteria;
import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.Assessment;
import eu.scy.core.model.pedagogicalplan.AssessmentCriteriaExperience;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.jan.2010
 * Time: 05:41:18
 * To change this template use File | Settings | File Templates.
 */
public interface AssessmentService extends BaseService{

    public Assessment findAssessmentByName(String name);

    void addCriteria(Assessment assessment);

    void addAssessment(AnchorELO anchorELO);

    void addScoreDefinition(Assessment assessment);

    AssessmentCriteria getAssessmentCriteria(String parameter);

    AssessmentCriteriaExperience getAssessmentCriteriaExperience(User user, AssessmentCriteria criteria);

    void createOrUpdateAssessmentCriteriaExperience(User user, AssessmentCriteria criteria, String criteriaText, int score, String comment);
}
