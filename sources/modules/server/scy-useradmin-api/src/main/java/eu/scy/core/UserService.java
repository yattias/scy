package eu.scy.core;

import eu.scy.core.model.User;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.des.2009
 * Time: 10:25:49
 * To change this template use File | Settings | File Templates.
 */
public interface UserService {

    public User getUser(String username);


    User createUser(String username, String password);
}
