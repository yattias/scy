/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.colorscheme;

import javafx.scene.CustomNode;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
   def windowColorSchemeEditorNode = WindowColorSchemeEditorNode {
         eloIconFactory: eloIconFactory
      }
   var technicalFormatKey: IMetadataKey;
   def eloIconName = bind windowColorSchemeEditorNode.selectedEloIconNamne as String on replace { eloIconNameChanged() };

   public override function initialize(windowContent: Boolean): Void {
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      eloFactory = toolBrokerAPI.getELOFactory();
      metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
      repository = toolBrokerAPI.getRepository();
      window.windowColorScheme = windowColorSchemeEditorNode.selectedWindowColorScheme;
   }

   function eloIconNameChanged():Void{
      if (eloIconName!=""){
         def eloIcon = eloIconFactory.createEloIcon(eloIconName);
         eloIcon.windowColorScheme = windowColorSchemeEditorNode.selectedWindowColorScheme;
         eloIcon.selected = true;
         window.eloIcon = eloIcon;
      }
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
      VBox {
         blocksMouse: true
         managed: false
         spacing: spacing;
         content: [
            buttonBox = HBox {
                  spacing: spacing;
                  padding: Insets {
                     left: spacing
                     top: spacing
                     right: spacing
                  }
                  content: [
                     Button {
                        text: "Save"
                        action: function() {
                           doSaveElo();
                        }
                     }
                     Button {
                        text: "Save as"
                        action: function() {
                           doSaveAsElo();
                        }
                     }
                  ]
               }
            windowColorSchemeEditorNode
         ]
      }
   }

   function doLoadElo(eloUri: URI) {
      logger.info("Trying to load elo {eloUri}");
      var newElo = repository.retrieveELO(eloUri);
      if (newElo != null) {
      //         var metadata = newElo.getMetadata();
      //         var text = eloContentXmlToText(newElo.getContent().getXmlString());
      //         logger.info("elo text loaded");
      //         elo = newElo;
      //         FX.deferAction(function(): Void {
      //            textBox.text = text;
      //         });
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
