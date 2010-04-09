/*
 * InterviewToolContentCreator.fx
 *
 * Created on 11.12.2009, 10:30:01
 */

package eu.scy.client.tools.interviewtool;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolWindowContentCreatorFX;
import javafx.util.StringLocalizer;

/**
 * @author kaido
 */

/**
* Standard SCY Tool creator class for Interview Tool
*/
public class InterviewToolContentCreator extends ScyToolWindowContentCreatorFX {
   public override function createScyToolWindowContent():Node{
      StringLocalizer.associate("eu.scy.client.tools.interviewtool.resources.InterviewTool", "eu.scy.client.tools.interviewtool");
      return InterviewToolScyNode{};
   }
}
