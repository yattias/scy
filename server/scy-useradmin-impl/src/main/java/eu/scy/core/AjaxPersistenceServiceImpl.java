package eu.scy.core;

import eu.scy.core.model.ScyBase;
import eu.scy.core.persistence.AjaxPersistenceDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mar.2010
 * Time: 06:15:11
 */
public class AjaxPersistenceServiceImpl extends BaseServiceImpl implements AjaxPersistenceService {

    private AjaxPersistenceDAO ajaxPersistenceDAO;

    public AjaxPersistenceDAO getAjaxPersistenceDAO() {
        return ajaxPersistenceDAO;
    }

    public void setAjaxPersistenceDAO(AjaxPersistenceDAO ajaxPersistenceDAO) {
        super.setScyBaseDAO(ajaxPersistenceDAO);
        this.ajaxPersistenceDAO = ajaxPersistenceDAO;
    }

    @Override
    public Object get(Class c, String id) {
        return ajaxPersistenceDAO.get(c, id);
    }

    @Override
    @Transactional
    public void save(Object scyBaseObject) {
        super.save(scyBaseObject);
    }
}
