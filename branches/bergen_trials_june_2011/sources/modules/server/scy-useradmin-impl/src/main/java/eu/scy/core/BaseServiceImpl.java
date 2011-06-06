package eu.scy.core;

import eu.scy.core.model.ScyBase;
import eu.scy.core.persistence.SCYBaseDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.jan.2010
 * Time: 05:43:45
 * To change this template use File | Settings | File Templates.
 */
public class BaseServiceImpl implements BaseService{

    private SCYBaseDAO scyBaseDAO;

    public SCYBaseDAO getScyBaseDAO() {
        return scyBaseDAO;
    }

    public void setScyBaseDAO(SCYBaseDAO scyBaseDAO) {
        this.scyBaseDAO = scyBaseDAO;
    }

    @Transactional
    public void save(Object scyBaseObject) {
        getScyBaseDAO().save(scyBaseObject);
    }

    @Override
    public ScyBase get(String id, Class type) {
        return (ScyBase) getScyBaseDAO().getObject(type, id);
    }
}
