package eu.scy.tools.gstyler.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Starter class for GStyler, sets up some helpful debugging stuff and then adds GStyler to the RootPanel
 */
public class GStylerApp implements EntryPoint {

    /**
     * Useful for debugging DnD: Catch otherwise uncaught exceptions, print them to stderr and display big error dialog 
     */
    public void onModuleLoad() {
        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {

            public void onUncaughtException(Throwable throwable) {
                String text = "Uncaught exception: ";
                while (throwable != null) {
                    StackTraceElement[] stackTraceElements = throwable.getStackTrace();
                    text += throwable.toString() + "\n";
                    for (int i = 0; i < stackTraceElements.length; i++) {
                        text += "    at " + stackTraceElements[i] + "\n";
                    }
                    throwable = throwable.getCause();
                    if (throwable != null) {
                        text += "Caused by: ";
                    }
                }
                DialogBox dialogBox = new DialogBox(true);
                DOM.setStyleAttribute(dialogBox.getElement(), "backgroundColor", "#ABCDEF");
                System.err.print(text);
                text = text.replaceAll(" ", " ");
                dialogBox.setHTML("<pre>" + text + "</pre>");
                dialogBox.center();
            }
        });

        // use a deferred command so that the handler catches onModuleLoad2() exceptions
        DeferredCommand.addCommand(new Command() {

            public void execute() {
                onModuleLoad2();
            }
        });
    }

    private void onModuleLoad2() {
        RootPanel.get().add(new GStyler());
    }
}
