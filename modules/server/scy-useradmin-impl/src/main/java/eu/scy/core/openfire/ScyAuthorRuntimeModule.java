package eu.scy.core.openfire;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.ActionPacketTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.common.configuration.Configuration;
import eu.scy.commons.whack.WhacketExtension;
import eu.scy.core.runtime.RuntimeService;
import eu.scy.scyhub.SCYHubModule;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import org.apache.log4j.Logger;
import org.xmpp.component.Component;
import org.xmpp.packet.Packet;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.apr.2010
 * Time: 13:35:32
 * To change this template use File | Settings | File Templates.
 */
public class ScyAuthorRuntimeModule extends SCYHubModule {

    private static final Logger logger = Logger.getLogger(ScyAuthorRuntimeModule.class);
    private TupleSpace ts;
    private RuntimeService runtimeService;

    /**
     * All subclasses must call the constructor to set the
     * {@link org.xmpp.component.Component} reference for sending messages.
     *
     * @param scyhub      the {@link org.xmpp.component.Component}
     * @param 
     */
    public ScyAuthorRuntimeModule(Component scyhub){
        super(scyhub, new ActionPacketTransformer());
        logger.debug("CREAETED SCY AUTHOR RUNTIME MODULE!!!!!!");
    	try {
    		ts = new TupleSpace(new User("Action Logging Module"), Configuration.getInstance().getSQLSpacesServerHost(), Configuration.getInstance().getSQLSpacesServerPort(), "actions");
    	} catch (TupleSpaceException e) {
    		e.printStackTrace();
    	}
    }

    @Override
    protected void process(Packet packet, WhacketExtension extension) {
        Action action = (Action) extension.getPojo();
        logger.debug("ACTION TYPE: " + action.getType());
        logger.debug("ID" + action.getId());
        logger.debug("USER: " + action.getUser());
        logger.debug("TIME: " + action.getTimeInMillis());
        logger.debug("TOOL: " + action.getContext().get(ContextConstants.tool));
        logger.debug("MISSION: " + action.getContext().get(ContextConstants.mission));
        logger.debug("SESSION: " + action.getContext().get(ContextConstants.session));
        logger.debug("ELOURI: " + action.getContext().get(ContextConstants.eloURI));

        getRuntimeService().storeAction(
                action.getType(),
                action.getId(),
                action.getTimeInMillis(),
                action.getContext().get(ContextConstants.tool),
                action.getContext().get(ContextConstants.mission),
                action.getContext().get(ContextConstants.session),
                action.getContext().get(ContextConstants.eloURI),
                action.getUser());

    }

    @Override
    public void shutdown() {
        logger.debug("Shutdown ScyAuthorRuntimeModule");
    }

    public RuntimeService getRuntimeService() {
        return runtimeService;
    }

    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }
}
