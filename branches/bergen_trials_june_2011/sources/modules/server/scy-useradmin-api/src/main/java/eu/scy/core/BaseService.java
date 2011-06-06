package eu.scy.core;

import eu.scy.core.model.ScyBase;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.jan.2010
 * Time: 05:41:41
 * To change this template use File | Settings | File Templates.
 */
public interface BaseService {

    void save(Object scyBaseObject);

    public ScyBase get(String id, Class type);

}
