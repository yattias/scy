/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Resizable;
import javafx.util.Math;
import javafx.scene.layout.Container;

/**
 * @author SikkenJ
 */
public class ListSelectionTool extends CustomNode, Resizable {

   public-init var listItems: Object[];
   public-init var cellFactory: function(): ListCell;
   public-init var titleLabel = "title";
   public-init var okButtonLabel = "Ok";
   public-init var okButtonAction: function(selectedObject: Object): Void;
   public-init var cancelAction: function(): Void;
   public override var width on replace { sizeChanged() };
   public override var height on replace { sizeChanged() };
   var mainVBox: VBox;
   def spacing = 5.0;
   def minimumWidth = 300;
   def minimumHeight = 200;
   def preferredWidth = 400;
   def preferredHeight = 300;
   def listView: ListView = ListView {
              layoutInfo: LayoutInfo {
                 height: 100.0
                 vgrow: Priority.ALWAYS
                 vshrink: Priority.ALWAYS
              }
              items: listItems
              cellFactory: cellFactory
              onKeyTyped: function(event: KeyEvent): Void {
                 if (event.char == '\n') {
                    selectAction();
                 }
              }
              onMouseClicked: function(event: javafx.scene.input.MouseEvent): Void {
                 if (event.clickCount == 2) {
                    selectAction();
                 }
              }
           }

   function selectAction(): Void {
      if (listView.selectedIndex >= 0) {
         okButtonAction(listView.selectedItem);
      }
   }

   public override function create(): Node {
      mainVBox = VBox {
                 managed: false
                 spacing: spacing
                 padding: Insets {
                    top: spacing
                    right: spacing
                    bottom: spacing
                    left: spacing
                 }
                 content: [
                    Label {
                       text: titleLabel
                    }
                    listView,
                    HBox {
                       spacing: spacing
                       padding: Insets {
                          top: spacing
                          right: 0.0
                          bottom: spacing
                          left: spacing
                       }
                       hpos: HPos.RIGHT
                       content: [
                          Button {
                             text: okButtonLabel
                             disable: bind listView.selectedItem == null
                             action: function() {
                                selectAction()
                             }
                          }
                          Button {
                             text: ##"Cancel"
                             action: function() {
                                cancelAction()
                             }
                          }
                       ]
                    }
                 ]
              }
   }

   function sizeChanged(): Void {
      Container.resizeNode(mainVBox, width, height);
   }

   public override function getPrefHeight(h: Number): Number {
      Math.max(preferredHeight, mainVBox.getPrefHeight(h));
   }

   public override function getPrefWidth(w: Number): Number {
      Math.max(preferredWidth, mainVBox.getPrefWidth(w));
   }

   public override function getMinHeight(): Number {
      Math.max(minimumHeight, mainVBox.getMinHeight());
   }

   public override function getMinWidth(): Number {
      Math.max(minimumWidth, mainVBox.getMinWidth())
   }

}
