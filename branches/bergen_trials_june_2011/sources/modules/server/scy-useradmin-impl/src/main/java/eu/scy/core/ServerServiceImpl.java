package eu.scy.core;

import eu.scy.core.model.Server;
import eu.scy.core.persistence.ServerDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.feb.2010
 * Time: 12:29:14
 * To change this template use File | Settings | File Templates.
 */
public class ServerServiceImpl extends BaseServiceImpl implements ServerService{
    
    private ServerDAO serverDAO;

    public ServerDAO getServerDAO() {
        return serverDAO;
    }

    public void setServerDAO(ServerDAO serverDAO) {
        this.serverDAO = serverDAO;
    }

    @Override
    @Transactional
    public Server getServer() {
        return serverDAO.getServer();
    }
}
