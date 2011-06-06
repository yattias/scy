package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import eu.scy.client.desktop.scydesktop.corners.elomanagement.ModalDialogNode;
import javafx.scene.Node;
import eu.scy.client.common.datasync.ISynchronizable;

public class AcceptSyncModalDialog extends ModalDialogNode {

    public-read var label: javafx.scene.control.Label;
    public-read var cancelButton: javafx.scene.control.Button;
    public-read var okayButton: javafx.scene.control.Button;
    public-read var currentState: org.netbeans.javafx.design.DesignState;
    public var object:ISynchronizable;

    // <editor-fold defaultstate="collapsed" desc="Generated Init Block">
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
            text: ##"Do you want to synchronise\nboth tools?"
        };
        cancelButton = javafx.scene.control.Button {
            layoutX: 150.0
            layoutY: 50.0
            text: ##"No"
            action: cancelButtonAction
        };
        okayButton = javafx.scene.control.Button {
            //disable: true
            layoutX: 20.0
            layoutY: 50.0
            text: ##"Yes"
            action: okayButtonAction
            strong: true
        };

        currentState = org.netbeans.javafx.design.DesignState {
            names: []
            stateChangeType: org.netbeans.javafx.design.DesignStateChangeType.PAUSE_AND_PLAY_FROM_START
//            createTimeline: function (actual) {
//                null
//            }
        }
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Generated Design Functions">
    public function getDesignRootNodes(): javafx.scene.Node[] {
        [label, cancelButton, okayButton,]
    }

    public function getDesignScene(): javafx.scene.Scene {
        javafx.scene.Scene {
            content: getDesignRootNodes()
        }
    }// </editor-fold>

    function okayButtonAction(): Void {
        okayAction(object);
    }

    function cancelButtonAction(): Void {
        cancelAction();
    }

    public var okayAction: function(object: ISynchronizable): Void;
    public var cancelAction: function(): Void;

    override public function getContentNodes(): Node[] {
        return getDesignRootNodes();
    }

}