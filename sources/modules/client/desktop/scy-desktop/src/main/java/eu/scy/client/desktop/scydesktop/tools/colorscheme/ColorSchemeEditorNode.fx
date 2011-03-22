/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.colorscheme;

import javafx.scene.CustomNode;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.net.URI;
import org.apache.log4j.Logger;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.art.WindowColorSchemes;
import eu.scy.common.mission.ColorSchemesElo;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import roolo.elo.api.IELO;
import eu.scy.client.desktop.scydesktop.art.eloicons.EloIconFactory;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButton;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButtonManager;
import eu.scy.client.desktop.scydesktop.utils.EmptyBorderNode;

/**
 * @author SikkenJ
 */
public class ColorSchemeEditorNode extends CustomNode, ScyToolFX, EloSaverCallBack {

   def logger = Logger.getLogger(this.getClass());
   public var toolBrokerAPI: ToolBrokerAPI on replace {
         eloFactory = toolBrokerAPI.getELOFactory();
         metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
         repository = toolBrokerAPI.getRepository();
      };
   public var eloFactory: IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository: IRepository;
   public var window: ScyWindow;
   public var eloIconFactory: EloIconFactory;
   def spacing = 5.0;
   var buttonBox: HBox;
   var eloUri: URI;
   var colorSchemesElo: ColorSchemesElo;
   def windowColorSchemeEditorNode: WindowColorSchemeEditorNode = WindowColorSchemeEditorNode {
         eloIconFactory: eloIconFactory
         colorChanged: colorChanged
      }
   var technicalFormatKey: IMetadataKey;
   def eloIconName = bind windowColorSchemeEditorNode.selectedEloIconNamne as String on replace { eloIconNameChanged() };
   def saveTitleBarButton = TitleBarButton {
              actionId: "save"
              iconType: "save"
              action: doSaveElo
              tooltip: "save ELO"
           }
   def saveAsTitleBarButton = TitleBarButton {
              actionId: "saveAs"
              iconType: "save_as"
              action: doSaveAsElo
              tooltip: "save copy of ELO"
           }

   public override function initialize(windowContent: Boolean): Void {
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      eloFactory = toolBrokerAPI.getELOFactory();
      metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
      repository = toolBrokerAPI.getRepository();
      FX.deferAction(function():Void{
         windowColorSchemeEditorNode.selectDefaults();
         colorChanged();

      });
   }

   public override function setTitleBarButtonManager(titleBarButtonManager: TitleBarButtonManager, windowContent: Boolean): Void {
//      println("setTitleBarButtonManager({titleBarButtonManager},{windowContent})");
//      println("saveTitleBarButton: {saveTitleBarButton}, saveAsTitleBarButton: {saveAsTitleBarButton}");
      if (windowContent) {
         titleBarButtonManager.titleBarButtons = [
                    saveTitleBarButton,
                    saveAsTitleBarButton
                 ]
      }
   }

   function eloIconNameChanged(): Void {
      if (eloIconName != "") {
         def eloIcon = eloIconFactory.createEloIcon(eloIconName);
         eloIcon.windowColorScheme = window.windowColorScheme;
         eloIcon.selected = true;
         window.eloIcon = eloIcon;
      }
   }

   function colorChanged(): Void {
      window.windowColorScheme.assign(windowColorSchemeEditorNode.selectedWindowColorScheme);
   }

   public override function loadElo(uri: URI) {
      doLoadElo(uri);
   }

   public override function newElo() {
      windowColorSchemeEditorNode.windowColorSchemes = WindowColorSchemes.getStandardWindowColorSchemes();
   }

   public override function onQuit(): Void {
      if (colorSchemesElo != null) {
         def oldContentXml = colorSchemesElo.getContent().getXmlString();
         def newContentXml = getElo().getContent().getXmlString();
         if (oldContentXml == newContentXml) {
            // nothing changed
            return;
         }
      }
      doSaveElo();
   }

   public override function create(): Node {
      //      resizeContent();
      //      FX.deferAction(resizeContent);
//      VBox {
//         blocksMouse: true
//         managed: false
//         spacing: spacing;
//         content: [
//            buttonBox = HBox {
//                  spacing: spacing;
//                  padding: Insets {
//                     left: spacing
//                     top: spacing
//                     right: spacing
//                  }
//                  content: [
//                     Button {
//                        text: "Save"
//                        action: function() {
//                           doSaveElo();
//                        }
//                     }
//                     Button {
//                        text: "Save as"
//                        action: function() {
//                           doSaveAsElo();
//                        }
//                     }
//                  ]
//               }
//            windowColorSchemeEditorNode
//         ]
//      }
      EmptyBorderNode{
         borderSize: 5.0
         content:windowColorSchemeEditorNode
      }
//
//      windowColorSchemeEditorNode
   }

   function doLoadElo(eloUri: URI) {
      logger.info("Trying to load elo {eloUri}");
      var newColorSchemesElo = ColorSchemesElo.loadElo(eloUri, toolBrokerAPI);
      if (newColorSchemesElo != null) {
         def colorSchemes = newColorSchemesElo.getTypedContent().getColorSchemes();
         def windowColorSchemes = WindowColorSchemes.getWindowColorSchemes(colorSchemes);
         windowColorSchemeEditorNode.windowColorSchemes = windowColorSchemes;
         colorSchemesElo = newColorSchemesElo;
      }
   }

   function doSaveElo() {
      eloSaver.eloUpdate(getElo().getUpdatedElo(), this);
   }

   function doSaveAsElo() {
      eloSaver.eloSaveAs(getElo().getUpdatedElo(), this);
   }

   function getElo(): ColorSchemesElo {
      if (colorSchemesElo == null) {
         colorSchemesElo = ColorSchemesElo.createElo(toolBrokerAPI);
      }
      def colorSchemes = windowColorSchemeEditorNode.windowColorSchemes.getColorSchemes();
      colorSchemesElo.getTypedContent().setColorSchemes(colorSchemes);
      return colorSchemesElo;
   }

   public override function eloSaved(elo: IELO): Void {
      colorSchemesElo = new ColorSchemesElo(elo, toolBrokerAPI);
   }

   public override function eloSaveCancelled(elo: IELO) {

   }

}
