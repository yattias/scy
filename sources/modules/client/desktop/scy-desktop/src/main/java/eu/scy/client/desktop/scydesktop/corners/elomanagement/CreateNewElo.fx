/*
 * CreateNewElo.fx
 *
 * Created on 28-feb-2010, 11:51:40
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.tools.search.ScyEloListCellDisplay;

/**
 * @author sikken
 */
public class CreateNewElo extends ModalDialogNode, ScyEloListCellDisplay {

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:main
    public-read def sizeRectangle: javafx.scene.shape.Rectangle = javafx.scene.shape.Rectangle {
        layoutX: 158.0
        layoutY: 125.0
        fill: javafx.scene.paint.Color.WHITE
        width: 100.0
        height: 50.0
    }
    
    def __layoutInfo_label: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 259.0
        height: 16.0
    }
    public-read def label: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 0.0
        layoutY: 0.0
        layoutInfo: __layoutInfo_label
        text: "##Select ELO type"
    }
    
    def __layoutInfo_listView: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 259.0
        height: 123.0
    }
    public-read def listView: javafx.scene.control.ListView = javafx.scene.control.ListView {
        disable: false
        layoutX: 0.0
        layoutY: 22.0
        layoutInfo: __layoutInfo_listView
        onKeyTyped: listViewOnKeyTyped
        onMouseClicked: listViewOnMouseClicked
    }
    
    public-read def cancelButton: javafx.scene.control.Button = javafx.scene.control.Button {
        layoutX: 192.0
        layoutY: 156.0
        text: "##Cancel"
        action: cancelButtonAction
    }
    
    public-read def createButton: javafx.scene.control.Button = javafx.scene.control.Button {
        disable: true
        layoutX: 88.0
        layoutY: 156.0
        text: "##Create"
        strong: true
        action: createButtonAction
    }
    
    public-read def currentState: org.netbeans.javafx.design.DesignState = org.netbeans.javafx.design.DesignState {
    }
    
    public function getDesignRootNodes (): javafx.scene.Node[] {
        [ sizeRectangle, label, listView, cancelButton, createButton, ]
    }
    
    public function getDesignScene (): javafx.scene.Scene {
        javafx.scene.Scene {
            content: getDesignRootNodes ()
        }
    }
    // </editor-fold>//GEN-END:main

    override public var disableRelevanceDisplay = true;

    postinit {
        sizeRectangle.fill = Color.TRANSPARENT;
    }

    def eloSelected = bind listView.selectedItem != null on replace {
                createButton.disable = not eloSelected;
            }

    function listViewOnKeyTyped(event: javafx.scene.input.KeyEvent): Void {
        if (not createButton.disabled and event.char == '\n') {
            createButtonAction();
        }
    }

    function listViewOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
        if (event.clickCount == 2 and listView.selectedIndex >= 0) {
            createButtonAction();
        }
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
