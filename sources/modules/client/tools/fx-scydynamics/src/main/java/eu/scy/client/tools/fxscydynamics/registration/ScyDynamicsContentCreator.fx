package eu.scy.client.tools.fxscydynamics.registration;

import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import java.util.Properties;
import java.awt.Dimension;

public class ScyDynamicsContentCreator extends ScyToolCreatorFX {

    override public function createScyToolNode (type:String, scyWindow:ScyWindow, windowContent: Boolean) : Node {
        var props:Properties = new Properties();
        props.put("show.filetoolbar", "false");
        var editor:ModelEditor = new ModelEditor(props);
        var dim:Dimension = new Dimension(660,400);
        editor.setSize(dim);
        editor.setPreferredSize(dim);
        ScyDynamicsNode{
           modelEditor: editor;
           scyWindow:scyWindow
        }
    }

}