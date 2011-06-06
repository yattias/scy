package eu.scy.server.controllers.scyfeedback;

import eu.scy.core.model.AssessmentCriteria;
import eu.scy.core.model.FileRef;
import eu.scy.core.model.pedagogicalplan.Assessment;
import eu.scy.core.model.pedagogicalplan.AssessmentCriteriaExperience;
import eu.scy.core.model.pedagogicalplan.AssessmentScoreDefinition;

import java.util.List;

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

    public String getScore() {
        if(getAssessmentCriteriaExperience() != null) {
            if(getAssessmentCriteriaExperience().getScore() != null){
                return getAssessmentCriteriaExperience().getScore().toString();
            }
        }
        return "";
    }

    public String getComment() {
        if(getAssessmentCriteriaExperience() != null) {
            if(getAssessmentCriteriaExperience().getComment() != null) {
                return getAssessmentCriteriaExperience().getComment();
            }
        }
        return "";
    }

    public FileRef getFileRef()  {
        if(getAssessmentCriteriaExperience() != null) {
            Assessment assessment = getAssessmentCriteriaExperience().getAssessmentCriteria().getAssessment();
            List scoreDefs = assessment.getAssessmentScoreDefinitions();
            if(getAssessmentCriteriaExperience().getScore() != null) {
                for (int i = 0; i < scoreDefs.size(); i++) {
                    AssessmentScoreDefinition assessmentScoreDefinition = (AssessmentScoreDefinition) scoreDefs.get(i);
                    if(assessmentScoreDefinition.getScore().equals(getAssessmentCriteriaExperience().getScore())) return  assessmentScoreDefinition.getFileRef();
                }

            }
        }

        return null;
    }
}
