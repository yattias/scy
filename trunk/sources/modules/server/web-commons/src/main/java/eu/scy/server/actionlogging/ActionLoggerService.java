package eu.scy.server.actionlogging;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 04.aug.2011
 * Time: 05:56:57
 * To change this template use File | Settings | File Templates.
 */
public interface ActionLoggerService {

    void logAction(String type, String userName, String tool);
    
}
