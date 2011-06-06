package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.persistence.ActivityDAO;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 18:40:09
 * To change this template use File | Settings | File Templates.
 */
public class ActivityDAOHibernate extends ScyBaseDAOHibernate implements ActivityDAO {
   
    public Activity getActivity(String activityId) {
        return (Activity) getSession().createQuery("from ActivityImpl where id = :id")
                .setString("id", activityId)
                .uniqueResult();
    }
}
