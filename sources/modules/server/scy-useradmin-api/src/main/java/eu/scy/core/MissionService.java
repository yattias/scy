package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.BaseObject;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.jan.2010
 * Time: 12:09:46
 * To change this template use File | Settings | File Templates.
 */
public interface MissionService extends BaseService{

    void save(BaseObject baseObject);
}
