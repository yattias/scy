package eu.scy.core;

import eu.scy.core.model.ScyBase;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mar.2010
 * Time: 06:12:09
 * To change this template use File | Settings | File Templates.
 */
public interface AjaxPersistenceService extends BaseService{
    Object get(Class c, String id);

    @Override
    void save(Object scyBaseObject);
}
