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
import javafx.geometry.Insets;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import roolo.elo.api.IELOFactory;
import java.lang.Exception;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.DialogBox;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import roolo.elo.api.IELO;
import eu.scy.common.scyelo.ScyElo;
import java.lang.IllegalArgumentException;

/**
 * @author SikkenJ
 */
// place your code here
public class EloXmlViewerNode extends CustomNode, Resizable, ScyToolFX, EloSaverCallBack {

   public override var width on replace { sizeChanged() };
   public override var height on replace { sizeChanged() };
   public var window: ScyWindow;
   public var repository: IRepository;
   public var eloFactory: IELOFactory;
   public var toolBrokerAPI: ToolBrokerAPI on replace {
              repository = toolBrokerAPI.getRepository();
              eloFactory = toolBrokerAPI.getELOFactory();
           };
   def spacing = 5.0;
   def minimumWidth = 150;
   def minimumHeight = 100;
   def textBox: TextBox = TextBox {
              multiline: true
              lines: 3
              editable: bind editable
              layoutInfo: LayoutInfo {
                 hfill: true
                 vfill: true
                 hgrow: Priority.ALWAYS
                 vgrow: Priority.ALWAYS
              }
           }
   var savedXml = "";
   var showingEloXml = false;
   var nodeBox: HBox;
   var eloUri: URI;
   var scyElo: ScyElo;
   def editCheckBox = CheckBox {
              text: "Editable"
              allowTriState: false
              selected: false
              disable: true
           }
   def editable = bind repository != null and eloUri != null and editCheckBox.selected;

   public override function create(): Node {
      return nodeBox = HBox {
                         managed: false
                         spacing: spacing;
                         padding: Insets {
                            left: spacing
                         }
                         content: [
                            VBox {
                               spacing: spacing
                               padding: Insets {
                                  top: spacing
                               }
                               content: [
                                  Button {
                                     text: "Update"
                                     action: showXml
                                  }
                                  editCheckBox,
                                  Button {
                                     text: "Save"
                                     disable: bind not editable or savedXml == textBox.rawText
                                     action: saveXml
                                  }
                                  Button {
                                     text: "Copy all"
                                     disable: bind not showingEloXml
                                     action: copyAll
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
      Math.max(minimumWidth, nodeBox.getMinWidth())
   }

   public override function loadedEloChanged(uri: URI): Void {
      eloUri = uri;
   }

   function showXml(): Void {
      showingEloXml = false;
      editCheckBox.selected = false;
      scyElo = null;
      if (repository == null) {
         savedXml = "Repository is not defined";
      } else if (eloUri == null) {
         savedXml = "ELO uri is not defined";
      } else {
         scyElo = ScyElo.loadElo(eloUri, toolBrokerAPI);
         if (scyElo == null) {
            savedXml = "ELO does not exists.\n\nURI: {eloUri}";
         } else {
            savedXml = scyElo.getElo().getXml();
            showingEloXml = true;
         }
      }
      editCheckBox.disable = not showingEloXml;
      textBox.text = savedXml;
   }

   function saveXml(): Void {
      try {
         def newElo = eloFactory.createELOFromXml(textBox.rawText);
         def newScyElo = new ScyElo(newElo, toolBrokerAPI);
         if (eloUri != newElo.getUri()) {
            throw new IllegalArgumentException("The ELO uri does not match the loaded ELO. \nYou may not change the elo uri.\nOr the loaded ELO has changed, please press the Update button");
         }
         if (scyElo.getTechnicalFormat() != newScyElo.getTechnicalFormat()) {
            throw new IllegalArgumentException("You may not change the technical format");
         }
         eloSaver.eloUpdate(newElo, this);
      } catch (e: Exception) {
         DialogBox.showMessageDialog("An exception occured during the saving of the ELO xml:\n{e.getMessage()}", "Problems with saving ELO xml", null, null, null);
      }
   }

   override public function eloSaveCancelled(elo: IELO): Void {
   }

   override public function eloSaved(elo: IELO): Void {
      editCheckBox.selected = false;
      scyElo = new ScyElo(elo, toolBrokerAPI);
      savedXml = scyElo.getElo().getXml();
      textBox.text = savedXml;
      textBox.positionCaret(0);
   }

   function copyAll():Void{
      textBox.selectAll();
      textBox.copy();
   }


}
