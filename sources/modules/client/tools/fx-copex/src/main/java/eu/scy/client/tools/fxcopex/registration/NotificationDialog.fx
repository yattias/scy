/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxcopex.registration;

import eu.scy.client.desktop.scydesktop.corners.elomanagement.ModalDialogNode;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import javafx.scene.Node;


/**
 * notification dialog
 * @author Marjolaine
 */

public class NotificationDialog   extends ModalDialogNode {

    public-read var label: javafx.scene.control.Label;
    public-read var cancelButton: javafx.scene.control.Button;
    public-read var okayButton: javafx.scene.control.Button;

    public var bundle:ResourceBundleWrapper;

    public var notificationText: String;

    init {
        label = javafx.scene.control.Label {
            layoutX: 0.0
            layoutY: 0.0
            width: 500.0
            height: 10.0
            layoutInfo: javafx.scene.layout.LayoutInfo {
                width: bind label.width
                height: bind label.height
            }
            text: notificationText
        };
        cancelButton = javafx.scene.control.Button {
            layoutX: 150.0
            layoutY: 50.0
            text: getBundleString("FX-COPEX.BUTTON_CANCEL")
            action: cancelButtonAction
        };
        okayButton = javafx.scene.control.Button {
            //disable: true
            layoutX: 20.0
            layoutY: 50.0
            text: getBundleString("FX-COPEX.BUTTON_OK")
            action: okayButtonAction
            strong: true
        };
    }

    function okayButtonAction(): Void {
        okayAction();
    }

    function cancelButtonAction(): Void {
        cancelAction();
    }

    public var okayAction: function(): Void;
    public var cancelAction: function(): Void;

    override public function getContentNodes(): Node[] {
        return getDesignRootNodes();
    }

    public function getDesignRootNodes(): javafx.scene.Node[] {
        [label, cancelButton, okayButton,]
    }

    public function getDesignScene(): javafx.scene.Scene {
        javafx.scene.Scene {
            content: getDesignRootNodes()
        }
    }

    public function getBundleString(key:String) : String{
       return bundle.getString(key);
   }
}
