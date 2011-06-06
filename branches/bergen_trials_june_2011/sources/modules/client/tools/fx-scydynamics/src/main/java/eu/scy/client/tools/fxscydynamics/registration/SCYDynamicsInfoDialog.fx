/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxscydynamics.registration;

import eu.scy.client.desktop.scydesktop.corners.elomanagement.ModalDialogNode;
import javafx.scene.Node;

public class SCYDynamicsInfoDialog extends ModalDialogNode {

    public-read var label: javafx.scene.control.Label;
    public-read var okayButton: javafx.scene.control.Button;
    public-read var currentState: org.netbeans.javafx.design.DesignState;
    public var okayAction: function(): Void;

    // <editor-fold defaultstate="collapsed" desc="Generated Init Block">
    init {
        label = javafx.scene.control.Label {
            layoutX: 0.0
            layoutY: 0.0
            width: 100.0
            height: 15.0
            layoutInfo: javafx.scene.layout.LayoutInfo {
                width: bind label.width
                height: bind label.height
            }
            text: "To save a dataset, you need some\nvalues in the table."
        };
        okayButton = javafx.scene.control.Button {
            //disable: true
            layoutX: 40.0
            layoutY: 50.0
            text:"Okay"
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
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Design Functions">
    public function getDesignRootNodes(): javafx.scene.Node[] {
        [label, okayButton]
    }

    public function getDesignScene(): javafx.scene.Scene {
        javafx.scene.Scene {
            content: getDesignRootNodes()
        }
    }// </editor-fold>
    override public function getContentNodes(): Node[] {
        return getDesignRootNodes();
    }

    function okayButtonAction(): Void {
        okayAction();
    }

}
