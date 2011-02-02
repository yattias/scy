package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.FileRef;
import eu.scy.core.model.impl.FileRefImpl;
import eu.scy.core.model.pedagogicalplan.Assessment;
import eu.scy.core.model.pedagogicalplan.AssessmentScoreDefinition;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 13.jul.2010
 * Time: 07:49:05
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table (name = "assessmentscoredefinition")
public class AssessmentScoreDefinitionImpl extends BaseObjectImpl implements AssessmentScoreDefinition{

    private Assessment assessment;
    private String heading;
    private Integer score;
    private FileRef fileRef;

    @Override
    @ManyToOne(targetEntity = AssessmentImpl.class)
    @JoinColumn (name = "assessment_primKey")
    public Assessment getAssessment() {
        return assessment;
    }

    @Override
    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    @Override
    public String getHeading() {
        return heading;
    }

    @Override
    public void setHeading(String heading) {
        this.heading = heading;
    }

    @Override
    public Integer getScore() {
        return score;
    }

    @Override
    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    @ManyToOne (targetEntity = FileRefImpl.class)
    @JoinColumn (name = "fileRef_primKey")
    public FileRef getFileRef() {
        return fileRef;
    }

    @Override
    public void setFileRef(FileRef fileRef) {
        this.fileRef = fileRef;
    }
}
