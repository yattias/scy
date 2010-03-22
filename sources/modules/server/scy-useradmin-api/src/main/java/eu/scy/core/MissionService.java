package eu.scy.core;

import eu.scy.core.model.ScyBase;
import eu.scy.core.model.pedagogicalplan.BaseObject;
import eu.scy.core.model.pedagogicalplan.Mission;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.jan.2010
 * Time: 12:09:46
 * To change this template use File | Settings | File Templates.
 */
public interface MissionService extends BaseService{

    public void save(BaseObject baseObject);

    public Mission getMission(String parameter);
}
