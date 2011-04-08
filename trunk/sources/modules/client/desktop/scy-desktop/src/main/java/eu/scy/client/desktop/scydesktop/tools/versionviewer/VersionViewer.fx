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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import eu.scy.common.scyelo.ScyElo;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.ExtendedScyEloDisplayNode;

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
              versionKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.VERSION);
           };
   public var window: ScyWindow;
   public var scyDesktop: ScyDesktop;
   var repository: IRepository;
   var metadataTypeManager: IMetadataTypeManager;
   var identifierKey: IMetadataKey;
   var versionKey: IMetadataKey;
   def spacing = 5.0;
   def versionListViewer = ListView {
              items: []
              cellFactory: scyEloCellFactory
              layoutInfo: LayoutInfo {
                 height: 75.0
                 hfill: true
                 vfill: true
                 hgrow: Priority.ALWAYS
                 vgrow: Priority.ALWAYS
              }
           }
   def selectedItem = bind versionListViewer.selectedItem on replace {
              if (selectedToggle == versionsRadioButton) {
                 selectedVersionElo = selectedItem as ScyElo;
                 updateFromEloForked();
              } else if (selectedToggle.value == forksRadioButton) {
                 selectedForkedElo = selectedItem as ScyElo;
              }
           }
   var versionsRadioButton: RadioButton;
   var forksRadioButton: RadioButton;
   def versionForksToggle = ToggleGroup {
           }
   def selectedToggle = bind versionForksToggle.selectedToggle on replace {
              if (selectedToggle == versionsRadioButton) {
                 updateVersionsDisplay();
              } else if (selectedToggle == forksRadioButton) {
                 updateForksDisplay();
              }
           }
   var nodeBox: Container;
   var loadedEloUri: URI;
   var versionElos: ScyElo[];
   var forkedElos: ScyElo[];
   var selectedVersionElo: ScyElo;
   var selectedForkedElo: ScyElo;
   var fromEloForked: ScyElo;
   var openFromEloForked: Button;

   public function scyEloCellFactory(): ListCell {
      var listCell: ListCell;
      listCell = ListCell {
                 node: ScyEloVersionCellDisplay {
                    tooltipManager: window.tooltipManager
                    newEloCreationRegistry: scyDesktop.newEloCreationRegistry
                    windowStyler: scyDesktop.windowStyler
                    windowColorScheme: window.windowColorScheme
                    versionKey: versionKey
                    item: bind listCell.item
                 }
              }
      return listCell;
   }

   public override function create(): Node {
      nodeBox = VBox {
                 managed: false
                 spacing: spacing
                 padding: Insets {
                    top: spacing
                 }
                 content: [
                    HBox {
                       spacing: spacing
                       padding: Insets {
                          right: spacing
                          left: spacing
                       }
                       content: [
                          Button {
                             text: "view"
                             disable: bind selectedVersionElo == null or selectedToggle != versionsRadioButton or loadedEloUri == selectedVersionElo.getUri()
                             action: viewSelectedVersionAction
                          }
                          Button {
                             text: "last"
                             disable: bind loadedEloUri == null or true
                             action: viewLastVersionAction
                          }
                          Button {
                             text: "open"
                             disable: bind selectedForkedElo == null or selectedToggle != forksRadioButton
                             action: openSelectedForkedEloAction
                          }
                          openFromEloForked = Button {
                                     text: "open forked from"
                                     disable: bind fromEloForked == null
                                     action: openFromFromEloAction
                                  }
                          versionsRadioButton = RadioButton {
                                     text: "versions"
                                     toggleGroup: versionForksToggle
                                     selected: true
                                  }
                          forksRadioButton = RadioButton {
                                     text: "forked by"
                                     toggleGroup: versionForksToggle
                                     disable: bind selectedVersionElo == null
                                  }
                       ]
                    }
                    Label {
                       text: bind "Current: {loadedEloUri}"
                    }
                    versionListViewer
                 ]
              }
   }

   function viewSelectedVersionAction(): Void {
      def uriToShow = selectedVersionElo.getUri();
      window.scyToolsList.loadElo(uriToShow);
      window.scyToolsList.loadedEloChanged(uriToShow);
   }

   function viewLastVersionAction(): Void {

   }

   function openSelectedForkedEloAction(): Void {
      openElo(selectedForkedElo)
   }

   function openFromFromEloAction(): Void {
      openElo(fromEloForked)
   }

   function openElo(elo: ScyElo) {
      if (elo!=null){
         scyDesktop.scyWindowControl.addOtherScyWindow(elo.getUri());
      }
   }

   function loadEloLists(): Void {
      delete  versionElos;
      delete  forkedElos;
      if (loadedEloUri != null) {
         def metadatas = repository.retrieveMetadataAllVersions(loadedEloUri);
         versionElos =
                 for (metadata in metadatas) {
                    new ScyElo(metadata, toolBrokerAPI)
                 }
      }
   }

   function updateVersionsDisplay() {
      versionListViewer.items = versionElos;
      selectedVersionElo = null;
      selectedForkedElo = null;
      fromEloForked = null;
   }

   function updateFromEloForked() {
      def fromEloForkedUri = selectedVersionElo.getIsForkedOfEloUri();
      if (fromEloForkedUri == null) {
         fromEloForked = null;
         openFromEloForked.tooltip = null;
      } else {
         fromEloForked = ScyElo.loadMetadata(fromEloForkedUri, toolBrokerAPI);
         def eloIcon = scyDesktop.windowStyler.getScyEloIcon(fromEloForked);
         openFromEloForked.tooltip = Tooltip {
                    graphic: ExtendedScyEloDisplayNode {
                       scyElo: fromEloForked
                       newEloCreationRegistry: scyDesktop.newEloCreationRegistry
                       eloIcon: eloIcon
                    }
                 }
      }

   }

   function updateForksDisplay() {
      //      println("updateForksDisplay: selectedVersionElo:{selectedVersionElo}");
      forkedElos =
              for (uri in selectedVersionElo.getIsForkedByEloUris()) {
                 ScyElo.loadMetadata(uri, toolBrokerAPI);
              }
      versionListViewer.items = forkedElos;
   }

   public override function loadedEloChanged(eloUri: URI): Void {
      loadedEloUri = eloUri;
      loadEloLists();
      versionsRadioButton.selected = true;
      updateVersionsDisplay();
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
