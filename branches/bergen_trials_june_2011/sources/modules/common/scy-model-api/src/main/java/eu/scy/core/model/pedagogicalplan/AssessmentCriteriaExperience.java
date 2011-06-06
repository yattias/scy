package eu.scy.core.model.pedagogicalplan;

import eu.scy.core.model.AssessmentCriteria;
import eu.scy.core.model.User;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.jul.2010
 * Time: 07:36:14
 * To change this template use File | Settings | File Templates.
 */
public interface AssessmentCriteriaExperience extends BaseObject{
    Integer getScore();

    void setScore(Integer score);

    AssessmentCriteria getAssessmentCriteria();

    void setAssessmentCriteria(AssessmentCriteria assessmentCriteria);

    User getUser();

    void setUser(User user);

    String getCriteriaText();

    void setCriteriaText(String criteriaText);

    String getComment();

    void setComment(String comment);
}
