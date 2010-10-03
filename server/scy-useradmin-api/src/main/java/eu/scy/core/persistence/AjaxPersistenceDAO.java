package eu.scy.core.persistence;

import eu.scy.core.model.ScyBase;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mar.2010
 * Time: 06:12:58
 * To change this template use File | Settings | File Templates.
 */
public interface AjaxPersistenceDAO extends SCYBaseDAO{
    Object get(Class c, String id);
}
