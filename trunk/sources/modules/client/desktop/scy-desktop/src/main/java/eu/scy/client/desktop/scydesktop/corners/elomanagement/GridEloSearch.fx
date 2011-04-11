/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.LayoutInfo;
import javafx.stage.Stage;
import com.javafx.preview.layout.Grid;
import com.javafx.preview.layout.GridRow;
import javafx.scene.control.TextBox;

/**
 * @author SikkenJ
 */
public class GridEloSearch extends GridSearchNode {

   var searchButton: Button;
   public var queryBox: TextBox;
   public var doSearch: function(gridEloSearch: GridEloSearch): Void;

   init {
   }

   public override function create(): Node {
      grid = Grid {
            managed: false
            hgap: spacing
            vgap: spacing
            padding: gridPadding
            growRows: [0, 0, 0, 0, 1, 0]
            growColumns: [0, 1]
            rows: [
               GridRow {
                  cells: [
                     getLeftColumnFiller(),
                     Label {
                        text: ##"Query"
                     }
                  ]
               }
               GridRow {
                  cells: [
                     getLeftColumnFiller(),
                     HBox {
                        spacing: spacing
                        layoutInfo: LayoutInfo {
                        //                           hpos: HPos.RIGHT
                        }
                        content: [
                           queryBox = TextBox {
                                 text: ""
                                 columns: 40
                                 selectOnFocus: true
                                 action: function() {
                                     if(queryBox.rawText.trim()!=""){
                                        doSearch(this);
                                     }
                                 }
                              }
                           searchButton = Button {
                                 text: ##"Search"
                                 disable: bind queryBox.rawText.trim()==""
                                 action: function() {
                                    doSearch(this);
                                 }
                              }
                        ]
                     }
                  ]
               }
               getDevideLine(),
               getResultDisplayPart(),
               GridRow {
                  cells: [
                     getLeftColumnFiller(),
                     HBox {
                        spacing: spacing
                        layoutInfo: LayoutInfo {
                        //                           hpos: HPos.RIGHT
                        }
                        hpos: HPos.RIGHT
                        content: [
                           openButton,
                           cancelButton
                        ]
                     }
                  ]
               }
            ]
         }
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
            GridEloSearch {
               layoutX: 10
               layoutY: 10
            }
         ]
      }
   }

}
