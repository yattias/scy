package eu.scy.core.model.pedagogicalplan;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 14:26:12
 * To change this template use File | Settings | File Templates.
 */
public interface LearningActivitySpaceScaffoldConfiguration extends LearningActivitySpaceArtifactConfiguration{

    public void setScaffold(Scaffold scaffold);
    public Scaffold getScaffold();

}
