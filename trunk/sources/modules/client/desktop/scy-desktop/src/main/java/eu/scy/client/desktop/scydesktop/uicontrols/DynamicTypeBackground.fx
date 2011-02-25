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
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;

/**
 * @author sikken
 */
public class DynamicTypeBackground extends CustomNode {

   public var eloIcon:EloIcon on replace{
      eloIconChanged()
   }
   public-init var elements = [
         BackgroundElement {
            scale: 1.5
            xPos: 80
            yPos: 70
         }
         BackgroundElement {
            scale: 2
            xPos: 40
            yPos: 380
         }
         BackgroundElement {
            scale: 1.25
            xPos: 100
            yPos: 690
         }
         BackgroundElement {
            scale: 1.25
            xPos: 260
            yPos: 260
         }
         BackgroundElement {
            scale: 2
            xPos: 390
            yPos: 480
         }
         BackgroundElement {
            scale: 1.25
            xPos: 550
            yPos: 650
         }
         BackgroundElement {
            scale: 1.5
            xPos: 490
            yPos: 110
         }
         BackgroundElement {
            scale: 1.5
            xPos: 680
            yPos: 440
         }
         BackgroundElement {
            scale: 1.25
            xPos: 750
            yPos: 210
         }
         BackgroundElement {
            scale: 1.5
            xPos: 900
            yPos: 140
         }
         BackgroundElement {
            scale: 2
            xPos: 890
            yPos: 690
         }
      ];
   public-init var iconOpacity = 0.1;
   public-init var iconEffect = GaussianBlur {
         radius: 1
      };
   def backgroundGray = Color.web("#EAEAEA");
   def background = Background {
         elements: elements;
         iconOpacity: iconOpacity
         iconEffect: iconEffect
         specification: BackgroundSpecification{
          backgroundColor: backgroundGray
         }

      }

   function eloIconChanged():Void{
      background.specification = BackgroundSpecification{
          backgroundColor: backgroundGray;
          eloIcon: eloIcon
      }
   }

   public override function create(): Node {
      eloIconChanged();
      return Group {
            content: background
         };
   }

}
