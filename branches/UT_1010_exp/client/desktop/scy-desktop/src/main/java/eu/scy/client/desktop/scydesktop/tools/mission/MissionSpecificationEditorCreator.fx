/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.mission;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author SikkenJ
 */
// place your code here
public class MissionSpecificationEditorCreator extends ScyToolCreatorFX {

   override public function createScyToolNode(eloType: String, creatorId: String, window: ScyWindow, windowContent: Boolean): Node {
      MissionSpecificationEditor {
         window: window
      }

   }

}
