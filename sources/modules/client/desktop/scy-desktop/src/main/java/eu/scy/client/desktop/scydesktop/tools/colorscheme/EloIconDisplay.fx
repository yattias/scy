/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.colorscheme;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import eu.scy.client.desktop.desktoputils.art.eloicons.EloIconFactory;
import javafx.scene.layout.VBox;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import javafx.scene.control.Label;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import javafx.scene.Group;
import java.util.HashMap;

/**
 * @author SikkenJ
 */
public class EloIconDisplay extends CustomNode {

   var needToUpdateEloIcon = false;
   public-init var eloIconFactory: EloIconFactory;
   public var eloIconName: String on replace { updateEloIcon(true) }
   public var windowColorScheme: WindowColorScheme on replace { updateEloIcon(false) };
   var eloIcon: EloIcon;
   def eloIconsMap = new HashMap();

   def eloIconGroup = Group{};
   def eloIconNameLabel = Label{};

   function updateEloIcon(eloIconNameChanged: Boolean): Void {
      if (needToUpdateEloIcon) {
         if (eloIconNameChanged) {
            eloIcon = eloIconsMap.get(eloIconName) as EloIcon;
            eloIconGroup.content = eloIcon;
            eloIconNameLabel.text = eloIconName;
         }
         eloIcon.windowColorScheme = windowColorScheme;
      }
   }

   function createEloIcons() {
      for (eloIconName in eloIconFactory.getNames()) {
         def eloIcon = eloIconFactory.createEloIcon(eloIconName);
         eloIconsMap.put(eloIconName, eloIcon);
      }
   }

   public override function create(): Node {
      createEloIcons();
      needToUpdateEloIcon = true;
      updateEloIcon(true);
      VBox {
         content: [
            eloIconGroup,
            eloIconNameLabel
         ]
      }
   }

}
