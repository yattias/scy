/*
 * CharacterEloIcon.fx
 *
 * Created on 10-dec-2009, 10:17:13
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextOrigin;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author sikken
 */

public class CharacterEloIcon extends EloIcon {

   public var iconCharacter = "?";
   public var color = Color.RED;
   public var backgroundColor = Color.WHITE;
   public var iconSize = 16.0;
   public var iconGap = 2.0;
   public var fontSize = 14;
   public var iconFont = Font.font("Verdana", FontWeight.BOLD, fontSize);

   def mainColor = bind if (not selected) color else backgroundColor;
   def bgColor = bind if (not selected) backgroundColor else color;

    public override function create(): Node {
     return Group {
                 content: [
                    Group { // icon for title
               var iconChar:Text;
 					content: [
						Rectangle{
							x: 0
							y: 0
							width: iconSize-1
							height: iconSize-1
							fill: bind bgColor
                     stroke:bind color
						}
						iconChar = Text {
							font: iconFont
                     textOrigin:TextOrigin.BOTTOM;
                     textAlignment:TextAlignment.CENTER
      					x: iconGap,
							y: iconSize
							content: bind iconCharacter.substring(0, 1)
							fill: bind mainColor
						}
					]
 				},
         ]
      };
   }

}

function run(){
      Stage {
      title : "test title bar"
      scene: Scene {
         width: 200
         height: 200
         content: [
            CharacterEloIcon{
               translateX:10;
               translateY:10;
               selected: true;
            }
            CharacterEloIcon{
               iconCharacter:"w"
               selected:false
               translateX:10;
               translateY:50;
            }

         ]
      }
   }
}