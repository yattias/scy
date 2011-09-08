/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search;

import javafx.geometry.Insets;
import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Container;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Resizable;
import javafx.scene.layout.Stack;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Math;
import com.javafx.preview.layout.Grid;
import com.javafx.preview.layout.GridLayoutInfo;
import com.javafx.preview.layout.GridRow;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import eu.scy.common.scyelo.ScyElo;
import java.text.DateFormat;
import java.util.Date;

/**
 * @author SikkenJ
 */
public abstract class GridSearchResultsNode extends CustomNode, Resizable, ScyEloListCellDisplay, ShowSearching {

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
   def leftCollumnFillerHeight = 1.0;
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
              onMouseClicked: function(e: MouseEvent): Void {
                 if (e.clickCount == 2 and selectedSearchResult != null) {
                    openAction()
                 }
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
   def dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
   def foundOnDateLabelText = ##"on";
   def foundByLabelText = ##"by";
   def foundAndLabelText = ##"and";
   def foundAndOthersLabelText = ##"and others";
   var nrFoundString = "";
   var dateFoundString = "";
   public-read def selectedSearchResult = bind resultsListView.selectedItem as ScySearchResult on replace {
              //         openButton.disable = selectedSearchResult == null;
              //         baseButton.disable = selectedSearchResult == null;
              moreEloInfo.scyElo = selectedSearchResult.getScyElo();
              moreEloInfo.relevance = selectedSearchResult.getRelevance();
              moreEloInfo.eloIcon = (selectedSearchResult.getEloIcon() as EloIcon).clone();
           };
   public var openAction: function(): Void;
   public var cancelAction: function(): Void;
   public var useAsBaseAction: function(scyElo: ScyElo): Void;
   protected def openButton = Button {
              disable: bind selectedSearchResult == null
              text: ##"Open"
              action: function() {
                 openAction();
              }
           }
   protected def useAsBaseButton = Button {
              disable: bind selectedSearchResult == null
              text: ##"Use as base"
              action: function() {
                 useAsBaseAction(selectedSearchResult.getScyElo());
              }
           }
   protected def cancelButton = Button {
              text: ##"Cancel"
              action: function() {
                 cancelAction();
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
            vgrow: Priority.NEVER
            vshrink: Priority.NEVER
            vfill: false
            cells: [
               getLeftColumnFiller(),
               foundLabel = Label {
                          text: ""
                          layoutInfo: GridLayoutInfo {
                             hspan: 2
                             vgrow: Priority.NEVER
                             vshrink: Priority.NEVER
                          }
                       }
            ]
         }
         GridRow {
            vgrow: Priority.ALWAYS
            cells: [
               getLeftColumnFiller(),
               Stack {
                  content: [
                     resultsListView,
                     progressIndicator
                  ]
               }
            ]
         }
         GridRow {
            vgrow: Priority.NEVER
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

   public function setDateFound(dateMillis: Long) {
      dateFoundString = dateFormat.format(new Date(dateMillis));
      setFoundLabelText()
   }

   function setNumberOfResults(nr: String): Void {
      nrFoundString = nr;
      setFoundLabelText()
   }

   protected function getByAuthors(): String[] {
      []
   }

   function setFoundLabelText() {
      var byAuthorsText = "";
      def authorsList = getByAuthors();
      if (sizeof authorsList > 0) {
         byAuthorsText = " {foundByLabelText} {authorsList[0]}";
         if (sizeof authorsList > 1) {
            if (sizeof authorsList == 2) {
               byAuthorsText += " {foundAndLabelText} {authorsList[1]}"
            } else {
               byAuthorsText += " {foundAndOthersLabelText}"
            }
         }
      }
      foundLabel.text = "{foundLabelText}:  {nrFoundString}  -  {foundOnDateLabelText}  {dateFoundString}{byAuthorsText}";
   }

//   public override function getPrefWidth(w: Number): Number {
//      Math.max(grid.getPrefWidth(w), minimumWidth);
//   }
//
//   public override function getPrefHeight(h: Number): Number {
//      Math.max(grid.getPrefHeight(h),minimumHeight);
//   }
//
//   public override function getMinWidth(): Number {
//      def minWidth = Math.max(grid.getMinWidth(), minimumWidth);
//      println("minWidth: {minWidth}");
//      minWidth
//   }
//
//   public override function getMinHeight(): Number {
//      Math.max(grid.getMinHeight(),minimumHeight);
//   }
//
   public function sizeChanged(): Void {
      separatorLineLength = width - 3 * spacing - leftCollumnSpace - 3 * separatorlineWidth;
      Container.resizeNode(grid, width, height);
   }

   function printGridSizes(label: String): Void {
   //      println("grid {label} change: size {grid.width}*{grid.height}, ""pref {grid.getPrefWidth(grid.width)}*{grid.getPrefHeight(grid.height)}, ""min {grid.getMinWidth()}*{grid.getMinHeight()}");
   //      var square = width * height;
   }

}

