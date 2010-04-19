package eu.scy.core;

import eu.scy.core.persistence.SCYBaseDAO;
import eu.scy.core.persistence.SetupDAO;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 26.mar.2010
 * Time: 15:01:14
 * To change this template use File | Settings | File Templates.
 */
public class SetupServiceImpl extends BaseServiceImpl implements SetupService {

    private SetupDAO setupDAO;

    public SetupDAO getSetupDAO() {
        return setupDAO;
    }

    public void setSetupDAO(SetupDAO setupDAO) {
        super.setScyBaseDAO((SCYBaseDAO) setupDAO);
        this.setupDAO = setupDAO;
    }
}
