/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.login;

import eu.scy.common.scyelo.ScyElo;
import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import java.util.Locale;
import javafx.scene.image.Image;
import eu.scy.client.desktop.desktoputils.art.ImageLoader;
import org.apache.log4j.Logger;
import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author SikkenJ
 */
public class MissionListCellDisplay extends CustomNode {

   def logger = Logger.getLogger(this.getClass());
   public var mission: ScyElo on replace { missionChanged() };
   public-init var imageLoader: ImageLoader;
   public-init var imageExtension = ".png";
   def flagHeight = 20.0;
   def flagImageView = ImageView {
           }

   init {
      if (imageLoader == null) {
         imageLoader = ImageLoader.getImageLoader();
      }
   }

   override function create(): Node {
      missionChanged();
      HBox {
         spacing: 0.0
         nodeVPos:VPos.CENTER
         content: [
            flagImageView,
            Rectangle {
               x: 0, y: 0
               width: 5.0, height: flagHeight
               fill: Color.TRANSPARENT
            }
            Label {
               text: bind mission.getTitle()
            }
         ]
      }
   }

   function missionChanged() {
      var languageDisplay: Locale = null;
      if (mission != null) {
         languageDisplay = getLanguageToDisplay()
      }

      flagImageView.image = getFlagImage(languageDisplay)
   }

   function getLanguageToDisplay(): Locale {
      def languages = mission.getElo().getLanguages();
      if (languages != null and languages.size() > 0) {
         return languages.get(0)
      }
      return null;
   }

   function getFlagImage(language: Locale): Image {
      if (language == null) {
         return null;
      }
      var flagImage = imageLoader.getImage("flags/{language}{imageExtension}");
      if (not flagImage.error) {
         return flagImage
      } else {
         logger.warn("no flag found for language {language}");
      }
      return null;
   }

}
