package eu.scy.core.model.pedagogicalplan;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 12:44:52
 * To change this template use File | Settings | File Templates.
 */
public interface LearningActivitySpaceToolConfiguration extends LearningActivitySpaceArtifactConfiguration{

    public void setTool (Tool tool);
    public Tool getTool();


    Activity getActivity();

    void setActivity(Activity activity);
}
