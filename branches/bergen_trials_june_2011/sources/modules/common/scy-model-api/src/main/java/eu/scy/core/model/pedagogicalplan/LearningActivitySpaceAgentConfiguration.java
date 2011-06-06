package eu.scy.core.model.pedagogicalplan;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 12:48:14
 * To change this template use File | Settings | File Templates.
 */

public interface LearningActivitySpaceAgentConfiguration extends LearningActivitySpaceArtifactConfiguration{


    Agent getAgent();

    void setAgent(Agent agent);
}
