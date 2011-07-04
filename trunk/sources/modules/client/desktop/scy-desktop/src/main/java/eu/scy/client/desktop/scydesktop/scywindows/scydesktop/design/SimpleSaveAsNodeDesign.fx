/*
 * SimpleSaveAsNodeDesign.fx
 *
 * Created on 26-feb-2010, 12:10:22
 */
package eu.scy.client.desktop.scydesktop.scywindows.scydesktop.design;


/**
 * @author sikken
 */
public class SimpleSaveAsNodeDesign extends EloSaveAsMixin {

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:main
    public-read def rectangle: javafx.scene.shape.Rectangle = javafx.scene.shape.Rectangle {
        disable: true
        opacity: 0.0
        layoutX: 2.0
        layoutY: 1.0
        fill: javafx.scene.paint.Color.WHITE
        width: 300.0
        height: 70.0
    }
    
    public-read def titleLabel: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 12.0
        layoutY: 12.0
        text: "##Title"
    }
    
    def __layoutInfo_titleTextBox: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 244.0
        height: 24.0
    }
    public-read def titleTextBox: javafx.scene.control.TextBox = javafx.scene.control.TextBox {
        layoutX: 54.0
        layoutY: 6.0
        layoutInfo: __layoutInfo_titleTextBox
        onKeyTyped: titleTextBoxOnKeyTyped
        action: titleTextBoxAction
    }
    
    public-read def cancelButton: javafx.scene.control.Button = javafx.scene.control.Button {
        layoutX: 223.0
        layoutY: 44.0
        text: "##Cancel"
        action: cancelButtonAction
    }
    
    public-read def saveButton: javafx.scene.control.Button = javafx.scene.control.Button {
        disable: true
        layoutX: 150.0
        layoutY: 44.0
        text: "##Save"
        action: saveButtonAction
    }
    
    public-read def currentState: org.netbeans.javafx.design.DesignState = org.netbeans.javafx.design.DesignState {
    }
    
    public function getDesignRootNodes (): javafx.scene.Node[] {
        [ rectangle, titleLabel, titleTextBox, cancelButton, saveButton, ]
    }
    
    public function getDesignScene (): javafx.scene.Scene {
        javafx.scene.Scene {
            content: getDesignRootNodes ()
        }
    }
    // </editor-fold>//GEN-END:main

   public override function getDesignNodes (): javafx.scene.Node[]{
      return getDesignRootNodes()
   }

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

   function updateSaveState() {
      var title = getTitle();
      var saveDisabled = title.length() == 0;
      saveButton.disable = saveDisabled;
   }

   public override function getTitle(): String {
      titleTextBox.rawText.trim();
   }

   public override function setTitle(title:String): Void{
      titleTextBox.text = title;
      updateSaveState();
   }

   public override function correctButtonPositions(): Void{
      def cancelLayoutX = titleTextBox.boundsInParent.maxX - cancelButton.boundsInLocal.width;
      cancelButton.layoutX = cancelLayoutX;
      saveButton.layoutX = cancelLayoutX - saveButton.boundsInLocal.width - 26
   }


}
