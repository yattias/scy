package eu.scy.actionlogging.server;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import org.apache.log4j.Logger;
import org.xmpp.component.Component;
import org.xmpp.packet.Packet;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.ActionPacketTransformer;
import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.IAction;
import eu.scy.common.configuration.Configuration;
import eu.scy.commons.whack.WhacketExtension;
import eu.scy.scyhub.SCYHubModule;


public class ActionProcessModule extends SCYHubModule {
    
	private static final Logger logger = Logger.getLogger(ActionProcessModule.class);
	
    private TupleSpace ts;
    
    public ActionProcessModule(Component scyhub) {
    	super(scyhub, new ActionPacketTransformer());
        try {
            ts = new TupleSpace(new User("Action Logging Module"), Configuration.getInstance().getSQLSpacesServerHost(), Configuration.getInstance().getSQLSpacesServerPort(), "actions");
            logger.debug("ActionProcessModule created!");
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }
    
    private void writeAction(IAction action) throws TupleSpaceException {
        if (ts != null) {
        	Tuple actionTuple = ActionTupleTransformer.getActionAsTuple(action);
            try {
                ts.write(actionTuple);
                logger.debug("Tuple with " + action.getId() + " written into the logging space");
            } catch (TupleSpaceException e) {
            	logger.error("Could not write tuple into space", e);
            }
        } 
    }

	@Override
	protected void process(Packet packet, WhacketExtension extension) {
		Action action = (Action) extension.getPojo();
		logger.debug("Received action for logging: " + action);
		try {
			writeAction(action);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void shutdown() {
		try {
			ts.disconnect();
			logger.debug("Shutdown ActionProcessModule");
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}
}
