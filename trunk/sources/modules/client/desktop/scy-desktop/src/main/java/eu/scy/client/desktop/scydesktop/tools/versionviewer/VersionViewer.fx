/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.versionviewer;

import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import javafx.scene.Node;
import javafx.scene.layout.Container;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.net.URI;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.layout.Priority;
import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import javafx.scene.control.Label;

/**
 * @author SikkenJ
 */
public class VersionViewer extends CustomNode, Resizable, ScyToolFX {

   public override var width on replace { sizeChanged()
           };
   public override var height on replace { sizeChanged()
           };
   public var toolBrokerAPI: ToolBrokerAPI on replace {
              repository = toolBrokerAPI.getRepository();
              metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
              identifierKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER);
           };
   public var window: ScyWindow;
   var repository: IRepository;
   var metadataTypeManager: IMetadataTypeManager;
   var identifierKey: IMetadataKey;
   def spacing = 5.0;
   def versionListViewer = ListView {
              items: []
              layoutInfo: LayoutInfo {
                 height: 75.0
                 hfill: true
                 vfill: true
                 hgrow: Priority.ALWAYS
                 vgrow: Priority.ALWAYS
              }
           }
   var nodeBox: Container;
   var loadedEloUri: URI;

   public override function create(): Node {
      nodeBox = VBox {
                 managed: false
                 spacing: spacing
                 padding: Insets {
                    top: spacing
                    left: spacing
                 }
                 content: [
                    Label {
                       text: bind "Current: {window.eloUri}"
                    }
                    HBox {
                       spacing: spacing;
                       content: [
                          VBox {
                             spacing: spacing
                             padding: Insets {
                                top: spacing
                                bottom: spacing
                                left: spacing
                             }
                             content: [
                                Button {
                                   text: "view"
                                   disable: bind loadedEloUri == null or versionListViewer.selectedItem==null
                                   action: viewSelectedVersionAction
                                }
                                Button {
                                   text: "last"
                                   disable: bind loadedEloUri == null or true
                                   action: viewLastVersionAction
                                }
                             ]
                          }
                          versionListViewer
                       ]
                    }
                 ]
              }
   }

   function viewSelectedVersionAction(): Void {

   }

   function viewLastVersionAction(): Void {

   }

   function loadVersionList(): Void {
      delete  versionListViewer.items;
      if (loadedEloUri != null) {
         def metadatas = repository.retrieveMetadataAllVersions(loadedEloUri);
         versionListViewer.items =
                 for (metadata in metadatas) {
                    metadata.getMetadataValueContainer(identifierKey).getValue()
                 }
      }
   }

   public override function loadedEloChanged(eloUri: URI): Void {
      loadedEloUri = eloUri;
      loadVersionList();
   }

   function sizeChanged(): Void {
      Container.resizeNode(nodeBox, width, height);
   }

   public override function getPrefHeight(h: Number): Number {
      nodeBox.getPrefHeight(h);
   }

   public override function getPrefWidth(w: Number): Number {
      nodeBox.getPrefWidth(w);
   }

   public override function getMinHeight(): Number {
      nodeBox.getMinHeight();
   }

   public override function getMinWidth(): Number {
      nodeBox.getMinWidth();
   }

   public override function getMaxHeight(): Number {
      nodeBox.getMaxHeight();
   }

   public override function getMaxWidth(): Number {
      nodeBox.getMaxWidth();
   }

}
