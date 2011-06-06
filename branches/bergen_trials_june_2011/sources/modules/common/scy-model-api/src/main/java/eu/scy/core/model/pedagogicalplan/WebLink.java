package eu.scy.core.model.pedagogicalplan;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 12:25:53
 * represents a web link
 */
public interface WebLink extends LearningMaterial{
    String getUrl();

    void setUrl(String url);

}