package eu.scy.core.persistence;

import eu.scy.core.model.User;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.apr.2010
 * Time: 06:12:50
 * To change this template use File | Settings | File Templates.
 */
public interface RuntimeDAO extends SCYBaseDAO{
    void storeAction(String type, String id, long timeInMillis, String tool, String mission, String session, String eloUri, String userName);

    List getActions(User user);
}
