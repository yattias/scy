package eu.scy.core.model.pedagogicalplan;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 13:37:56
 * To change this template use File | Settings | File Templates.
 */
public interface Activity extends BaseObject{


    /**
     * The anchor elo that will be produced by this activity
     * @param anchorELO
     */
    void setAnchorELO(AnchorELO anchorELO);
    AnchorELO getAnchorELO();
    
    public LearningActivitySpace getLearningActivitySpace();
    public void setLearningActivitySpace(LearningActivitySpace learningActivitySpace);


    /**
     * Provides access to tools configured for this activity
     * @return
     */
    public Set<LearningActivitySpaceToolConfiguration> getLearningActivitySpaceToolConfigurations();
    public void setLearningActivitySpaceToolConfiguration(LearningActivitySpaceToolConfiguration learningActivitySpaceToolConfiguration);




}
