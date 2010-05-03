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
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.ContextConstants;

/**
 * @author sven
 */
public class OptionDialogShowCommand extends ScyDesktopRemoteCommand {

        var optionDialogId:String;
        var userId:String;

    function logResponse(responseCode:Integer){
        def action: IAction = new Action();
                action.setType("option_dialog_response");
                action.setUser(scyDesktop.config.getToolBrokerAPI().getLoginUserName());
                action.addContext(ContextConstants.tool, "remote_control");
                action.addAttribute("response_code", responseCode.toString());
                action.addAttribute("option_dialog_id",optionDialogId);
                scyDesktop.config.getToolBrokerAPI().getActionLogger().log(action);
    }

    public var yesAction: function() = function () {
                println("*=*=*=*=performed yesAction()!*=*=*=*=*=*=");
                logResponse(0);
            };
    public var noAction: function() = function () {
                println("*=*=*=*=performed noAction()!*=*=*=*=*=*=");
                logResponse(1);
            };

    override public function getActionName(): String {
        "option_dialog_show"
    }

    override public function executeRemoteCommand(notification: INotification): Void {
        logger.debug("*****************message dialog show action*********************");
        def modalProperty = notification.getFirstProperty("modal");
        def modal: Boolean = if (not (modalProperty == null)) Boolean.parseBoolean(modalProperty) else true;
        def text: String = notification.getFirstProperty("text");
        def dialogWidth: Double = (if (notification.getFirstProperty("width") == null) DialogBox.DEFAULT_WIDTH else Math.min(scyDesktop.scene.width, Double.valueOf(notification.getFirstProperty("width"))));
        def dialogTypeProperty = notification.getFirstProperty("dialogType");
        optionDialogId = notification.getUniqueID();
        userId = notification.getUserId();
        def dialogType: DialogType = if (dialogTypeProperty == null) DialogType.YES_NO_DIALOG else DialogType.valueOf(dialogTypeProperty);

        if (dialogType == DialogType.OK_CANCEL_DIALOG or dialogType == DialogType.YES_NO_DIALOG) {
            DialogBox.showOptionDialog(dialogType, text, dialogWidth, scyDesktop, modal, yesAction, noAction);
        }
    }

}
