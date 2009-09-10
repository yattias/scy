package eu.scy.core.model.pedagogicalplan;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 11:20:42
 * To change this template use File | Settings | File Templates.
 */
public interface LearningActivitySpace extends LearningActivitiSpaceTemplate{

    public LearningActivitiSpaceTemplate getLearningActivitiSpaceTemplate();
    public void setLearningActivitySpaceTemplate(LearningActivitiSpaceTemplate learningActivitiSpaceTemplate);

    public Set<AnchorELO> getProduces();
    public void setProduces(Set <AnchorELO> anchorELOs);

}
