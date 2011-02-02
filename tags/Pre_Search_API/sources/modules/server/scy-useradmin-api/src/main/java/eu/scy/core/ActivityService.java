package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.Activity;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 18:39:03
 * To change this template use File | Settings | File Templates.
 */
public interface ActivityService extends BaseService{
    Activity getActivity(String activityId);
}
