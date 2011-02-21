/*
 * LogoEloIcon.fx
 *
 * Created on 10-mei-2010, 15:46:48
 */
package eu.scy.client.desktop.scydesktop.art.javafx;

import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.EloIconBackground;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.EloIconBorder;

/**
 * @author SikkenJ
 */
public class LogoEloIcon extends EloIcon {

   override protected function create(): Node {
      def logo = NotSelectedLogo {
            color: bind windowColorScheme.mainColor
         }
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
