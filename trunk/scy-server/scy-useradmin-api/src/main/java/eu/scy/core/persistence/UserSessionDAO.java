package eu.scy.core.persistence;

import eu.scy.core.model.User;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 21.okt.2008
 * Time: 07:58:29
 * A DAO to manage all user sessions created by any kind of login (web-client-service etc)
 */
public interface UserSessionDAO extends SCYBaseDAO {

    public void loginUser(User user);

}
