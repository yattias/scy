package eu.scy.core.model.pedagogicalplan;

import eu.scy.core.model.FileRef;

import java.util.Set;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 11:20:42
 * To change this template use File | Settings | File Templates.
 */
public interface LearningActivitySpace extends LearningActivitySpaceBase, Assessable, PositionedObject {

    public LearningActivitySpaceTemplate getLearningActivitySpaceTemplate();
    public void setLearningActivitySpaceTemplate(LearningActivitySpaceTemplate learningActivitiSpaceTemplate);

    //public Set<AnchorELO> getProduces();
    //public void setProduces(Set <AnchorELO> anchorELOs);
    //public void addAnchorELO(AnchorELO anchorELO);


    public List <Activity> getActivities();
    public void setActivities(List <Activity> activities );


    public Set <LearningActivitySpaceScaffoldConfiguration> getLearningActivitySpaceScaffoldConfigurations();
    public void setLearninigActivitySpaceScaffoldConfigurations(Set <LearningActivitySpaceScaffoldConfiguration> learninigActivitySpaceScaffoldConfigurations);


    void addActivity(Activity activity);

    Scenario getParticipatesIn();

    void setParticipatesIn(Scenario participatesIn);

    FileRef getImage();

    void setImage(FileRef image);
}
