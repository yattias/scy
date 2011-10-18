package eu.scy.client.tools.fxscymapper.registration;

import eu.scy.client.desktop.scydesktop.corners.elomanagement.ModalDialogNode;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Scene;

public class NotificationDialog extends ModalDialogNode {

    public-read var label: javafx.scene.control.Label;
//    public-read var cancelButton: javafx.scene.control.Button;
    public-read var okayButton: javafx.scene.control.Button;
    public var notificationText: String;

    init {
		label = javafx.scene.control.Label {
            layoutX: 0.0
            layoutY: 24.0
            width: 500.0
            height: 49.0
            layoutInfo: javafx.scene.layout.LayoutInfo {
                width: bind label.width
                height: bind label.height
            }
            text: notificationText
        };
//        cancelButton = javafx.scene.control.Button {
//            layoutX: 150.0
//            layoutY: 50.0
//            text: ##"Cancel"
//            action: cancelButtonAction
//        };
        okayButton = javafx.scene.control.Button {
            //disable: true
            layoutX: 20.0
            layoutY: 114.0
            text: ##"Okay"
            action: okayButtonAction
            strong: true
        };
    }

    function okayButtonAction(): Void {
        okayAction();
    }

//    function cancelButtonAction(): Void {
//        cancelAction();
//    }

    public var okayAction: function(): Void;

//    public var cancelAction: function(): Void;

    override public function getContentNodes(): Node[] {
        return getDesignRootNodes();
    }

    public function getDesignRootNodes(): javafx.scene.Node[] {
//        [label, cancelButton, okayButton,]
        [label, okayButton,]
    }

    public function getDesignScene(): javafx.scene.Scene {
        javafx.scene.Scene {
            content: getDesignRootNodes()
        }
    }
   
}
