package eu.scy.actionlogging;

import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.api.IAction;
import eu.scy.core.persistence.hibernate.ScyBaseDAOHibernate;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.aug.2009
 * Time: 10:52:17
 * To change this template use File | Settings | File Templates.
 */
public class ActionLoggerHibernate extends ScyBaseDAOHibernate implements IActionLogger {
    
	@Override
    @Deprecated
    public void log(String username, String source, IAction action) {
    	log(action);
    }

	@Override
	public void log(IAction action) {
		if(action == null) throw new NullPointerException("Action cannot be null");
		save(action);
	}
}
