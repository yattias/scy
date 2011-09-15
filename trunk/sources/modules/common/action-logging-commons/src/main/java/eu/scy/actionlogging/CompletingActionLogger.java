package eu.scy.actionlogging;

import eu.scy.actionlogging.api.ActionLoggedEventListener;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.api.IContextService;

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
    public void log(IAction action) {
        autoComplete(action);
        internalLogger.log(action);
    }

    @Override
    @Deprecated
    public void log(String username, String source, IAction action) {
        autoComplete(action);
        internalLogger.log(username, source, action);
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

   @Override
   public void addActionLoggedEventListener(ActionLoggedEventListener actionLoggedEventListener)
   {
      internalLogger.addActionLoggedEventListener(actionLoggedEventListener);
   }

   @Override
   public void removeActionLoggedEventListener(ActionLoggedEventListener actionLoggedEventListener)
   {
      internalLogger.removeActionLoggedEventListener(actionLoggedEventListener);
   }
    
}
