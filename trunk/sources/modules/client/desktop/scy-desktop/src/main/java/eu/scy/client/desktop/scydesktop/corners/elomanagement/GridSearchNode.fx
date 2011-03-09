/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Container;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Stack;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Math;
import com.javafx.preview.layout.Grid;
import com.javafx.preview.layout.GridLayoutInfo;
import com.javafx.preview.layout.GridRow;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;
import javafx.scene.control.Button;

/**
 * @author SikkenJ
 */
public abstract class GridSearchNode extends CustomNode, Resizable, ScyEloListCellDisplay, ShowSearching, ModalDialogNode {

   public override var width on replace {
         sizeChanged();
         printGridSizes("width");
      }
   public override var height on replace {
         sizeChanged();
         printGridSizes("height");
      }
   protected var grid: Grid;
   protected def spacing = 5.0;
   protected def gridPadding = Insets {
         top: spacing
         right: spacing
         bottom: spacing
         left: spacing
      }
   def leftCollumnSpace = 10.0;
   def leftCollumnFillerHeight = 5.0;
   def leftCollumnFillerColor = Color.TRANSPARENT;
   def separatorlineWidth = 2.0;
   var separatorLineLength = 10.0;
   def separatorLineColor = Color.BLACK;
   protected def eloInfoLayoutInfo = GridLayoutInfo {
         width: 300
         minWidth: 200
         vfill: true
         hspan: 2
      }
   protected def resultsListView: ListView = ListView {
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
   protected def moreEloInfo = ExtendedScyEloDisplayNode {
         layoutInfo: eloInfoLayoutInfo
         newEloCreationRegistry: newEloCreationRegistry
      }
   def foundLabelText = ##"Found";
   var foundLabel: Label;
   public-read def selectedSearchResult = bind resultsListView.selectedItem as ScySearchResult on replace {
         //         openButton.disable = selectedSearchResult == null;
         //         baseButton.disable = selectedSearchResult == null;
         moreEloInfo.scyElo = selectedSearchResult.getScyElo();
         moreEloInfo.relevance = selectedSearchResult.getRelevance();
         moreEloInfo.eloIcon = (selectedSearchResult.getEloIcon() as EloIcon).clone();
      };
   public var openAction: function(gridSearchNode: GridSearchNode): Void;
   public var cancelAction: function(gridSearchNode: GridSearchNode): Void;
   protected def openButton = Button {
         disable: bind selectedSearchResult == null
         text: ##"Open"
         action: function() {
            openAction(this);
         }
      }
   protected def cancelButton = Button {
         text: ##"Cancel"
         action: function() {
            cancelAction(this);
         }
      }

   init {
      FX.deferAction(function(): Void {
//         searchersList.cellFactory = eloBasedSearcherCellFactory;
         resultsListView.cellFactory = simpleScyEloCellFactory;
         showSearchResult(null);
//         sizeChanged();
      });
   }

   protected function getLeftColumnFiller(): Node {
      Rectangle {
         x: 0, y: 0, width: leftCollumnSpace, height: leftCollumnFillerHeight
         fill: leftCollumnFillerColor
      }
   }

   protected function getDevideLine(): GridRow {
      GridRow {
         cells: [
            getLeftColumnFiller(),
            Line {
               startX: separatorlineWidth, startY: 0
               endX: bind separatorLineLength, endY: 0
               strokeWidth: separatorlineWidth
               stroke: separatorLineColor
            }
         ]
      }
   }

   protected function getResultDisplayPart(): GridRow[] {
      [
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
      ]
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
      Math.max(grid.getPrefWidth(w), 600.0);
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
      separatorLineLength = width - 3 * spacing - leftCollumnSpace - 3 * separatorlineWidth;
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

