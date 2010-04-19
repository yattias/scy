package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.*;

import javax.persistence.*;
import java.util.HashSet;
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

    private int xPos;
    private int yPos;

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

    public int getXPos() {
        return xPos;
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public void setYPos(int yPos) {
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
}
