/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.colorscheme;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.art.eloicons.EloIconFactory;
import javafx.scene.layout.VBox;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import javafx.scene.control.Label;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import javafx.scene.Group;

/**
 * @author SikkenJ
 */
public class EloIconDisplay extends CustomNode {

   public-init var eloIconFactory: EloIconFactory;
   public var eloIconName: String on replace { updateEloIcon(true) }
   public var windowColorScheme: WindowColorScheme on replace { updateEloIcon(false) };
   public var eloIcon: EloIcon;

   function updateEloIcon(eloIconNameChanged: Boolean): Void {
      if (eloIconNameChanged) {
         eloIcon = eloIconFactory.createEloIcon(eloIconName);
      }
      eloIcon.windowColorScheme = windowColorScheme;
      println("updateEloIcon({eloIconNameChanged}): eloIcon: {eloIcon} with wcs: {windowColorScheme.colorSchemeId}");
   }

   public override function create(): Node {
      println("creating EloIconDisplay");
      updateEloIcon(true);
      VBox {
         content: [
            eloIcon,
            Label {
               text: bind eloIconName
            }
         ]
      }
   }

}
