/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.imagewindowstyler;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.art.EloImageInformation;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.scywindows.window.CharacterEloIcon;
import java.util.HashMap;
import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.client.desktop.scydesktop.art.FxdImageLoader;
import eu.scy.client.desktop.scydesktop.art.ArtSource;
import javafx.scene.Node;
import javafx.fxd.Duplicator;

/**
 * @author SikkenJ
 */

public class FxdWindowStyler extends WindowStyler {

//   public-init var impagesPath = "{__DIR__}images/";
   public var repository: IRepository;
   public var metadataTypeManager: IMetadataTypeManager;
   public var imageLoader: FxdImageLoader;
   def fxdNodesMap = new HashMap();

   init {
      if (imageLoader==null){
         imageLoader = FxdImageLoader{
               sourceName: ArtSource.notSelectedIconsPackage
            };
      }

      for (eloImageInformation in EloImageInformation.values()) {
         def fxdNode = imageLoader.getNode(eloImageInformation.iconName);
         fxdNode.visible = true;
         fxdNodesMap.put(eloImageInformation.type, fxdNode);
      }
   }

   public override function getScyColor(type:String):Color{
      var scyColors = EloImageInformation.getScyColors(type);
      var colorName = ScyColors.darkGray.mainColorName;
      if (scyColors != null) {
         colorName = scyColors.mainColorName;
      }
      return Color.web(colorName);
   }

   public override function getScyColors(type:String):ScyColors{
      var scyColors = EloImageInformation.getScyColors(type);
      return scyColors;
   }

   public override function getWindowColorScheme(type:String):WindowColorScheme{
      var scyColors = EloImageInformation.getScyColors(type);
      if (scyColors==null){
         scyColors = ScyColors.darkGray;
      }
      return WindowColorScheme.getWindowColorScheme(scyColors);
   }

   public override function getScyIconCharacter(type: String): String {
      var iconChar = "?";
//      if (type == drawingType)
//         iconChar = "D" else if (type == datasetType)
//         iconChar = "V" else if (type == simulationConfigType)
//         iconChar = "S" else if (type == datasetProcessingType)
//         iconChar = "P" else if (type == textType)
//         iconChar = "T" else if (type == imageType)
//         iconChar = "I" else if (type == meloType)
//         iconChar = "I" else if (type == mappingType)
//         iconChar = "M" else if (type == urlType)
//         iconChar = "W" else if (type == videoType)
//         iconChar = "V" else if (type == interviewType)
//         iconChar = "I" else if (type == xprocType)
//         iconChar = "X";
      return iconChar;
   }

   public override function getScyEloIcon(type: String): EloIcon {
      def fxdNode = fxdNodesMap.get(type) as Node;
      if (fxdNode!=null){
         return FxdEloIcon{
            selected: false
            fxdNode: Duplicator.duplicate(fxdNode)
            windowColorScheme:getWindowColorScheme(type)
         }
      }
      CharacterEloIcon {
         iconCharacter: getScyIconCharacter(type);
         color: getScyColor(type)
      };
   }

//   public override function style(window: ScyWindow, uri: URI)  {
//      var type = eloTypeControl.getEloType(uri);
//      var windowColorScheme = getWindowColorScheme(type);
//      var eloIcon = getScyEloIcon(type);
//      window.windowColorScheme = windowColorScheme;
//      window.eloIcon = eloIcon;
//   }
//
//   public override function getScyEloIcon(uri: URI): EloIcon {
//      var type = eloTypeControl.getEloType(uri);
//      return getScyEloIcon(type);
//   }

   function createEloIcon(eloImageSet: EloImageSet): EloIcon {
      ImageEloIcon {
         activeImage: eloImageSet.activeImage;
         inactiveImage: eloImageSet.inactiveImage;
      }
   }
}
