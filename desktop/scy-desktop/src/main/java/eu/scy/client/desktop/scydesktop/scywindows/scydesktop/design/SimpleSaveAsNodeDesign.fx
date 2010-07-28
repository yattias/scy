/*
 * SimpleSaveAsNodeDesign.fx
 *
 * Created on 26-feb-2010, 12:10:22
 */
package eu.scy.client.desktop.scydesktop.scywindows.scydesktop.design;

import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import roolo.elo.api.IELO;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;

/**
 * @author sikken
 */
public class SimpleSaveAsNodeDesign {

    public-read var rectangle: javafx.scene.shape.Rectangle;//GEN-BEGIN:main
    public-read var titleLabel: javafx.scene.control.Label;
    public-read var titleTextBox: javafx.scene.control.TextBox;
    public-read var cancelButton: javafx.scene.control.Button;
    public-read var saveButton: javafx.scene.control.Button;
    
    public-read var currentState: org.netbeans.javafx.design.DesignState;
    
    // <editor-fold defaultstate="collapsed" desc="Generated Init Block">
    init {
        rectangle = javafx.scene.shape.Rectangle {
            disable: true
            opacity: 0.0
            layoutX: 2.0
            layoutY: 1.0
            fill: javafx.scene.paint.Color.WHITE
            width: 300.0
            height: 70.0
        };
        titleLabel = javafx.scene.control.Label {
            layoutX: 12.0
            layoutY: 12.0
            text: "##Title"
        };
        titleTextBox = javafx.scene.control.TextBox {
            layoutX: 54.0
            layoutY: 6.0
            width: 244.0
            height: 24.0
            layoutInfo: javafx.scene.layout.LayoutInfo {
                width: bind titleTextBox.width
                height: bind titleTextBox.height
            }
            onKeyTyped: titleTextBoxOnKeyTyped
            action: titleTextBoxAction
        };
        cancelButton = javafx.scene.control.Button {
            layoutX: 223.0
            layoutY: 44.0
            text: "##Cancel"
            action: cancelButtonAction
        };
        saveButton = javafx.scene.control.Button {
            disable: true
            layoutX: 150.0
            layoutY: 44.0
            text: "##Save"
            action: saveButtonAction
        };
        
        currentState = org.netbeans.javafx.design.DesignState {
            names: [ ]
            stateChangeType: org.netbeans.javafx.design.DesignStateChangeType.PAUSE_AND_PLAY_FROM_START
//            createTimeline: function (actual) {
//                null
//            }
        }
    }// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Generated Design Functions">
    public function getDesignRootNodes () : javafx.scene.Node[] {
        [ rectangle, titleLabel, titleTextBox, cancelButton, saveButton, ]
    }
    
    public function getDesignScene (): javafx.scene.Scene {
        javafx.scene.Scene {
            content: getDesignRootNodes ()
        }
    }// </editor-fold>//GEN-END:main

   function titleTextBoxAction(): Void {
      saveButtonAction();
   }

   function cancelButtonAction(): Void {
      cancelAction(this);
   }

   function saveButtonAction(): Void {
      saveAction(this)
   }

   function titleTextBoxOnKeyTyped(event: javafx.scene.input.KeyEvent): Void {
      updateSaveState();
   }

   public var saveAction: function(: SimpleSaveAsNodeDesign): Void;
   public var cancelAction: function(: SimpleSaveAsNodeDesign): Void;
   // store information of caller
   public var elo: IELO;
   public var myElo: Boolean;
   public var eloSaverCallBack: EloSaverCallBack;
   public var modalDialogBox: ModalDialogBox;

   function updateSaveState() {
      var title = getTitle();
      var saveDisabled = title.length() == 0;
      saveButton.disable = saveDisabled;
   }

   public function getTitle(): String {
      titleTextBox.rawText.trim();
   }

   public function setTitle(title:String){
      titleTextBox.text = title;
      updateSaveState();
   }


}
