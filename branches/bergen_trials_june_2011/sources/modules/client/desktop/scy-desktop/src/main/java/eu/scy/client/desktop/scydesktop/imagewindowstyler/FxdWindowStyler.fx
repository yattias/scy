/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.imagewindowstyler;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.desktoputils.art.EloImageInformation;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.scywindows.window.CharacterEloIcon;
import java.util.HashMap;
import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.client.desktop.desktoputils.art.FxdImageLoader;
import eu.scy.client.desktop.desktoputils.art.ArtSource;
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
               sourceName: ArtSource.plainIconsPackage
            };
      }

      for (eloImageInformation in EloImageInformation.values()) {
         def fxdNode = imageLoader.getNode(eloImageInformation.iconName);
         fxdNode.visible = true;
         fxdNodesMap.put(eloImageInformation.type, fxdNode);
      }
   }

   public override function getWindowColorScheme(type:String):WindowColorScheme{
      var scyColors = EloImageInformation.getScyColors(type);
      if (scyColors==null){
         scyColors = ScyColors.darkGray;
      }
      return WindowColorScheme.getWindowColorScheme(scyColors);
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
         color: getWindowColorScheme(type).mainColor
      };
   }

}
