package eu.scy.core.service;

import javax.servlet.http.HttpSession;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 08.okt.2008
 * Time: 22:21:13
 * To change this template use File | Settings | File Templates.
 */
public interface UserSessionService {
    void logoutUser(HttpSession session);
}
