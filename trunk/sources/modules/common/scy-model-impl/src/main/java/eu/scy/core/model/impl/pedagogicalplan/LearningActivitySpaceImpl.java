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

    private Assessment assessment = null;
    private LearningActivitySpaceTemplate learningActivitySpaceTemplate = null;

    private List activities;
    private Set produces;

    private int xPos;
    private int yPos;

    @Transient
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

    @Transient
    public Set<AnchorELO> getProduces() {
        return produces;
    }

    public void setProduces(Set<AnchorELO> anchorELOs) {
        this.produces = anchorELOs;
    }

    public void addAnchorELO(AnchorELO anchorELO) {
        if(produces == null) {
            produces = new HashSet();
        }
        produces.add(anchorELO);
    }

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "learningActivitySpace", targetEntity = ActivityImpl.class, fetch = FetchType.EAGER)
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
}
