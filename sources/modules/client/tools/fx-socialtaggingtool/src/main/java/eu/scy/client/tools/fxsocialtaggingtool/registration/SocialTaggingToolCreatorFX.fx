package eu.scy.client.tools.fxsocialtaggingtool.registration;

import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import javafx.scene.Node;
//import colab.vt.whiteboard.component.WhiteboardPanel;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author sikken
 */
// place your code here
public class SocialTaggingToolCreatorFX extends ScyToolCreatorFX {

   override public function createScyToolNode(eloType: String, creatorId: String, scyWindow: ScyWindow, windowContent: Boolean): Node {
      //scyWindow.desiredContentWidth = 310;
      //scyWindow.desiredContentHeight = 200;
      //var whiteboardPanel = new WhiteboardPanel();
      //DrawingNode {
      //   whiteboardPanel: whiteboardPanel
      //   scyWindow: scyWindow
      //
       //TaggingToolNode{scyWindow: scyWindow}
       TaggingToolNode {}
   }

 

}
