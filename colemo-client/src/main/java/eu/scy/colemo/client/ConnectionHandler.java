package eu.scy.colemo.client;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.des.2008
 * Time: 10:19:36
 * To change this template use File | Settings | File Templates.
 */
public interface ConnectionHandler {


    void sendMessage(String message);

    void updateObject(Object object);

    void sendObject(Object object);

    void initialize() throws Exception;

    void cleanUp();

    void joinSession(String sessionId);
}
