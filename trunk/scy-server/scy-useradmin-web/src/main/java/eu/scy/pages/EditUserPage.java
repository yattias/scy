package eu.scy.pages;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.UserImpl;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 06.nov.2008
 * Time: 05:44:23
 * To change this template use File | Settings | File Templates.
 */
public class EditUserPage extends ScyModelPage{



    public void loadModel() {
        if(getModelId() != null) {
            setModel(getUserDAOHibernate().getUser(getModelId()));
        }
        else setModel(new UserImpl());

    }
}
