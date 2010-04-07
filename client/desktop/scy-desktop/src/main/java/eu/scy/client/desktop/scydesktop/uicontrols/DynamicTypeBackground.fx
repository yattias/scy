/*
 * DynamicTypeBackground.fx
 *
 * Created on 15-mrt-2010, 16:26:45
 */
package eu.scy.client.desktop.scydesktop.uicontrols;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.art.EloImageInformation;
import eu.scy.client.desktop.scydesktop.art.ArtSource;

/**
 * @author sikken
 */
public class DynamicTypeBackground extends CustomNode {

   public-init var sourceName = ArtSource.selectedIconsPackage;
   public var type: String on replace {
         typeChanged()
      };
   public-init var elements = [
         BackgroundElement {
            scale: 3
            xPos: 80
            yPos: 70
         }
         BackgroundElement {
            scale: 4
            xPos: 40
            yPos: 380
         }
         BackgroundElement {
            scale: 2.5
            xPos: 100
            yPos: 690
         }
         BackgroundElement {
            scale: 2.5
            xPos: 260
            yPos: 260
         }
         BackgroundElement {
            scale: 4
            xPos: 390
            yPos: 480
         }
         BackgroundElement {
            scale: 2.5
            xPos: 550
            yPos: 650
         }
         BackgroundElement {
            scale: 3
            xPos: 490
            yPos: 110
         }
         BackgroundElement {
            scale: 3
            xPos: 680
            yPos: 440
         }
         BackgroundElement {
            scale: 2.5
            xPos: 750
            yPos: 210
         }
         BackgroundElement {
            scale: 3
            xPos: 900
            yPos: 140
         }
         BackgroundElement {
            scale: 4
            xPos: 890
            yPos: 690
         }
      ];
   public-init var iconOpacity = 0.1;
   public-init var iconEffect = GaussianBlur {
         radius: 1
      };
   def background = Background {
         //sourceUrl: sourceUrl;
         sourceName:sourceName
         elements: elements;
         iconOpacity: iconOpacity
         iconEffect: iconEffect
      }
   def backgroundGray = Color.web("#EAEAEA");

   function typeChanged(): Void {
      background.specification = typeToBackgroundSpecification(type);
   }

   function typeToBackgroundSpecification(type: String): BackgroundSpecification {
      var iconName = EloImageInformation.getIconName(type);
      if (iconName == null) {
         iconName = EloImageInformation.generalLogo.iconName;
      }
      BackgroundSpecification {
         backgroundColor: backgroundGray;
         iconName: iconName
      }
   }

   public override function create(): Node {
      typeChanged();
      return Group {
            content: background
         };
   }

}
