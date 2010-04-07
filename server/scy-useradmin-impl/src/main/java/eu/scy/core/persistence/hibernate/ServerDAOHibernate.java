package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.Server;
import eu.scy.core.model.impl.ServerImpl;
import eu.scy.core.persistence.ServerDAO;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.feb.2010
 * Time: 12:29:38
 * To change this template use File | Settings | File Templates.
 */
public class ServerDAOHibernate extends ScyBaseDAOHibernate implements ServerDAO {
    @Override
    public Server getServer() {
        Server server = getDefaultServer();
        if(server ==null) {
            logger.info("Creating new Server instance");
            server = new ServerImpl();
            save(server);
        }

        return server;
    }

    private Server getDefaultServer() {
        return (Server) getSession().createQuery("from ServerImpl")
                .setMaxResults(1)
                .uniqueResult();

    }
}
