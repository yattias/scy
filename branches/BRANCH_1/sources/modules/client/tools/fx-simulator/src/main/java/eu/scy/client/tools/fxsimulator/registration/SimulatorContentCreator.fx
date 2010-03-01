package eu.scy.client.tools.fxsimulator.registration;

import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class SimulatorContentCreator extends ScyToolCreatorFX {

    override public function createScyToolNode (eloType:String, creatorId: String, scyWindow:ScyWindow, windowContent: Boolean) : Node {
        var panel:JPanel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setVisible(true);
        SimulatorNode{
           simquestPanel: panel;
           scyWindow: scyWindow
        }
    }

}