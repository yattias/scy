package eu.scy.client.desktop.scydesktop.feedbackquestion;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

public class FeedbackQuestionNodeCreator extends ScyToolCreatorFX{

    override public function createScyToolNode (eloType:String,creatorId:String, scyWindow:ScyWindow, windowContent:Boolean) : Node {
        FeedbackQuestionNode{}
    }

}
