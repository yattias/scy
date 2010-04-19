/*
 * ButtonWithTooltip.fx
 *
 * Created on 14-jan-2010, 15:09:56
 */

package eu.scy.client.desktop.scydesktop.corners.tools;
import javafx.scene.control.Button;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import javafx.scene.Node;
import javafx.scene.text.Text;

/**
 * @author sikken
 */

// place your code here

public class ButtonWithTooltip extends Button, TooltipCreator{
   public var tooltip:String;

    override public function createTooltipNode (sourceNode : Node) : Node {
        Text{
           content:tooltip
        }
    }
}
