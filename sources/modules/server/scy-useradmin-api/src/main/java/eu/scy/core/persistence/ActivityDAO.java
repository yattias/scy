package eu.scy.core.persistence;

import eu.scy.core.model.pedagogicalplan.Activity;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 18:39:23
 * To change this template use File | Settings | File Templates.
 */
public interface ActivityDAO extends BaseDAO{
    Activity getActivity(String activityId);
}
