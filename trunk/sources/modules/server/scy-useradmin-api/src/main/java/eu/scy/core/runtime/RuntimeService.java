package eu.scy.core.runtime;

import eu.scy.core.BaseService;
import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.Tool;
import eu.scy.core.model.runtime.AbstractRuntimeAction;
import info.collide.sqlspaces.commons.admin.ClientActivityMonitor;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.apr.2010
 * Time: 06:12:12
 * To change this template use File | Settings | File Templates.
 */
public interface RuntimeService extends BaseService {

    List getActions(User user);

    void storeAction(String type, String id, long timeInMillis, String tool, String mission, String session, String eloUri, String user, String oldLASId, String newLASId);

    AbstractRuntimeAction getLatestInterestingAction(User user);

    public String getCurrentTool(User user);

    public String getCurrentELO(User user);

    public String getCurrentLAS(User user);

    public List getLastELOs(User user);

    public List getUsersCurrentlyInLAS(String lasId);
}
