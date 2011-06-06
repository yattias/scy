package eu.scy.client.desktop.scydesktop.feedbackquestion;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.ScyDesktop;

public class FeedbackQuestionNodeCreator extends ScyToolCreatorFX{
    public var scyDesktop:ScyDesktop;
    override public function createScyToolNode (eloType:String,creatorId:String, scyWindow:ScyWindow, windowContent:Boolean) : Node {
        FeedbackQuestionNode{
            scyDesktop: scyDesktop,
            scyWindow: scyWindow
        }
    }
}
