package eu.scy.client.tools.scydynamics.logging;

import colab.all.logging.Action;
import colab.all.logging.SQLSpacesLogger;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;


public class ModellingSQLSpacesLogger extends SQLSpacesLogger
		//TODO implements IActionLogger
{

	public ModellingSQLSpacesLogger(String username) {
		super(username);
	}

	//@Override
	public void log(IAction action) {
		System.out.println("ModellingSQLSpacesLogger.log "+action.getType());
		// conversion neccessary from (new) scy-action to (old) colab-action
		writeAction(createAction(action).getXML());
	}

	//@Override
	public void log(String username, String source, IAction action) {
		throw(new UnsupportedOperationException());
	}
	
	private Action createAction(IAction iAction) {
		Action action = createBasicAction(iAction.getType());
		action.addContext("id", iAction.getId());
		action.addContext("time", iAction.getTimeInMillis()+"");
		action.addContext("user", iAction.getUser());
		//TODO the rest...
		return action;
	}

}
