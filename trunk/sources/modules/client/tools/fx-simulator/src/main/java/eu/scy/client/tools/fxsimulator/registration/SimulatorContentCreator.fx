package eu.scy.client.tools.fxsimulator.registration;

import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javafx.util.StringLocalizer;

public class SimulatorContentCreator extends ScyToolCreatorFX {

    override public function createScyToolNode (eloType:String, creatorId: String, scyWindow:ScyWindow, windowContent: Boolean) : Node {
        StringLocalizer.associate("languages.fxsimulator", "eu.scy.client.tools.fxsimulator.registration");
        var panel:JPanel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setVisible(true);
        scyWindow.desiredContentWidth = 400;
        scyWindow.desiredContentHeight = 400;
        SimulatorNode{
           simquestPanel: panel;
           scyWindow: scyWindow
        }
    }

}