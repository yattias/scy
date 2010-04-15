package eu.scy.core.openfire;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.ActionPacketTransformer;
import eu.scy.common.packetextension.SCYPacketTransformer;
import eu.scy.commons.whack.WhacketExtension;
import eu.scy.scyhub.SCYHubModule;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.apache.log4j.Logger;
import org.xmpp.component.Component;
import org.xmpp.packet.Packet;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.apr.2010
 * Time: 13:35:32
 * To change this template use File | Settings | File Templates.
 */
public class ScyAuthorRuntimeModule extends SCYHubModule {

    private static final Logger logger = Logger.getLogger(ScyAuthorRuntimeModule.class);

    /**
     * All subclasses must call the constructor to set the
     * {@link org.xmpp.component.Component} reference for sending messages.
     *
     * @param scyhub      the {@link org.xmpp.component.Component}
     * @param transformer
     */
    public ScyAuthorRuntimeModule(Component scyhub){
        super(scyhub, new ActionPacketTransformer());
        logger.info("********************************** I AM FREAKIN ADDED!");
        logger.info("********************************** I AM FREAKIN ADDED!");
        logger.info("********************************** I AM FREAKIN ADDED!");
    }

    @Override
    protected void process(Packet packet, WhacketExtension extension) {
        Action action = (Action) extension.getPojo();
        Set keys = action.getAttributes().keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String o = (String) it.next();
            logger.info("KEY: " + o + " VALUE: " + action.getAttribute(o));
        }


/*		try {
			writeAction(action);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}*/
    }

    @Override
    public void shutdown() {
        logger.debug("Shutdown ScyAuthorRuntimeModule");
    }
}
