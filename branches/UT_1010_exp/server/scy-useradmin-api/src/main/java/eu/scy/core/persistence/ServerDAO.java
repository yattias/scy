package eu.scy.core.persistence;

import eu.scy.core.model.Server;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.feb.2010
 * Time: 12:28:50
 * To change this template use File | Settings | File Templates.
 */
public interface ServerDAO extends BaseDAO{
    public Server getServer();
}
