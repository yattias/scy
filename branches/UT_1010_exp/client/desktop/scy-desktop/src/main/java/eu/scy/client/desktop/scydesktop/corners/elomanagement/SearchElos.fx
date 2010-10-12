/*
 * SearchElos.fx
 *
 * Created on 28-feb-2010, 15:38:37
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.scene.Node;

/**
 * @author sikken
 */
public class SearchElos extends ModalDialogNode, ScyEloListCellDisplay {

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:main
    public-read def label: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 0.0
        layoutY: 0.0
        text: "##Query:"
    }
    
    public-read def label2: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 125.0
        layoutY: 0.0
        text: "##All"
    }
    
    public-read def allTypesCheckBox: javafx.scene.control.CheckBox = javafx.scene.control.CheckBox {
        layoutX: 158.0
        layoutY: -1.0
        onMouseClicked: allTypesCheckBoxOnMouseClicked
        text: ""
        selected: true
    }
    
    def __layoutInfo_typesListView: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 150.0
        height: 93.0
    }
    public-read def typesListView: javafx.scene.control.ListView = javafx.scene.control.ListView {
        layoutX: 25.0
        layoutY: 28.0
        layoutInfo: __layoutInfo_typesListView
        onMouseClicked: typesListViewOnMouseClicked
    }
    
    public-read def label3: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 240.0
        layoutY: 0.0
        text: "##Title"
    }
    
    def __layoutInfo_nameTextbox: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 169.0
        height: 24.0
    }
    public-read def nameTextbox: javafx.scene.control.TextBox = javafx.scene.control.TextBox {
        layoutX: 240.0
        layoutY: 28.0
        layoutInfo: __layoutInfo_nameTextbox
    }
    
    def __layoutInfo_mineCheckBox: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 17.0
        height: 17.0
    }
    public-read def mineCheckBox: javafx.scene.control.CheckBox = javafx.scene.control.CheckBox {
        layoutX: 284.0
        layoutY: 102.0
        layoutInfo: __layoutInfo_mineCheckBox
        onMouseClicked: onlyMineCheckBoxOnMouseClicked
        text: ""
    }
    
    def __layoutInfo_label4: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 49.0
        height: 22.0
    }
    public-read def label4: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 240.0
        layoutY: 80.0
        layoutInfo: __layoutInfo_label4
        text: "##Author"
    }
    
    public-read def line: javafx.scene.shape.Line = javafx.scene.shape.Line {
        layoutX: 0.0
        layoutY: 137.0
        stroke: javafx.scene.paint.Color.LIGHTBLUE
        strokeWidth: 2.0
        endX: 493.0
        endY: 0.0
    }
    
    public-read def searchButton: javafx.scene.control.Button = javafx.scene.control.Button {
        layoutX: 419.0
        layoutY: 100.0
        text: "##Search"
        hpos: javafx.geometry.HPos.RIGHT
        action: searchButtonAction
    }
    
    public-read def label5: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 0.0
        layoutY: 147.0
        text: "##Results:"
    }
    
    def __layoutInfo_resultsListView: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 384.0
        height: 112.0
    }
    public-read def resultsListView: javafx.scene.control.ListView = javafx.scene.control.ListView {
        layoutX: 25.0
        layoutY: 169.0
        layoutInfo: __layoutInfo_resultsListView
        onMouseClicked: resultsListViewOnMouseClicked
    }
    
    public-read def openButton: javafx.scene.control.Button = javafx.scene.control.Button {
        disable: true
        layoutX: 435.0
        layoutY: 217.0
        text: "##Open"
        action: openButtonAction
    }
    
    public-read def cancelButton: javafx.scene.control.Button = javafx.scene.control.Button {
        layoutX: 426.0
        layoutY: 259.0
        text: "##Cancel"
        textAlignment: javafx.scene.text.TextAlignment.LEFT
        hpos: javafx.geometry.HPos.RIGHT
        action: cancelButtonAction
    }
    
    public-read def label6: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 240.0
        layoutY: 102.0
        text: "##Me"
    }
    
    public-read def label7: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 316.0
        layoutY: 102.0
        text: "##Others"
    }
    
    public-read def othersCheckBox: javafx.scene.control.CheckBox = javafx.scene.control.CheckBox {
        layoutX: 370.0
        layoutY: 102.0
        text: ""
    }
    
    public-read def currentState: org.netbeans.javafx.design.DesignState = org.netbeans.javafx.design.DesignState {
    }
    
    public function getDesignRootNodes (): javafx.scene.Node[] {
        [ label, label2, allTypesCheckBox, typesListView, label3, nameTextbox, mineCheckBox, label4, line, searchButton, label5, resultsListView, openButton, cancelButton, label6, label7, othersCheckBox, ]
    }
    
    public function getDesignScene (): javafx.scene.Scene {
        javafx.scene.Scene {
            content: getDesignRootNodes ()
        }
    }
    // </editor-fold>//GEN-END:main

   function cancelButtonAction(): Void {
      cancelAction(this);
   }

   function openButtonAction(): Void {
      openAction(this);
   }

   function resultsListViewOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
      openButton.disable = resultsListView.selectedIndex < 0;
      if (event.clickCount==2 and resultsListView.selectedIndex>=0 ){
         openButtonAction();
      }
   }

   function searchButtonAction(): Void {
      searchAction(this);
   }

   function typesListViewOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
      allTypesCheckBox.selected = false;
      if (event.clickCount==2){
         searchButtonAction();
      }
   }

   function onlyMineCheckBoxOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
      //TODO
   }

   function allTypesCheckBoxOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
      typesListView.clearSelection();
   }

   public var searchAction: function(searchElos: SearchElos): Void;
   public var openAction: function(searchElos: SearchElos): Void;
   public var cancelAction: function(searchElos: SearchElos): Void;

   init{
      resultsListView.cellFactory = scyEloCellFactory;
   }
   
   override public function getContentNodes(): Node[] {
      return getDesignRootNodes();
   }

}
