/*
 * CreateNewElo.fx
 *
 * Created on 28-feb-2010, 11:51:40
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.scene.Node;

/**
 * @author sikken
 */
public class CreateNewElo extends ModalDialogNode {

    public-read var label: javafx.scene.control.Label;//GEN-BEGIN:main
    public-read var listView: javafx.scene.control.ListView;
    public-read var cancelButton: javafx.scene.control.Button;
    public-read var createButton: javafx.scene.control.Button;
    
    public-read var currentState: org.netbeans.javafx.design.DesignState;
    
    // <editor-fold defaultstate="collapsed" desc="Generated Init Block">
    init {
        label = javafx.scene.control.Label {
            layoutX: 0.0
            layoutY: 0.0
            width: 259.0
            height: 16.0
            layoutInfo: javafx.scene.layout.LayoutInfo {
                width: bind label.width
                height: bind label.height
            }
            text: "##Select ELO type"
        };
        listView = javafx.scene.control.ListView {
            layoutX: 0.0
            layoutY: 28.0
            width: 259.0
            height: 111.0
            layoutInfo: javafx.scene.layout.LayoutInfo {
                width: bind listView.width
                height: bind listView.height
            }
            onKeyTyped: listViewOnKeyTyped
            onMouseClicked: listViewOnMouseClicked
        };
        cancelButton = javafx.scene.control.Button {
            layoutX: 181.0
            layoutY: 152.0
            text: "##Cancel"
            action: cancelButtonAction
        };
        createButton = javafx.scene.control.Button {
            disable: true
            layoutX: 88.0
            layoutY: 152.0
            text: "##Create"
            action: createButtonAction
            strong: true
        };
        
        currentState = org.netbeans.javafx.design.DesignState {
            names: [ ]
            stateChangeType: org.netbeans.javafx.design.DesignStateChangeType.PAUSE_AND_PLAY_FROM_START
            createTimeline: function (actual) {
                null
            }
        }
    }// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Generated Design Functions">
    public function getDesignRootNodes () : javafx.scene.Node[] {
        [ label, listView, cancelButton, createButton, ]
    }
    
    public function getDesignScene (): javafx.scene.Scene {
        javafx.scene.Scene {
            content: getDesignRootNodes ()
        }
    }// </editor-fold>//GEN-END:main

   function listViewOnKeyTyped(event: javafx.scene.input.KeyEvent): Void {
      println("key: {event} {event.char=='\n'}");
      if (not createButton.disabled and event.char=='\n') {
         createButtonAction();
      }
   }

   function listViewOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
      createButton.disable = listView.selectedIndex < 0;
   }

   function createButtonAction(): Void {
      createAction(this);
   }

   function cancelButtonAction(): Void {
      cancelAction(this);
   }

   public var createAction: function(createNewElo: CreateNewElo): Void;
   public var cancelAction: function(createNewElo: CreateNewElo): Void;

   override public function getContentNodes(): Node[] {
      return getDesignRootNodes();
   }

}
