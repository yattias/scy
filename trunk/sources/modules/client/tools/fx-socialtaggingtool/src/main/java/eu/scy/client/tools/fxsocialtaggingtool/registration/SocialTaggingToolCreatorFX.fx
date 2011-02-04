/*
 * DrawingToolCreatorFX.fx
 *
 * Created on 13-jan-2010, 14:12:17
 */
package eu.scy.client.tools.fxsocialtaggingtool.registration;

import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import javafx.scene.Node;
import colab.vt.whiteboard.component.WhiteboardPanel;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.util.StringLocalizer;
import javafx.scene.control.ListView;

/**
 * @author sikken
 */
// place your code here
public class SocialTaggingToolCreatorFX extends ScyToolCreatorFX {

   override public function createScyToolNode(eloType: String, creatorId: String, scyWindow: ScyWindow, windowContent: Boolean): Node {
      StringLocalizer.associate("languages.fxdrawingtool", "eu.scy.client.tools.fxdrawingtool");
      scyWindow.desiredContentWidth = 310;
      scyWindow.desiredContentHeight = 200;
      var whiteboardPanel = new WhiteboardPanel();
      //DrawingNode {
      //   whiteboardPanel: whiteboardPanel
      //   scyWindow: scyWindow
      //
      ListView {}
      }

   

}
