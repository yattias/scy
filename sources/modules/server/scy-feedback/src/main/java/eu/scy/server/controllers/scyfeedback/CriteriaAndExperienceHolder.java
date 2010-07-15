package eu.scy.server.controllers.scyfeedback;

import eu.scy.core.model.AssessmentCriteria;
import eu.scy.core.model.pedagogicalplan.AssessmentCriteriaExperience;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.jul.2010
 * Time: 08:33:16
 * To change this template use File | Settings | File Templates.
 */
public class CriteriaAndExperienceHolder {

    private AssessmentCriteria assessmentCriteria;
    private AssessmentCriteriaExperience assessmentCriteriaExperience;

    public AssessmentCriteria getCriteria() {
        return assessmentCriteria;
    }

    public void setCriteria(AssessmentCriteria assessmentCriteria) {
        this.assessmentCriteria = assessmentCriteria;
    }

    public AssessmentCriteriaExperience getAssessmentCriteriaExperience() {
        return assessmentCriteriaExperience;
    }

    public void setAssessmentCriteriaExperience(AssessmentCriteriaExperience assessmentCriteriaExperience) {
        this.assessmentCriteriaExperience = assessmentCriteriaExperience;
    }

    public String getCriteriaText() {
        if(getAssessmentCriteriaExperience() != null) {
            if(getAssessmentCriteriaExperience().getCriteriaText() != null) return getAssessmentCriteriaExperience().getCriteriaText();
        }
        return getCriteria().getCriteria();
    }
}
