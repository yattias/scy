package eu.scy.actionlogging;

import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.api.IContextService;
import java.awt.EventQueue;

/**
 * An action logger that is wrapped around another logger and that corrects missing attributes by
 * querying the context service.
 * 
 * @author weinbrenner
 * 
 */
public class CompletingActionLogger implements IActionLogger {

    private IContextService contextService;
    private IActionLogger internalLogger;

    public CompletingActionLogger(IContextService contextService, IActionLogger internalLogger) {
        this.internalLogger = internalLogger;
        this.contextService = contextService;
    }

    @Override
    public void log(final IAction action) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                autoComplete(action);
                internalLogger.log(action);
            }
        };
        if (EventQueue.isDispatchThread()) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.start();
        } else {
            r.run();
        }
    }

    private void autoComplete(IAction action) {
        if (action.getContext(ContextConstants.mission) == null && contextService.getMissionSpecificationURI() != null) {
            action.addContext(ContextConstants.mission, contextService.getMissionSpecificationURI());
        }
        if (action.getContext(ContextConstants.session) == null && contextService.getSession() != null) {
            action.addContext(ContextConstants.session, contextService.getSession());
        }
        if (action.getUser() == null && contextService.getUsername() != null) {
            action.setUser(contextService.getUsername());
        }
    }

    public IActionLogger getInternalLogger() {
        return internalLogger;
    }
}
