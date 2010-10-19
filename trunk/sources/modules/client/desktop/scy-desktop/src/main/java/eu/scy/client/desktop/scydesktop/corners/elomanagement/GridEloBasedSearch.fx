/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.scene.Node;
import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;
import com.javafx.preview.layout.Grid;
import com.javafx.preview.layout.GridRow;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;
import com.javafx.preview.layout.GridLayoutInfo;
import javafx.scene.control.ListView;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.layout.Priority;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Container;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressIndicator;
import eu.scy.common.scyelo.ScyElo;
import javafx.scene.layout.Stack;
import javafx.animation.Timeline;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;

/**
 * @author SikkenJ
 */
public class GridEloBasedSearch extends CustomNode, Resizable, ScyEloListCellDisplay, ShowSearching, ModalDialogNode {

   public override var width on replace {
         sizeChanged();
      }
   public override var height on replace {
         sizeChanged();
      }
   var grid: Grid;
   def spacing = 5.0;
   def leftCollumnSpace = 50.0;
   var eloDescription: Label;
   public def searchersList: ListView = ListView {
         //                           items: ['searcher 1', 'searcher 2']
         layoutInfo: LayoutInfo {
            height: 100
            minHeight: 50
            vfill: true
            vgrow: Priority.NEVER
            vshrink: Priority.NEVER
         }
      };
   def resultsListView: ListView = ListView {
         items: []
         layoutInfo: LayoutInfo {
            height: 200
            minHeight: 100
            width: 250
            vfill: true
            vgrow: Priority.ALWAYS
            vshrink: Priority.ALWAYS
         }
      };
   def progressIndicator = ProgressIndicator {
         progress: -1
      }
   def moreEloInfo = ExtendedScySearchResultCellNode{
      newEloCreationRegistry:newEloCreationRegistry
   }

   def foundLabelText = ##"Found";
   var foundLabel: Label;
   var baseButton: Button;
   var openButton: Button;
   var cancelButton: Button;
   public var doSearch: function(gridEloBasedSearch: GridEloBasedSearch): Void;
   public var baseOnAction: function(gridEloBasedSearch: GridEloBasedSearch): Void;
   public var openAction: function(gridEloBasedSearch: GridEloBasedSearch): Void;
   public var cancelAction: function(gridEloBasedSearch: GridEloBasedSearch): Void;
   public var baseElo: ScyElo on replace {
         eloDescription.text = getEloDescription(baseElo);
      };
   public-read def selectedEloBasedSearcher = bind searchersList.selectedItem as EloBasedSearcher on replace {
         doSearch(this);
      };
   public-read def selectedSearchResult = bind resultsListView.selectedItem as ScySearchResult on replace {
         openButton.disable = selectedSearchResult == null;
         baseButton.disable = selectedSearchResult == null;
         moreEloInfo.scySearchResult = selectedSearchResult;
      };

   init {
      FX.deferAction(function(): Void {
         searchersList.cellFactory = eloBasedSearcherCellFactory;
         resultsListView.cellFactory = simpleScyEloCellFactory;
         showSearchResult(null);
//         sizeChanged();
      });
      Timeline {
         repeatCount: 1
         keyFrames: [
            KeyFrame {
               time: 1s
               action: sizeChanged
            }
         ];
      }

   }

   public override function create(): Node {
      grid = Grid {
            managed: false
            hgap: spacing
            vgap: spacing
            padding: Insets {
               top: spacing
               right: spacing
               bottom: spacing
               left: spacing
            }
            rows: [
               GridRow {
                  cells: [
                     Label {
                        text: ##"ELO"
                     }
                     eloDescription = Label {
                           text: "elo"
                        }
                  ]
               }
               GridRow {
                  cells: [
                     Label {
                        text: ##"Search for"
                        layoutInfo: GridLayoutInfo { hspan: 2 }
                     }
                  ]
               }
               GridRow {
                  cells: [
                     Rectangle {
                        x: 0, y: 0, width: leftCollumnSpace, height: 10
                        fill: Color.TRANSPARENT
                     }
                     searchersList
                  ]
               }
               GridRow {
                  cells: [
                     foundLabel = Label {
                           text: ""
                           layoutInfo: GridLayoutInfo { hspan: 2 }
                        }
                  ]
               }
               GridRow {
                  cells: [
                     Label {
                        text: ""
                     }
                     Stack {
                        content: [
                           resultsListView,
                           progressIndicator
                        ]
                     }
                  ]
               }
               GridRow {
                  cells: [
                     Label {
                        text: ""
                     }
                     moreEloInfo
                  ]
               }
               GridRow {
                  cells: [
                     Label {
                        text: ""
                     }
                     HBox {
                        spacing: spacing
                        layoutInfo: LayoutInfo {
                        //                           hpos: HPos.RIGHT
                        }
                        hpos: HPos.RIGHT
                        content: [
                           baseButton = Button {
                                 disable: true
                                 text: ##"Use as base"
                                 action: function() {
                                    baseOnAction(this);
                                 }
                              }
                           openButton = Button {
                                 disable: true
                                 text: ##"Open"
                                 action: function() {
                                    openAction(this);
                                 }
                              }
                           cancelButton = Button {
                                 text: ##"Cancel"
                                 action: function() {
                                    cancelAction(this);
                                 }
                              }
                        ]
                     }
                  ]
               }
            ]
         }
   }

   function eloBasedSearcherCellFactory(): ListCell {
      var listCell: ListCell;
      listCell = ListCell {
            node: Label {
               text: bind "{(listCell.item as EloBasedSearcher).getDisplayId()}"
            }
         }
   }

   public override function showSearching(): Void {
            delete resultsListView.items;
//      resultsListView.items = [];
      resultsListView.disable = true;
      progressIndicator.visible = true;
      setNumberOfResults("?");
   }

   public override function showSearchResult(results: Object[]): Void {
      resultsListView.items = results;
      resultsListView.disable = false;
      progressIndicator.visible = false;
      setNumberOfResults("{sizeof resultsListView.items}");
   }

   function setNumberOfResults(nr: String): Void {
      foundLabel.text = "{foundLabelText} : {nr}";
   }

   public override function getPrefWidth(w: Number): Number {
      grid.getPrefWidth(w);
   }

   public override function getPrefHeight(h: Number): Number {
      grid.getPrefHeight(h);
   }

   public override function getMinWidth(): Number {
      grid.getMinWidth()
   }

   public override function getMinHeight(): Number {
      grid.getMinHeight();
   }

   function sizeChanged(): Void {
      Container.resizeNode(grid, width, height);
   }

   public override function getContentNodes(): Node[] {
      null
   }

   public override function getContentGroup(): Node {
      this
   }

}

function run() {
   Stage {
      title: "Grid elo based seach test"
      onClose: function() {
      }
      scene: Scene {
         width: 400
         height: 400
         content: [
            GridEloBasedSearch {
               layoutX: 10
               layoutY: 10
            }
         ]
      }
   }

}
