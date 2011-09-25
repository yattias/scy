/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import java.net.URI;
import org.apache.log4j.Logger;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.lang.StringBuilder;
import java.lang.Exception;
import javafx.scene.control.TextBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.File;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.ImageView;
import eu.scy.client.desktop.desktoputils.art.ArtSource;
import javafx.ext.swing.SwingUtils;
import eu.scy.client.desktop.desktoputils.art.javafx.NoThumbnailView;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import javafx.util.Math;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.common.scyelo.ScyElo;
import javafx.util.Sequences;
import javafx.scene.control.ListCell;
import java.io.FileNotFoundException;
import eu.scy.client.common.scyi18n.UriLocalizer;

/**
 * @author SikkenJ
 */
public class LasInfoDisplay extends CustomNode {

   def logger = Logger.getLogger(this.getClass());
   public var las: LasFX on replace { lasChanged() };
   public var windowColorScheme: WindowColorScheme;
   public var tbi: ToolBrokerAPI;
   public var openElo: function(scyElo: ScyElo, las: LasFX): Void;
   def spacing = 5.0;
   def fontFamily = "Verdana";
   def titleFontSize = 18.0;
   def sectionFontSize = 12.0;
   def contentFontSize = 11.0;
   def width = 300.0;
   def lineLength = width - 2 * spacing;
   def lineColor = windowColorScheme.mainColor;
   var lastModifiedElosList: ListView;
   def selectedLastModifiedElo = bind lastModifiedElosList.selectedItem as ScyElo on replace { lastModifiedEloSelected() }
   def uriLocalizer = new UriLocalizer();
   def showOnlyAnchorElos = true;

   public override function create(): Node {
      def progressDisplay: Node = ProgressDisplay {
                 fillColor: windowColorScheme.mainColorLight
                 borderColor: windowColorScheme.mainColor
                 progress: Math.random()
              }
      var thumbnail: Node;
      def thumbnailImage = las.mainAnchor.scyElo.getThumbnail();
      if (thumbnailImage != null) {
         thumbnail = ImageView {
                    fitWidth: ArtSource.thumbnailWidth
                    fitHeight: ArtSource.thumbnailHeight
                    preserveRatio: true
                    image: SwingUtils.toFXImage(thumbnailImage)
                 }
      } else {
         thumbnail = NoThumbnailView {
                    windowColorScheme: windowColorScheme
                 }
      }
      VBox {
         spacing: spacing
         padding: Insets {
            top: spacing
            right: spacing
            bottom: spacing
            left: spacing
         }
         layoutInfo: LayoutInfo {
            width: width
         }
         content: [
            HBox {
               spacing: spacing
               padding: Insets {
                  top: spacing
                  right: spacing
                  bottom: spacing
                  left: spacing
               }
               nodeVPos: VPos.BASELINE
               content: [
                  Label {
                     layoutInfo: LayoutInfo {
                        hgrow: Priority.ALWAYS
                        hfill: true
                     }
                     font: Font.font(fontFamily, FontWeight.BOLD, titleFontSize)
                     text: las.title
                  }
               //                  Group {
               //                     content: progressDisplay
               //                  }
               ]
            }
            Line {
               startX: 0, startY: 0
               endX: lineLength, endY: 0
               strokeWidth: 1
               stroke: lineColor
            }

            HBox {
               spacing: spacing
               layoutInfo: LayoutInfo {
                  height: thumbnail.layoutBounds.height
               }
               content: [
                  TextBox {
                     layoutInfo: LayoutInfo {
                     }
                     font: Font.font(fontFamily, FontWeight.REGULAR, contentFontSize)
                     text: getTextFromUri(las.mainAnchor.targetDescriptionUri)
                     multiline: true
                     selectOnFocus: false
                     editable: false
                  }
                  thumbnail
               ]
            }
            Line {
               startX: 0, startY: 0
               endX: lineLength, endY: 0
               strokeWidth: 1
               stroke: lineColor
            }
            Label {
               translateX: spacing
               font: Font.font(fontFamily, FontWeight.BOLD, sectionFontSize)
               text: ##"My ELO list (last modified):"
            }
            lastModifiedElosList = ListView {
                       layoutInfo: LayoutInfo {
                          height: 100
                       }
                       cellFactory: simpleScyEloCellFactory
                       items: getLastModifiedElos()
                    //                  items: ['first ideas <2 hours ago>', 'last ideas <yesterday>', "CO2 concept map"]
                    }
         ]
      }
   }

   function getTextFromUri(uri: URI): String {
      //      logger.debug("retrieving text from uri: {uri}");
      def localizedUri = uriLocalizer.localizeUriWithChecking(uri);
      if (localizedUri == null) {
         return "no targetDescriptionUri defined"
      }
      var reader: BufferedReader;
      try {
         reader = new BufferedReader(new InputStreamReader(localizedUri.toURL().openStream(), "UTF-8"));
      } catch (e: FileNotFoundException) {
         logger.warn("cannot find text file: {localizedUri}");
         return "cannot find text file:\n{localizedUri}";
      } catch (e: Exception) {
         logger.warn("failed to open text file: {localizedUri}, {e.getMessage()}");
         return "failed to open text file:\n{localizedUri},\n{e.getMessage()}";
      }

      def builder = new StringBuilder();
      try {
         var line: String;
         while ((line = reader.readLine()) != null) {
            if (builder.length() > 0) {
               builder.append("\n");
            }
            builder.append(line);
         }
      } catch (e: Exception) {
         logger.warn("error reading content of uri: {localizedUri}", e);
         builder.append("\n\nError reading from {localizedUri}, {e.getMessage()}");
      } finally {
         try {
            reader.close();
         } catch (e: Exception) {
            logger.warn("failed to close stream to uri {localizedUri}");
         }
      }
      builder.toString();
   }

   function lasChanged() {
   //      lastModifiedElosList.items = getLastModifiedElos();
   }

   function getLastModifiedElos(): ScyElo[] {
      var scyElos: ScyElo[];
      if (las != null) {
         if (las.mainAnchor.scyElo.getDateFirstUserSave() != null) {
            insert las.mainAnchor.scyElo into scyElos;
         }
         for (intermediate in las.intermediateAnchors) {
            if (intermediate.scyElo.getDateFirstUserSave() != null) {
               insert intermediate.scyElo into scyElos;
            }
         }
         if (not showOnlyAnchorElos) {
            for (eloUri in las.otherEloUris) {
               def scyElo = ScyElo.loadMetadata(eloUri, tbi);
               if (scyElo.getDateFirstUserSave() != null) {
                  insert scyElo into scyElos;
               }
            }
         }
         scyElos = Sequences.sort(scyElos, new ScyEloLastModifiedComparator()) as ScyElo[];
      }
      //      println("nr of last modified elos: {sizeof scyElos}");
      return scyElos;
   }

   public function simpleScyEloCellFactory(): ListCell {
      var listCell: ListCell;
      listCell = ListCell {
                 node: LastModifiedScyEloCellView {
                    scyElo: bind listCell.item as ScyElo
                 }
              }
   }

   function lastModifiedEloSelected() {
      if (selectedLastModifiedElo != null) {
         openElo(selectedLastModifiedElo, las)
      }

   }

}

function run() {
   def las1 = LasFX {
              mainAnchor: MissionAnchorFX {
                 title: "las/anchor title"
              }
              instructionUri: new URI("http://www.scy-lab.eu/content/en/mission1/startPage/Welcome.html")
           }

   def las2 = LasFX {
              mainAnchor: MissionAnchorFX {
                 title: "las/anchor title"
              }
              instructionUri: null
           }

   def styleSheet = "{__DIR__}../../../scy-desktop.css";
   def styleSheetFile = new File(styleSheet);
   println("styleSheetFile: {styleSheetFile.getAbsolutePath()}");
   println("styleSheetFile.parent: {styleSheetFile.getParentFile().getAbsolutePath()}");

   Stage {
      title: "test"
      onClose: function() {
      }
      scene: Scene {
         stylesheets: [
            "{__DIR__}../../scy-desktop.css",
            "{__FILE__}../../../scy-desktop.css",
            "{__DIR__}../../../../scy-desktop.css",
         ]
         width: 400
         height: 400
         content: [
            LasInfoDisplay {
               las: las1
            }
         ]
      }
   }

}
