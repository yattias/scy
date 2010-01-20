/*
 * ExternalDocCreator.fx
 *
 * Created on 20-jan-2010, 12:04:46
 */

package eu.scy.client.desktop.scydesktop.tools.content.eloImporter;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import javafx.scene.Node;
import java.lang.String;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author sikken
 */

public class ExternalDocCreator extends ScyToolCreatorFX {
   public-init var extensions:String[];
    override public function createScyToolNode (type : String, window : ScyWindow, windowContent : Boolean) : Node {
        ExternalDoc{
           technicalType:type;
           extensions: extensions;
        }

    }


}
