/*
 * InterviewToolContentCreator.fx
 *
 * Created on 11.12.2009, 10:30:01
 */

package eu.scy.client.tools.interviewtool;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolWindowContentCreatorFX;

/**
 * @author kaido
 */

public class InterviewToolContentCreator extends ScyToolWindowContentCreatorFX {
   public override function createScyToolWindowContent():Node{
      return InterviewToolNode{};
   }
}
