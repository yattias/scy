/*
 * AskUserForMissionNode.fx
 *
 * Created on 23-aug-2010, 15:31:44
 */
package eu.scy.client.desktop.scydesktop.mission;

/**
 * @author SikkenJ
 */
public class AskUserForMissionNode {

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:main
    public-read def label: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 0.0
        layoutY: 0.0
        text: "##Continue with"
    }
    
    def __layoutInfo_startedMissionListView: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 290.0
        height: 80.0
    }
    public-read def startedMissionListView: javafx.scene.control.ListView = javafx.scene.control.ListView {
        layoutX: 0.0
        layoutY: 17.0
        layoutInfo: __layoutInfo_startedMissionListView
        onKeyTyped: null
        onMouseClicked: startedMissionListViewOnMouseClicked
        items: [ "Item 1", "Item 2", "Item 3", ]
    }
    
    public-read def label2: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 0.0
        layoutY: 110.0
        text: "##Start new mission"
    }
    
    def __layoutInfo_newMissionListView: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 290.0
        height: 80.0
    }
    public-read def newMissionListView: javafx.scene.control.ListView = javafx.scene.control.ListView {
        layoutX: 0.0
        layoutY: 127.0
        layoutInfo: __layoutInfo_newMissionListView
        onKeyTyped: null
        onMouseClicked: newMissionListViewOnMouseClicked
        items: [ "Item 1", "Item 2", "Item 3", ]
    }
    
    def __layoutInfo_cancelButton: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        hpos: javafx.geometry.HPos.RIGHT
    }
    public-read def cancelButton: javafx.scene.control.Button = javafx.scene.control.Button {
        layoutX: 233.0
        layoutY: 220.0
        layoutInfo: __layoutInfo_cancelButton
        text: "##Quit"
    }
    
    public-read def goButton: javafx.scene.control.Button = javafx.scene.control.Button {
        disable: true
        layoutX: 134.0
        layoutY: 220.0
        text: "##Go"
    }
    
    def __layoutInfo_blankButton: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        hpos: javafx.geometry.HPos.LEFT
    }
    public-read def blankButton: javafx.scene.control.Button = javafx.scene.control.Button {
        layoutX: 0.0
        layoutY: 220.0
        layoutInfo: __layoutInfo_blankButton
        text: "##Blank"
    }
    
    public-read def currentState: org.netbeans.javafx.design.DesignState = org.netbeans.javafx.design.DesignState {
    }
    
    public function getDesignRootNodes (): javafx.scene.Node[] {
        [ label, startedMissionListView, label2, newMissionListView, cancelButton, goButton, blankButton, ]
    }
    
    public function getDesignScene (): javafx.scene.Scene {
        javafx.scene.Scene {
            content: getDesignRootNodes ()
        }
    }
    // </editor-fold>//GEN-END:main

   function cancelButtonLayoutX(): Number {
      newMissionListView.boundsInLocal.maxX - cancelButton.boundsInLocal.width
   }

   def newMissionSelected = bind newMissionListView.selectedItem != null on replace {
              if (newMissionSelected) {
                 startedMissionListView.clearSelection();
              }
           }
   def startedMissionSelected = bind startedMissionListView.selectedItem != null on replace {
              if (newMissionSelected) {
                 newMissionListView.clearSelection();
              }
           }
   def missionSelected = bind newMissionSelected or startedMissionSelected on replace {
              goButton.disable = not missionSelected
           }

   function newMissionListViewOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
      if (newMissionListView.selectedIndex >= 0) {
         if (event.clickCount == 2) {
            goButton.fire();
         }
      }
   }

   function startedMissionListViewOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
      if (startedMissionListView.selectedIndex >= 0) {
         if (event.clickCount == 2) {
            goButton.fire();
         }
      }
   }

   function correctButtonPositions(): Void{
      def cancelLayoutX = newMissionListView.boundsInLocal.maxX - cancelButton.boundsInLocal.width;
      cancelButton.layoutX = cancelLayoutX;
      goButton.layoutX = cancelLayoutX - goButton.boundsInLocal.width - 26
   }

   init{
      FX.deferAction(correctButtonPositions);
   }

}

function run (): Void {
    var design = AskUserForMissionNode {};

    javafx.stage.Stage {
        title: "AskUserForMissionNode"
        scene: javafx.scene.Scene {
            content: design.getDesignRootNodes ()
        }
    }
}
