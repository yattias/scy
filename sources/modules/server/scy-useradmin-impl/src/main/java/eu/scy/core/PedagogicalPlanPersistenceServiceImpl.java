package eu.scy.core;

import eu.scy.core.persistence.PedagogicalPlanPersistenceDAO;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2009
 * Time: 05:37:28
 * To change this template use File | Settings | File Templates.
 */
public class PedagogicalPlanPersistenceServiceImpl implements PedagogicalPlanPersistenceService {

    private static Logger log = Logger.getLogger("PedagogicalPlanPersistenceServiceImpl.class");

    private PedagogicalPlanPersistenceDAO pedagogicalPlanPersistenceDAO;

    public PedagogicalPlanPersistenceDAO getPedagogicalPlanPersistenceDAO() {
        return pedagogicalPlanPersistenceDAO;
    }

    public void setPedagogicalPlanPersistenceDAO(PedagogicalPlanPersistenceDAO pedagogicalPlanPersistenceDAO) {
        this.pedagogicalPlanPersistenceDAO = pedagogicalPlanPersistenceDAO;
    }
}
