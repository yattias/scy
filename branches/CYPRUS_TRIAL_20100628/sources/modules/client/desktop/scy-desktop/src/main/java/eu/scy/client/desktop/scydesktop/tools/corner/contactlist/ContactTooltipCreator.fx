/*
 * ContactTooltipCreator.fx
 *
 * Created on 11.02.2010, 16:24:13
 */

package eu.scy.client.desktop.scydesktop.tools.corner.contactlist;

import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import javafx.scene.Node;

/**
 * @author svenmaster
 */

public class ContactTooltipCreator extends TooltipCreator{

    override public function createTooltipNode (sourceNode : Node) : Node {
            if (sourceNode instanceof ContactFrame){
                def contactFrame:ContactFrame = sourceNode as ContactFrame;
                return ContactTooltip{contact: bind contactFrame.contact};
            } else {
                return null;
            }
        }
}