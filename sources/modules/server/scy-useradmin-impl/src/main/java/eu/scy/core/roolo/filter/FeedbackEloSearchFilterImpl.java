package eu.scy.core.roolo.filter;

import eu.scy.core.roolo.FeedbackEloSearchFilter;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 06.sep.2011
 * Time: 05:42:04
 * To change this template use File | Settings | File Templates.
 */
public class FeedbackEloSearchFilterImpl implements FeedbackEloSearchFilter {

    private String owner;
    private String criteria;
    private String category;

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String getCriteria() {
        return criteria;
    }

    @Override
    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }
}
