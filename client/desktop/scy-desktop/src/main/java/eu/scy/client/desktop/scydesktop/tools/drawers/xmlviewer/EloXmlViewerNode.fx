/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.Resizable;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import javafx.scene.control.TextBox;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.layout.Priority;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.layout.Container;
import javafx.util.Math;
import java.net.URI;
import roolo.api.IRepository;
import javafx.scene.Group;
import javafx.geometry.Insets;
import javafx.geometry.VPos;

/**
 * @author SikkenJ
 */
// place your code here
public class EloXmlViewerNode extends CustomNode, Resizable, ScyToolFX {

   public override var width on replace {sizeChanged()};
   public override var height on replace {sizeChanged()};
   public var repository: IRepository;
   def spacing = 5.0;
   def minimumWidth = 150;
   def minimumHeight = 100;
   def textBox: TextBox = TextBox {
         multiline: true
         lines: 3
         editable: false
         layoutInfo: LayoutInfo {
            hfill: true
            vfill: true
            hgrow: Priority.ALWAYS
            vgrow: Priority.ALWAYS
         }
      }
   var nodeBox: HBox;
   var buttonBox: HBox;
   var eloUri: URI;

   public override function create(): Node {
      return nodeBox = HBox {
               managed: false
               spacing: spacing;
               padding: Insets {
                  left: spacing
               }
               nodeVPos:VPos.CENTER
               content: [
                  Group{
                     content:[
                        Button {
                           text: "Update"
                           action: showXml
                           rotate: 90 
                        }
                     ]
                  }
                  textBox
               ]
            };
   }

   function sizeChanged(): Void {
      Container.resizeNode(nodeBox, width, height);
   }

   public override function getPrefHeight(h: Number): Number {
      Math.max(minimumHeight, Math.max(nodeBox.getPrefHeight(h), textBox.boundsInParent.minY + textBox.getPrefHeight(h)));
   }

   public override function getPrefWidth(w: Number): Number {
      Math.max(minimumWidth, textBox.boundsInParent.minX + textBox.getPrefWidth(w));
   }

   public override function getMinHeight(): Number {
      Math.max(minimumHeight, Math.max(nodeBox.getMinHeight(), textBox.boundsInParent.minY + textBox.getMinHeight()));
   }

   public override function getMinWidth(): Number {
      Math.max(minimumWidth, Math.max(buttonBox.getMinWidth(), textBox.getMinWidth()))
   }

   public override function loadedEloChanged(uri: URI): Void {
      eloUri = uri;
   }

   function showXml(): Void {
      var xml: String;
      if (repository == null) {
         xml = "Repository is not defined";
      } else if (eloUri == null) {
         xml = "ELO uri is not defined";
      }
      else {
         var elo = repository.retrieveELO(eloUri);
         if (elo == null) {
            xml = "ELO does not exists.\n\nURI: {eloUri}";
         } else {
            xml = elo.getXml();
         }
      }
      textBox.text = xml;
   }

}
