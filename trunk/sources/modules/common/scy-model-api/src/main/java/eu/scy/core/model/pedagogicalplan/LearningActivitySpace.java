package eu.scy.core.model.pedagogicalplan;

import java.util.Set;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 11:20:42
 * To change this template use File | Settings | File Templates.
 */
public interface LearningActivitySpace extends LearningActivitySpaceBase, Assessable {

    public LearningActivitySpaceBase getLearningActivitySpaceTemplate();
    public void setLearningActivitySpaceTemplate(LearningActivitySpaceBase learningActivitiSpaceTemplate);

    public Set<AnchorELO> getProduces();
    public void setProduces(Set <AnchorELO> anchorELOs);

    public List <Activity> getActivities();
    public void setActivities(List <Activity> activities );

    public Set <LearningActivitySpaceScaffoldConfiguration> getLearningActivitySpaceScaffoldConfigurations();
    public void setLearninigActivitySpaceScaffoldConfigurations(Set <LearningActivitySpaceScaffoldConfiguration> learninigActivitySpaceScaffoldConfigurations);

}
