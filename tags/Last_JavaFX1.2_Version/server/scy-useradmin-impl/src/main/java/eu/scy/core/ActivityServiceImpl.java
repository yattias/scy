package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.persistence.ActivityDAO;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 18:40:45
 * To change this template use File | Settings | File Templates.
 */
public class ActivityServiceImpl extends BaseServiceImpl implements ActivityService{

    private ActivityDAO activityDAO;

    public ActivityDAO getActivityDAO() {
        return activityDAO;
    }

    public void setActivityDAO(ActivityDAO activityDAO) {
        this.activityDAO = activityDAO;
    }

    @Override
    public Activity getActivity(String activityId) {
        return activityDAO.getActivity(activityId);
    }
}
