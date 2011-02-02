package eu.scy.core.service.impl;

import eu.scy.core.service.UserSessionService;

import javax.servlet.http.HttpSession;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 08.okt.2008
 * Time: 22:21:27
 * To change this template use File | Settings | File Templates.
 */
public class UserSessionServiceImpl implements UserSessionService {


    public void logoutUser(HttpSession session) {
        session.invalidate();
    }



}
