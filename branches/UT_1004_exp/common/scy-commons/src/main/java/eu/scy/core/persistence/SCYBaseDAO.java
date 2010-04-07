package eu.scy.core.persistence;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:36:54
 * To change this template use File | Settings | File Templates.
 */
public interface SCYBaseDAO {

    public Object getObject(Class clazz, String id);

    public Object save(Object object);

}
