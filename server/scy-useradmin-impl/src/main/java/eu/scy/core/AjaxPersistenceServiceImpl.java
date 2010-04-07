package eu.scy.core;

import eu.scy.core.AjaxPersistenceService;
import eu.scy.core.BaseServiceImpl;
import eu.scy.core.model.ScyBase;
import eu.scy.core.persistence.AjaxPersistenceDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mar.2010
 * Time: 06:15:11
 * To change this template use File | Settings | File Templates.
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
    public ScyBase get(Class c, String id) {
        return ajaxPersistenceDAO.get(c, id);
    }

    @Override
    @Transactional
    public void save(ScyBase scyBaseObject) {
        super.save(scyBaseObject);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
