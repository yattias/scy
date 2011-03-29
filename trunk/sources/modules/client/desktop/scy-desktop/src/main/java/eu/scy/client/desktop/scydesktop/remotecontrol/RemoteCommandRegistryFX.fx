/*
 * RemoteCommandRegistryFX.fx
 *
 * Created on 22.04.2010, 13:23:00
 */
package eu.scy.client.desktop.scydesktop.remotecontrol;

import eu.scy.client.desktop.scydesktop.remotecontrol.impl.CollaborationRequestCommand;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.remotecontrol.impl.CollaborationResponseCommand;
import eu.scy.client.desktop.scydesktop.remotecontrol.api.RemoteCommandRegistry;
import eu.scy.client.desktop.scydesktop.remotecontrol.impl.EloShowCommand;
import eu.scy.notification.api.INotifiable;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.scydesktop.remotecontrol.impl.MessageDialogShowCommand;
import eu.scy.client.desktop.scydesktop.remotecontrol.impl.OptionDialogShowCommand;
import eu.scy.client.desktop.scydesktop.remotecontrol.impl.EloAssessmentFinishedCommand;
import eu.scy.client.desktop.scydesktop.remotecontrol.impl.AddBuddyCommand;
import eu.scy.client.desktop.scydesktop.remotecontrol.impl.RemoveAllBuddiesCommand;

/** This JavaFX class wraps the Registry for RemoteCommands and makes JavaFX-RemoteCommands possible
 * (for example these Commands accessing ScyDesktop)
 * @author sven, lars
 */
public class RemoteCommandRegistryFX extends INotifiable {

    public-init var scyDesktop: ScyDesktop;
    public def remoteCommandRegistry: RemoteCommandRegistry = RemoteCommandRegistry.getInstance();

    function createBasicRegistry() {
        def collaborationRequestCommand = CollaborationRequestCommand { scyDesktop: scyDesktop };
        def collaborationResponseCommand = CollaborationResponseCommand { scyDesktop: scyDesktop };
        def eloShowCommand = EloShowCommand { scyDesktop: scyDesktop };
        def messageDialogShowCommand = MessageDialogShowCommand { scyDesktop: scyDesktop };
        def optionDialogShowCommand = OptionDialogShowCommand { scyDesktop: scyDesktop };
        def eloAssessmentFinishedCommand = EloAssessmentFinishedCommand { scyDesktop: scyDesktop };
        def addBuddyCommand = AddBuddyCommand { scyDesktop: scyDesktop };
        def removeAllBuddiesCommand = RemoveAllBuddiesCommand { scyDesktop: scyDesktop };
        remoteCommandRegistry.registerRemoteCommands(collaborationRequestCommand, collaborationResponseCommand, eloShowCommand, messageDialogShowCommand, optionDialogShowCommand, eloAssessmentFinishedCommand,addBuddyCommand,removeAllBuddiesCommand);
        }

   postinit{
       createBasicRegistry();
   }

    public override function processNotification(notification: INotification): Boolean {
        FX.deferAction(function (): Void {
            remoteCommandRegistry.processNotification(notification);
        });
        // have to get the "success"-return-value differently because of the FX.deferAction stuff
        return remoteCommandRegistry.acceptsNotification(notification);
    }

}
