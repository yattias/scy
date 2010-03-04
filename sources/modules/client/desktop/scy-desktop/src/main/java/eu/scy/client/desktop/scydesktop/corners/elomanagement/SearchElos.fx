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
public class SearchElos extends ModalDialogNode {

    public-read var label: javafx.scene.control.Label;//GEN-BEGIN:main
    public-read var label2: javafx.scene.control.Label;
    public-read var allTypesCheckBox: javafx.scene.control.CheckBox;
    public-read var typesListView: javafx.scene.control.ListView;
    public-read var label3: javafx.scene.control.Label;
    public-read var nameTextbox: javafx.scene.control.TextBox;
    public-read var onlyMineCheckBox: javafx.scene.control.CheckBox;
    public-read var label4: javafx.scene.control.Label;
    public-read var line: javafx.scene.shape.Line;
    public-read var searchButton: javafx.scene.control.Button;
    public-read var label5: javafx.scene.control.Label;
    public-read var resultsListView: javafx.scene.control.ListView;
    public-read var openButton: javafx.scene.control.Button;
    public-read var cancelButton: javafx.scene.control.Button;
    public-read var label6: javafx.scene.control.Label;
    
    public-read var currentState: org.netbeans.javafx.design.DesignState;
    
    // <editor-fold defaultstate="collapsed" desc="Generated Init Block">
    init {
        label = javafx.scene.control.Label {
            layoutX: 6.0
            layoutY: 6.0
            text: "Query:"
        };
        label2 = javafx.scene.control.Label {
            layoutX: 131.0
            layoutY: 6.0
            text: "All"
        };
        allTypesCheckBox = javafx.scene.control.CheckBox {
            layoutX: 158.0
            layoutY: 5.0
            onMouseClicked: allTypesCheckBoxOnMouseClicked
            text: ""
            selected: true
        };
        typesListView = javafx.scene.control.ListView {
            layoutX: 25.0
            layoutY: 34.0
            width: 150.0
            height: 93.0
            layoutInfo: javafx.scene.layout.LayoutInfo {
                width: bind typesListView.width
                height: bind typesListView.height
            }
            onMouseClicked: typesListViewOnMouseClicked
        };
        label3 = javafx.scene.control.Label {
            layoutX: 240.0
            layoutY: 6.0
            text: "Title"
        };
        nameTextbox = javafx.scene.control.TextBox {
            layoutX: 240.0
            layoutY: 34.0
            width: 169.0
            height: 24.0
            layoutInfo: javafx.scene.layout.LayoutInfo {
                width: bind nameTextbox.width
                height: bind nameTextbox.height
            }
        };
        onlyMineCheckBox = javafx.scene.control.CheckBox {
            layoutX: 314.0
            layoutY: 108.0
            onMouseClicked: onlyMineCheckBoxOnMouseClicked
            text: ""
        };
        label4 = javafx.scene.control.Label {
            layoutX: 240.0
            layoutY: 86.0
            text: "Owner"
        };
        line = javafx.scene.shape.Line {
            layoutX: 0.0
            layoutY: 143.0
            stroke: javafx.scene.paint.Color.LIGHTBLUE
            strokeWidth: 2.0
            endX: 490.0
            endY: 0.0
        };
        searchButton = javafx.scene.control.Button {
            layoutX: 426.0
            layoutY: 102.0
            text: "Search"
            action: searchButtonAction
        };
        label5 = javafx.scene.control.Label {
            layoutX: 6.0
            layoutY: 153.0
            text: "Results:"
        };
        resultsListView = javafx.scene.control.ListView {
            layoutX: 25.0
            layoutY: 175.0
            width: 384.0
            height: 112.0
            layoutInfo: javafx.scene.layout.LayoutInfo {
                width: bind resultsListView.width
                height: bind resultsListView.height
            }
            onMouseClicked: resultsListViewOnMouseClicked
        };
        openButton = javafx.scene.control.Button {
            disable: true
            layoutX: 435.0
            layoutY: 223.0
            text: "Open"
            action: openButtonAction
        };
        cancelButton = javafx.scene.control.Button {
            layoutX: 429.0
            layoutY: 259.0
            text: "Cancel"
            action: cancelButtonAction
        };
        label6 = javafx.scene.control.Label {
            layoutX: 240.0
            layoutY: 108.0
            text: "Only mine"
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
        [ label, label2, allTypesCheckBox, typesListView, label3, nameTextbox, onlyMineCheckBox, label4, line, searchButton, label5, resultsListView, openButton, cancelButton, label6, ]
    }
    
    public function getDesignScene (): javafx.scene.Scene {
        javafx.scene.Scene {
            content: getDesignRootNodes ()
        }
    }// </editor-fold>//GEN-END:main

   function cancelButtonAction(): Void {
      cancelAction(this);
   }

   function openButtonAction(): Void {
      openAction(this);
   }

   function resultsListViewOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
      openButton.disable = resultsListView.selectedIndex < 0;
   }

   function searchButtonAction(): Void {
      searchAction(this);
   }

   function typesListViewOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
      allTypesCheckBox.selected = false;
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

   override public function getContentNodes(): Node[] {
      return getDesignRootNodes();
   }

}
