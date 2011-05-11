/*
 * LogoEloIcon.fx
 *
 * Created on 10-mei-2010, 15:46:48
 */
package eu.scy.client.desktop.desktoputils.art.javafx;

import eu.scy.client.desktop.desktoputils.art.EloIcon;
import javafx.scene.Node;
import javafx.scene.Group;
import eu.scy.client.desktop.desktoputils.art.eloicons.EloIconBackground;
import eu.scy.client.desktop.desktoputils.art.eloicons.EloIconBorder;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * @author SikkenJ
 */
public class LogoEloIcon extends EloIcon {

   def logo = NotSelectedLogo {
              color: bind windowColorScheme.mainColor
           }

   protected override function sizeChanged(): Void {
      scaleNode(logo);
   }

   override protected function create(): Node {
      scaleNode(logo);
      Group {
         content: [
            EloIconBackground {
               visible: bind selected
               size: bind size
               cornerRadius: cornerRadius
               borderSize: borderSize
            }
            logo,
            EloIconBorder {
               visible: bind selected
               size: bind size
               cornerRadius: cornerRadius
               borderSize: borderSize
               borderColor: bind windowColorScheme.mainColor
            }
         ]
      }
   }

   public override function clone(): EloIcon {
      LogoEloIcon {
         windowColorScheme: windowColorScheme
         selected: selected
         size: size
      }
   }

}

function run() {
   def eloIcon = LogoEloIcon {
              layoutX: 10
              layoutY: 60
           //               size: 20
           }
   eloIcon.size = 20.0;
   Stage {
      title: "Test LogoEloIcon"
      onClose: function() {
      }
      scene: Scene {
         width: 200
         height: 200
         content: [
            LogoEloIcon {
               layoutX: 10
               layoutY: 10
            }
            LogoEloIcon {
               layoutX: 60
               layoutY: 10
               size: 80
            }
            eloIcon
         ]
      }
   }

}
