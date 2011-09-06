package eu.scy.core.roolo;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 06.sep.2011
 * Time: 05:45:10
 * To change this template use File | Settings | File Templates.
 */
public interface FeedbackEloSearchFilter {
    String getOwner();

    void setOwner(String owner);

    String getCriteria();

    void setCriteria(String criteria);

    String getCategory();

    void setCategory(String category);
}
