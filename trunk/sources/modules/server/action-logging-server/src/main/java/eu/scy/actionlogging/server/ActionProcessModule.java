package eu.scy.actionlogging.server;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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

    private PrintWriter fileLogger; 
    
    public ActionProcessModule(Component scyhub) {
        super(scyhub, new ActionPacketTransformer());
        String catalinaBase = System.getProperty("catalina.base");
        String logfile;
        if (catalinaBase != null) {
            logfile = catalinaBase + File.separator + "logs" + File.separator + "scyActions.log";
        } else {
            logger.error("Didn't find catalina base. Logging to current working directory. Are you sure this is started in a Tomcat context?");
            logfile = "scyActions.log";
        }
        try {
            fileLogger = new PrintWriter(new FileWriter(logfile, true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        reconnect();
        logger.debug("ActionProcessModule created!");
    }

    private void reconnect() {
        try {
            if (ts != null) {
                ts.disconnect();
            }
            ts = new TupleSpace(new User("Action Logging Module"), Configuration.getInstance().getSQLSpacesServerHost(), Configuration.getInstance().getSQLSpacesServerPort(), "actions");
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    private void writeAction(IAction action) throws TupleSpaceException {
        Tuple actionTuple = ActionTupleTransformer.getActionAsTuple(action);
        dumpLogToFile(actionTuple);
        if (ts != null) {
            ts.write(actionTuple);
            logger.info("Tuple with " + action.getId() + " written into the logging space");
        }
    }

    private void dumpLogToFile(Tuple actionTuple) {
        fileLogger.println(actionTuple.toXMLString());
    }

    @Override
    protected void process(Packet packet, WhacketExtension extension) {
        Action action = (Action) extension.getPojo();
        logger.info("Received action for logging: " + action);
        try {
            writeAction(action);
        } catch (TupleSpaceException e) {
            reconnect();
            try {
                writeAction(action);
            } catch (TupleSpaceException e1) {
                logger.error("Error occurred while logging action: " + action.toString() + "\nSkipping this action and reconnecting to TS");
                e1.printStackTrace();
                reconnect();
            }
        }
    }

    @Override
    public void shutdown() {
        try {
            ts.disconnect();
            fileLogger.close();
            logger.debug("Shutdown ActionProcessModule");
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }
}
