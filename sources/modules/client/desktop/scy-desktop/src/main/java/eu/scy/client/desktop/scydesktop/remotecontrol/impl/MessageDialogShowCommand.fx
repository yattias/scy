/*
 * CollaborationResponseAction.fx
 *
 * Created on 22.04.2010, 13:00:24
 */
package eu.scy.client.desktop.scydesktop.remotecontrol.impl;

 import java.lang.String;
import eu.scy.notification.api.INotification;
import javafx.util.Math;
import eu.scy.client.desktop.scydesktop.remotecontrol.api.ScyDesktopRemoteCommand;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.DialogBox;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.DialogType;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.DialogBoxParams;
import java.rmi.dgc.VMID;

/**
 * @author sven
 */
public class MessageDialogShowCommand extends ScyDesktopRemoteCommand {

    override public function getActionName(): String {
        "message_dialog_show"
    }
    
    override public function executeRemoteCommand(notification: INotification): Void {
        logger.debug("*****************message dialog show action*********************");
        def modalProperty = notification.getFirstProperty("modal");
        def indicateFocusProperty = notification.getFirstProperty("indicateFocus");
        def dialogTypeProperty = notification.getFirstProperty("dialogType");

        def params:DialogBoxParams = DialogBoxParams{
                scyDesktop:scyDesktop;
                dialogType: if (dialogTypeProperty == null) DialogType.YES_NO_DIALOG else DialogType.valueOf(dialogTypeProperty);
                dialogWidth: if (notification.getFirstProperty("width") == null) DialogBox.DEFAULT_WIDTH else Math.min(scyDesktop.scene.width, Double.valueOf(notification.getFirstProperty("width")));
                indicateFocus: if (not (indicateFocusProperty == null)) Boolean.parseBoolean(indicateFocusProperty) else true;
                text: notification.getFirstProperty("text");
                title: notification.getFirstProperty("title");
                modal: if (not (modalProperty == null)) Boolean.parseBoolean(modalProperty) else true;
                }

        DialogBox.showMessageDialog(params, new VMID().toString());

    }

}
