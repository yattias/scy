package eu.scy.core.persistence;

import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.Tool;
import eu.scy.core.model.runtime.AbstractRuntimeAction;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.apr.2010
 * Time: 06:12:50
 * To change this template use File | Settings | File Templates.
 */
public interface RuntimeDAO extends SCYBaseDAO{
    void storeAction(String type, String id, long timeInMillis, String tool, String mission, String session, String eloUri, String userName, String newLASId, String oldLASId);

    List getActions(User user);

    AbstractRuntimeAction getLatestInterestingAction(User user);

    String getCurrentTool(User user);

    String getCurrentELO(User user);

    String getCurrentLAS(User user);

    List getLastELOs(User user);

    List getUsersCurrentlyinLAS(String lasId);
}
