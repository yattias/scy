package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.*;

import java.util.Set;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 28.sep.2009
 * Time: 15:46:03
 */
public class LearningActivitySpaceImpl extends LearningActivitySpaceBaseImpl implements LearningActivitySpace, Assessable {
    private Assessment assessment = null;
    private LearningActivitySpaceTemplate learningActivitySpaceTemplate = null;

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public LearningActivitySpaceTemplate getLearningActivitySpaceTemplate() {
        return learningActivitySpaceTemplate;
    }

    public void setLearningActivitySpaceTemplate(LearningActivitySpaceTemplate learningActivitySpaceTemplate) {
        this.learningActivitySpaceTemplate = learningActivitySpaceTemplate;
    }

    @Override
    public Set<AnchorELO> getProduces() {
        return null;
    }

    @Override
    public void setProduces(Set<AnchorELO> anchorELOs) {

    }

    @Override
    public List<Activity> getActivities() {
        return null;
    }

    @Override
    public void setActivities(List<Activity> activities) {

    }

    @Override
    public Set<LearningActivitySpaceScaffoldConfiguration> getLearningActivitySpaceScaffoldConfigurations() {
        return null;
    }

    @Override
    public void setLearninigActivitySpaceScaffoldConfigurations(Set<LearningActivitySpaceScaffoldConfiguration> learninigActivitySpaceScaffoldConfigurations) {

    }
}
