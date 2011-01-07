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
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.utils.multiselectlistview.MultiSelectListView;
import eu.scy.client.desktop.scydesktop.utils.multiselectlistview.MultiSelectListCell;
import javafx.scene.shape.Line;
import javafx.util.Math;

/**
 * @author SikkenJ
 */
public class GridEloBasedSearch extends CustomNode, Resizable, ScyEloListCellDisplay, ShowSearching, ModalDialogNode {

   public override var width on replace {
         sizeChanged();
         printGridSizes("width");
      }
   public override var height on replace {
         sizeChanged();
         printGridSizes("height");
      }
   var grid: Grid;
   def spacing = 5.0;
   def leftCollumnSpace = 20.0;
   def leftCollumnFillerHeight = 5.0;
   def leftCollumnFillerColor = Color.TRANSPARENT;
   def separatorlineWidth = 2.0;
   var separatorLineLength = 10.0;
   def separatorLineColor = Color.BLACK;
   def eloInfoLayoutInfo = GridLayoutInfo {
            width: 300
            minWidth: 200
            vfill: true
            hspan: 2
         }

   def baseEloInfo = ExtendedScyEloDisplayNode {
         layoutInfo: eloInfoLayoutInfo
         newEloCreationRegistry: newEloCreationRegistry
         scyElo: bind baseElo
         eloIcon: bind baseEloIcon
      }
   public def searchersList: MultiSelectListView = MultiSelectListView {
         layoutInfo: LayoutInfo {
            height: 75
            minHeight: 50
            vfill: true
            vgrow: Priority.NEVER
            vshrink: Priority.NEVER
         }
      };
   def resultsListView: ListView = ListView {
         layoutInfo: LayoutInfo {
            height: 125
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
   def moreEloInfo = ExtendedScyEloDisplayNode {
         layoutInfo: eloInfoLayoutInfo
         newEloCreationRegistry: newEloCreationRegistry
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
   public var baseElo: ScyElo;
   public var baseEloIcon: EloIcon;
   public-read def selectedEloBasedSearchers = bind searchersList.selectedItems as EloBasedSearcher[] on replace {
         doSearch(this);
      };
   public-read def selectedSearchResult = bind resultsListView.selectedItem as ScySearchResult on replace {
         openButton.disable = selectedSearchResult == null;
         baseButton.disable = selectedSearchResult == null;
         moreEloInfo.scyElo = selectedSearchResult.getScyElo();
         moreEloInfo.eloIcon = (selectedSearchResult.getEloIcon() as EloIcon).clone();
      };

   init {
      FX.deferAction(function(): Void {
         searchersList.cellFactory = eloBasedSearcherCellFactory;
         resultsListView.cellFactory = simpleScyEloCellFactory;
         showSearchResult(null);
//         sizeChanged();
      });
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
            growRows: [0, 0, 0, 0, 1, 0]
            growColumns: [0, 1]
            rows: [
               GridRow {
                  cells: [
                     Rectangle {
                        x: 0, y: 0, width: leftCollumnSpace, height: leftCollumnFillerHeight
                        fill: leftCollumnFillerColor
                     }
                     Label {
                        text: ##"ELO"
                     }
                  ]
               }
               GridRow {
                  cells: [
                     baseEloInfo
                  ]
               }
               GridRow {
                  cells: [
                     Rectangle {
                        x: 0, y: 0, width: leftCollumnSpace, height: leftCollumnFillerHeight
                        fill: leftCollumnFillerColor
                     }
                     Line {
                        startX: separatorlineWidth, startY: 0
                        endX:bind separatorLineLength, endY: 0
                        strokeWidth: separatorlineWidth
                        stroke: separatorLineColor
                     }
                  ]
               }
               GridRow {
                  cells: [
                     Rectangle {
                        x: 0, y: 0, width: leftCollumnSpace, height: leftCollumnFillerHeight
                        fill: leftCollumnFillerColor
                     }
                     Label {
                        text: ##"Search for"
                        layoutInfo: GridLayoutInfo { hspan: 2 }
                     }
                  ]
               }
               GridRow {
                  cells: [
                     Rectangle {
                        x: 0, y: 0, width: leftCollumnSpace, height: leftCollumnFillerHeight
                        fill: leftCollumnFillerColor
                     }
                     searchersList
                  ]
               }
               GridRow {
                  cells: [
                     Rectangle {
                        x: 0, y: 0, width: leftCollumnSpace, height: leftCollumnFillerHeight
                        fill: leftCollumnFillerColor
                     }
                     Line {
                        startX: separatorlineWidth, startY: 0
                        endX:bind separatorLineLength, endY: 0
                        strokeWidth: separatorlineWidth
                        stroke: separatorLineColor
                     }
                  ]
               }
               GridRow {
                  cells: [
                     Rectangle {
                        x: 0, y: 0, width: leftCollumnSpace, height: leftCollumnFillerHeight
                        fill: leftCollumnFillerColor
                     }
                     foundLabel = Label {
                           text: ""
                           layoutInfo: GridLayoutInfo { hspan: 2 }
                        }
                  ]
               }
               GridRow {
                  cells: [
                     Rectangle {
                        x: 0, y: 0, width: leftCollumnSpace, height: leftCollumnFillerHeight
                        fill: leftCollumnFillerColor
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
                     moreEloInfo
                  ]
               }
               GridRow {
                  cells: [
                     Rectangle {
                        x: 0, y: 0, width: leftCollumnSpace, height: leftCollumnFillerHeight
                        fill: leftCollumnFillerColor
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
      listCell = MultiSelectListCell {
            node: Label {
               text: bind "{(listCell.item as EloBasedSearcher).getDisplayId()}"
            }
         }
   }

   public override function showSearching(): Void {
      delete  resultsListView.items;
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
      Math.max(grid.getPrefWidth(w),600.0);
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

   public function sizeChanged(): Void {
      separatorLineLength = width - 3 * spacing - leftCollumnSpace - 3*separatorlineWidth;
      Container.resizeNode(grid, width, height);
   }

   function printGridSizes(label: String): Void {
   //      println("grid {label} change: size {grid.width}*{grid.height}, ""pref {grid.getPrefWidth(grid.width)}*{grid.getPrefHeight(grid.height)}, ""min {grid.getMinWidth()}*{grid.getMinHeight()}");
   //      var square = width * height;
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
