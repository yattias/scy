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

/**
 * @author sven
 */
public class MessageDialogShowCommand extends ScyDesktopRemoteCommand {

    public var okAction:function()=function(){
            println("*=*=*=*=performed okAction()!*=*=*=*=*=*=")
    };

    public var yesAction:function()=function(){
            println("*=*=*=*=performed yesAction()!*=*=*=*=*=*=")
    };

    public var noAction:function()=function(){
            println("*=*=*=*=performed noAction()!*=*=*=*=*=*=")
    };



    override public function getActionName(): String {
        "message_dialog_show"
    }
    
    override public function executeRemoteCommand(notification: INotification): Void {
        logger.debug("*****************message dialog show action*********************");
        def modalProperty = notification.getFirstProperty("modal");
        def modal: Boolean = if(not (modalProperty==null)) Boolean.parseBoolean(modalProperty) else true;
        def text: String = notification.getFirstProperty("text");
        def dialogWidth: Double = (if (notification.getFirstProperty("width") == null) DialogBox.DEFAULT_WIDTH else Math.min(scyDesktop.scene.width, Double.valueOf(notification.getFirstProperty("width"))));
        def dialogTypeProperty = notification.getFirstProperty("dialogType");
        def dialogType: DialogType = if(dialogTypeProperty == null) DialogType.OK_DIALOG else DialogType.valueOf(dialogTypeProperty);
        
        if(dialogType==DialogType.OK_DIALOG){
            DialogBox.showMessageDialog(text, dialogWidth, scyDesktop, modal,okAction);
        } else if(dialogType==DialogType.OK_CANCEL_DIALOG or dialogType==DialogType.YES_NO_DIALOG){
            DialogBox.showOptionDialog(dialogType,text, dialogWidth, scyDesktop, modal,yesAction,noAction);
        }

    }

}
