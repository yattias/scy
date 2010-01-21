package eu.scy.client.tools.fxsimulator.registration;

import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class SimulatorContentCreator extends ScyToolCreatorFX {

    var uriFiel:JTextField;

    override public function createScyToolNode (type:String, scyWindow:ScyWindow, windowContent: Boolean) : Node {
        var panel:JPanel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setVisible(true);
        SimulatorNode{
           simquestPanel: panel;
           scyWindow: scyWindow
        }
    }

}