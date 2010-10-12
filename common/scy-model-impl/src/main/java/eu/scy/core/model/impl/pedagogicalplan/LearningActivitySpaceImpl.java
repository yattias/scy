package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.FileRef;
import eu.scy.core.model.impl.FileRefImpl;
import eu.scy.core.model.pedagogicalplan.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.Set;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 28.sep.2009
 * Time: 15:46:03
 */
@Entity
@Table(name="learningactivityspace")
public class LearningActivitySpaceImpl extends LearningActivitySpaceBaseImpl implements LearningActivitySpace, Assessable {

    private Scenario participatesIn;

    private Assessment assessment = null;
    private LearningActivitySpaceTemplate learningActivitySpaceTemplate = null;

    private List activities;

    private Integer xPos;
    private Integer yPos;

    private FileRef image;

    @OneToOne(targetEntity = AssessmentImpl.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn (name = "assessment_primKey")
    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    @Transient
    public LearningActivitySpaceTemplate getLearningActivitySpaceTemplate() {
        return learningActivitySpaceTemplate;
    }

    public void setLearningActivitySpaceTemplate(LearningActivitySpaceTemplate learningActivitySpaceTemplate) {
        this.learningActivitySpaceTemplate = learningActivitySpaceTemplate;
    }


    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "learningActivitySpace", targetEntity = ActivityImpl.class, fetch = FetchType.LAZY)
    public List<Activity> getActivities() {
        if(activities == null) {
            activities = new LinkedList();
        }
        return activities;
    }

    public void addActivity(Activity activity) {
        getActivities().add(activity);
        activity.setLearningActivitySpace(this);
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;

    }

    @Transient
    public Set<LearningActivitySpaceScaffoldConfiguration> getLearningActivitySpaceScaffoldConfigurations() {
        return null;
    }

    @Override
    public void setLearninigActivitySpaceScaffoldConfigurations(Set<LearningActivitySpaceScaffoldConfiguration> learninigActivitySpaceScaffoldConfigurations) {

    }

    public Integer getXPos() {
        return xPos;
    }

    public void setXPos(Integer xPos) {
        this.xPos = xPos;
    }

    public Integer getYPos() {
        return yPos;
    }

    public void setYPos(Integer yPos) {
        this.yPos = yPos;
    }

    @Override
    @ManyToOne(targetEntity = ScenarioImpl.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "participatesInScenario_primKey")
    public Scenario getParticipatesIn() {
        return participatesIn;
    }

    @Override
    public void setParticipatesIn(Scenario participatesIn) {
        this.participatesIn = participatesIn;
    }

    @Override
    @OneToOne(targetEntity = FileRefImpl.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_primKey")
    public FileRef getImage() {
        return image;
    }

    @Override
    public void setImage(FileRef image) {
        this.image = image;
    }
}
