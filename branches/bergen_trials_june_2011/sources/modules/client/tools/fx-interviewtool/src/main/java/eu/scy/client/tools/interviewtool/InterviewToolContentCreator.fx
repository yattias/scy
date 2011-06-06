/*
 * InterviewToolContentCreator.fx
 *
 * Created on 11.12.2009, 10:30:01
 */

package eu.scy.client.tools.interviewtool;

import javafx.scene.Node;
import javafx.util.StringLocalizer;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author kaido
 */

/**
* Standard SCY Tool creator class for Interview Tool
*/
public class InterviewToolContentCreator extends ScyToolCreatorFX {
    
   public override function createScyToolNode(eloType:String, creatorId:String, scyWindow:ScyWindow, windowContent: Boolean):Node {
      StringLocalizer.associate("eu.scy.client.tools.interviewtool.resources.InterviewTool", "eu.scy.client.tools.interviewtool");
      return InterviewToolScyNode{};
   }
}
