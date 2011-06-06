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
        logger.info("ACTION TYPE: " + action.getType());
        logger.info("ID" + action.getId());
        logger.info("USER: " + action.getUser());
        logger.info("TIME: " + action.getTimeInMillis());
        logger.info("TOOL: " + action.getContext().get(ContextConstants.tool));
        logger.info("MISSION: " + action.getContext().get(ContextConstants.mission));
        logger.info("SESSION: " + action.getContext().get(ContextConstants.session));
        logger.info("ELOURI: " + action.getContext().get(ContextConstants.eloURI));
        logger.info("NEW LAS: " + action.getAttribute("newLasId"));
        logger.info("OLD LAS: " + action.getAttribute("oldLasId"));

        getRuntimeService().storeAction(
                action.getType(),
                action.getId(),
                action.getTimeInMillis(),
                action.getContext().get(ContextConstants.tool),
                action.getContext().get(ContextConstants.mission),
                action.getContext().get(ContextConstants.session),
                action.getContext().get(ContextConstants.eloURI),
                action.getUser(),
                action.getAttribute("oldLasId"),
                action.getAttribute("newLasId")

                );

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
