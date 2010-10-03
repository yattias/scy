/*
 * EloBasedSearchDesign.fx
 *
 * Created on 28-sep-2010, 18:59:11
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;

/**
 * @author SikkenJ
 */
public class EloBasedSearchDesign extends ModalDialogNode, ScyEloListCellDisplay {

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:main
    public-read def label: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 0.0
        layoutY: 0.0
        text: "##ELO:"
    }
    
    public-read def label2: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 0.0
        layoutY: 21.0
        text: "##Search for"
    }
    
    def __layoutInfo_relationListView: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 426.0
        height: 102.0
    }
    public-read def relationListView: javafx.scene.control.ListView = javafx.scene.control.ListView {
        layoutX: 54.0
        layoutY: 42.0
        layoutInfo: __layoutInfo_relationListView
        items: [ "Item 1", "Item 2", "Item 3", ]
        cellFactory: null
    }
    
    public-read def label3: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: -2.0
        layoutY: 156.0
        text: "##Found"
    }
    
    def __layoutInfo_resultsListView: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 426.0
        height: 148.0
    }
    public-read def resultsListView: javafx.scene.control.ListView = javafx.scene.control.ListView {
        layoutX: 54.0
        layoutY: 177.0
        layoutInfo: __layoutInfo_resultsListView
        onMouseClicked: resultsListViewOnMouseClicked
        items: [ "Item 1", "Item 2", "Item 3", ]
    }
    
    public-read def cancelButton: javafx.scene.control.Button = javafx.scene.control.Button {
        layoutX: 414.0
        layoutY: 340.0
        text: "##Cancel"
        action: cancelButtonAction
    }
    
    public-read def openButton: javafx.scene.control.Button = javafx.scene.control.Button {
        layoutX: 319.0
        layoutY: 340.0
        text: "##Open"
        action: openButtonAction
    }
    
    public-read def baseButton: javafx.scene.control.Button = javafx.scene.control.Button {
        layoutX: 193.0
        layoutY: 340.0
        text: "##Use as base"
        action: baseButtonAction
    }
    
    public-read def eloDescribtion: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 65.0
        layoutY: 6.0
        text: ""
    }
    
    def __layoutInfo_eloDescription: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 420.0
    }
    public-read def eloDescription: javafx.scene.control.Label = javafx.scene.control.Label {
        layoutX: 54.0
        layoutY: 0.0
        layoutInfo: __layoutInfo_eloDescription
        text: "????"
    }
    
    public-read def scene: javafx.scene.Scene = javafx.scene.Scene {
        width: 480.0
        height: 386.0
        content: getDesignRootNodes ()
    }
    
    public-read def currentState: org.netbeans.javafx.design.DesignState = org.netbeans.javafx.design.DesignState {
    }
    
    function listCellFactory(): javafx.scene.control.ListCell {
        var listCell: javafx.scene.control.ListCell;
        
        def label4: javafx.scene.control.Label = javafx.scene.control.Label {
            visible: bind not listCell.empty
            text: bind "{listCell.item}"
        }
        
        listCell = javafx.scene.control.ListCell {
            node: label4
        }
        
        return listCell
    }
    
    function listCellFactory2(): javafx.scene.control.ListCell {
        var listCell2: javafx.scene.control.ListCell;
        
        def label5: javafx.scene.control.Label = javafx.scene.control.Label {
            visible: bind not listCell2.empty
            text: bind "{listCell2.item}"
        }
        
        listCell2 = javafx.scene.control.ListCell {
            node: label5
        }
        
        return listCell2
    }
    
    public function getDesignRootNodes (): javafx.scene.Node[] {
        [ label, label2, relationListView, label3, resultsListView, cancelButton, openButton, baseButton, eloDescribtion, eloDescription, ]
    }
    
    public function getDesignScene (): javafx.scene.Scene {
        scene
    }
    // </editor-fold>//GEN-END:main

   function resultsListViewOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
      if (event.clickCount == 2 and resultsListView.selectedIndex >= 0) {
         openButtonAction();
      }
   }

   function cancelButtonAction(): Void {
      cancelAction(this)
   }

   function openButtonAction(): Void {
      openAction(this)
   }

   function baseButtonAction(): Void {
      baseOnAction(this)
   }

   override public function getContentNodes(): Node[] {
      return getDesignRootNodes();
   }

   public var doSearch: function(eloBasedSearchDesign: EloBasedSearchDesign): Void;
   public var baseOnAction: function(eloBasedSearchDesign: EloBasedSearchDesign): Void;
   public var openAction: function(eloBasedSearchDesign: EloBasedSearchDesign): Void;
   public var cancelAction: function(eloBasedSearchDesign: EloBasedSearchDesign): Void;
   public var baseElo: ScyElo on replace {
         eloDescription.text = getEloDescription(baseElo);
      };
   public-read def selectedEloBasedSearcher = bind relationListView.selectedItem as EloBasedSearcher on replace {
         doSearch(this);
      };
   public-read def selectedSearchResult = bind resultsListView.selectedItem as ScySearchResult on replace {
         openButton.disable = selectedSearchResult == null;
         baseButton.disable = selectedSearchResult == null;
      };

   init{
      relationListView.cellFactory = eloBasedSearcherCellFactory;
      resultsListView.cellFactory = scyEloCellFactory;
   }

   function eloBasedSearcherCellFactory(): ListCell {
      var listCell: ListCell;
      listCell = ListCell {
            node: Label {
               text: bind "{(listCell.item as EloBasedSearcher).getDisplayId()}"
            }
         }
   }

}

function run (): Void {
    var design = EloBasedSearchDesign {};

    javafx.stage.Stage {
        title: "EloBasedSearchDesign"
        scene: design.getDesignScene ()
    }
}
