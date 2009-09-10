package eu.scy.core.model.pedagogicalplan;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 11:55:20
 * To change this template use File | Settings | File Templates.
 */
public interface LearningActivitySpaceBase extends BaseObject {

    public String getDescription();
    public void setDescription(String description);

    public int expectedDurationInMinutes();
    public void setExpectedDurationInMinutes(int expectedDurationInMinutes);

}
