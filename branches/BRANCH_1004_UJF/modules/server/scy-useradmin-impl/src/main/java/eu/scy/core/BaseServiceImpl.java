package eu.scy.core;

import eu.scy.core.model.ScyBase;
import eu.scy.core.persistence.BaseDAO;
import eu.scy.core.persistence.SCYBaseDAO;

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

    @Override
    public void save(ScyBase scyBaseObject) {
        getScyBaseDAO().save(scyBaseObject);
    }
}
