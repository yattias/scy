/*
 * TooltipManager.fx
 *
 * Created on 26-nov-2009, 17:34:55
 */
package eu.scy.client.desktop.scydesktop.tooltips;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * @author sikken
 */
// place your code here
public mixin class TooltipManager {

   public abstract function registerNode(sourceNode: Node, tooltipCreator: TooltipCreator): Void;

   public abstract function unregisterNode(sourceNode: Node): Void;

   public abstract function removeTooltip(): Void;

   public abstract function onMouseEntered(e: MouseEvent, tooltipCreator: TooltipCreator): Void;

   public abstract function onMouseExited(e: MouseEvent): Void;

}
