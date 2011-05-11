/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.colorscheme;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.desktoputils.art.eloicons.EloIconFactory;

/**
 * @author SikkenJ
 */
public class ColorSchemeEditorCreator extends ScyToolCreatorFX {

   def eloIconFactory = new EloIconFactory();

   override public function createScyToolNode(eloType: String, creatorId: String, scyWindow: ScyWindow, windowContent: Boolean): Node {
      ColorSchemeEditorNode {
         window: scyWindow
         eloIconFactory: eloIconFactory
      }
   }

}
