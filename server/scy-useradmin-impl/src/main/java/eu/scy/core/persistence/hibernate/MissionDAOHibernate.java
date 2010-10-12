package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.pedagogicalplan.Mission;
import eu.scy.core.persistence.MissionDAO;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.jan.2010
 * Time: 12:11:11
 * To change this template use File | Settings | File Templates.
 */
public class MissionDAOHibernate extends ScyBaseDAOHibernate implements MissionDAO {
    @Override
    public Mission getMission(String id) {
        return (Mission) getSession().createQuery("from MissionImpl where id like :id")
                .setString("id", id)
                .uniqueResult();
    }
}
