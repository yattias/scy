package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.persistence.AnchorELODAO;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 21:34:30
 * To change this template use File | Settings | File Templates.
 */
public class AnchorELODAOHibernate extends ScyBaseDAOHibernate implements AnchorELODAO {
    @Override
    public AnchorELO getAnchorELO(String anchorEloId) {
        return (AnchorELO) getSession().createQuery("from AnchorELOImpl where id like :id")
                .setString("id", anchorEloId)
                .uniqueResult();
    }
}
