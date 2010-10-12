/*
 * TooltipCreator.fx
 *
 * Created on 26-nov-2009, 17:36:28
 */

package eu.scy.client.desktop.scydesktop.tooltips;

import javafx.scene.Node;

/**
 * @author sikken
 */

public mixin class TooltipCreator {
   public abstract function createTooltipNode(sourceNode:Node):Node;

}
