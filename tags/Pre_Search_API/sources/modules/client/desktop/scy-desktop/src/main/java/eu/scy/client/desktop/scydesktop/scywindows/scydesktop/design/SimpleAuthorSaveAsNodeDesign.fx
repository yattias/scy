/*
 * SimpleAuthorSaveAsNodeDesign.fx
 *
 * Created on 10-nov-2010, 16:23:49
 */

package eu.scy.client.desktop.scydesktop.scywindows.scydesktop.design;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.SimpleScyDesktopEloSaver.FunctionalRoleContainer;
import eu.scy.common.scyelo.EloFunctionalRole;

/**
 * @author SikkenJ
 */
public class SimpleAuthorSaveAsNodeDesign extends EloSaveAsMixin {

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:main
    public-read def rectangle: javafx.scene.shape.Rectangle = javafx.scene.shape.Rectangle {
        layoutX: 0.0
        layoutY: 2.0
        fill: javafx.scene.paint.Color.WHITE
        width: 305.0
        height: 105.0
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
    }
    
    public-read def roleLabel: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 12.0
        layoutY: 46.0
        text: "##Role"
    }
    
    def __layoutInfo_roleChoiceBox: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 244.0
    }
    public-read def roleChoiceBox: javafx.scene.control.ChoiceBox = javafx.scene.control.ChoiceBox {
        layoutX: 55.0
        layoutY: 44.0
        layoutInfo: __layoutInfo_roleChoiceBox
        items: null
    }
    
    public-read def saveButton: javafx.scene.control.Button = javafx.scene.control.Button {
        disable: true
        layoutX: 154.0
        layoutY: 80.0
        onMouseClicked: null
        text: "##Save"
        action: saveButtonAction
    }
    
    public-read def cancelButton: javafx.scene.control.Button = javafx.scene.control.Button {
        layoutX: 232.0
        layoutY: 80.0
        text: "##Cancel"
        action: cancelButtonAction
    }
    
    public-read def currentState: org.netbeans.javafx.design.DesignState = org.netbeans.javafx.design.DesignState {
    }
    
    public function getDesignRootNodes (): javafx.scene.Node[] {
        [ rectangle, titleLabel, titleTextBox, roleLabel, roleChoiceBox, saveButton, cancelButton, ]
    }
    
    public function getDesignScene (): javafx.scene.Scene {
        javafx.scene.Scene {
            content: getDesignRootNodes ()
        }
    }
    // </editor-fold>//GEN-END:main

   def selectedFunctionalRole = bind (roleChoiceBox.selectedItem as FunctionalRoleContainer).functionalRole on replace {
         updateSaveState();
      };
   public override function getDesignNodes (): javafx.scene.Node[]{
      return getDesignRootNodes()
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
      if (sizeof roleChoiceBox.items > 1){
         if (selectedFunctionalRole==null){
            saveDisabled = true;
         }
      }
      saveButton.disable = saveDisabled;
   }

   public override function getTitle(): String {
      titleTextBox.rawText.trim();
   }

   public override function setTitle(title:String): Void{
      titleTextBox.text = title;
      updateSaveState();
   }

   public override function setFunctionalRoleContainers(functionalRoleContainers: FunctionalRoleContainer[]): Void{
      roleChoiceBox.items = functionalRoleContainers;
      roleChoiceBox.disable = sizeof roleChoiceBox.items <= 1;
   }

   public override function setFunctionalRole(functionalRole: EloFunctionalRole): Void{
      for (item in roleChoiceBox.items){
         def functionalRoleContainer = item as FunctionalRoleContainer;
         if (functionalRoleContainer.functionalRole==functionalRole){
            roleChoiceBox.select(indexof item);
            updateSaveState();
            return;
         }
      }
      roleChoiceBox.clearSelection();
      updateSaveState();
   }

   public override function getFunctionalRole(): EloFunctionalRole{
      return selectedFunctionalRole
   }
}

function run (): Void {
    var design = SimpleAuthorSaveAsNodeDesign {};

    javafx.stage.Stage {
        title: "SimpleAuthorSaveAsNodeDesign"
        scene: design.getDesignScene ()
    }
}
