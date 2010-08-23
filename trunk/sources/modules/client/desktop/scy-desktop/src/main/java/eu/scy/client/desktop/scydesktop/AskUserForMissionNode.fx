/*
 * AskUserForMissionNode.fx
 *
 * Created on 23-aug-2010, 15:31:44
 */

package eu.scy.client.desktop.scydesktop;

/**
 * @author SikkenJ
 */
public class AskUserForMissionNode {

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:main
    public-read def label: javafx.scene.control.Label = javafx.scene.control.Label {
        text: "Continue with"
    }
    
    def __layoutInfo_startedMissionListView: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 246.0
        height: 73.0
    }
    public-read def startedMissionListView: javafx.scene.control.ListView = javafx.scene.control.ListView {
        layoutInfo: __layoutInfo_startedMissionListView
        items: [ "Item 1", "Item 2", "Item 3", ]
    }
    
    public-read def label2: javafx.scene.control.Label = javafx.scene.control.Label {
        text: "Start new mission"
    }
    
    def __layoutInfo_listView: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 246.0
        height: 71.0
    }
    public-read def listView: javafx.scene.control.ListView = javafx.scene.control.ListView {
        layoutInfo: __layoutInfo_listView
        items: [ "Item 1", "Item 2", "Item 3", ]
    }
    
    public-read def cancelButton: javafx.scene.control.Button = javafx.scene.control.Button {
        text: "Cancel"
    }
    
    public-read def goButton: javafx.scene.control.Button = javafx.scene.control.Button {
        text: "Go"
    }
    
    public-read def currentState: org.netbeans.javafx.design.DesignState = org.netbeans.javafx.design.DesignState {
    }
    
    public function getDesignRootNodes (): javafx.scene.Node[] {
        [ label, startedMissionListView, label2, listView, cancelButton, goButton, ]
    }
    
    public function getDesignScene (): javafx.scene.Scene {
        javafx.scene.Scene {
            content: getDesignRootNodes ()
        }
    }
    // </editor-fold>//GEN-END:main

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
