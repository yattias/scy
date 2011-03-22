package eu.scy.core.runtime;

import eu.scy.core.BaseServiceImpl;
import eu.scy.core.model.User;
import eu.scy.core.persistence.RuntimeDAO;
import eu.scy.core.persistence.UserDAO;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.apr.2010
 * Time: 06:15:06
 * To change this template use File | Settings | File Templates.
 */
public class RuntimeServiceImpl extends BaseServiceImpl implements RuntimeService {

    private static final Logger logger = Logger.getLogger(RuntimeServiceImpl.class);

    private RuntimeDAO runtimeDAO;

    public RuntimeDAO getRuntimeDAO() {
        return runtimeDAO;
    }

    public void setRuntimeDAO(RuntimeDAO runtimeDAO) {
        this.runtimeDAO = runtimeDAO;
        setScyBaseDAO(runtimeDAO);
    }

    @Override
    public List getActions(User user) {
        return getRuntimeDAO().getActions(user);
    }

    @Override
    @Transactional
    public void storeAction(String type, String id, long timeInMillis, String tool, String mission, String session, String eloUri, String userName) {
        runtimeDAO.storeAction(type, id, timeInMillis, tool, mission, session, eloUri, userName);
    }

}