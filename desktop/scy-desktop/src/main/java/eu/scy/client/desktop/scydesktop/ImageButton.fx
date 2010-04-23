/*
 * ImageButton.fx
 *
 * Created on Feb 18, 2010, 11:48:05 AM
 */

package eu.scy.client.desktop.scydesktop;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.tooltips.impl.ColoredTextTooltip;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;

/**
 * @author anthonjp
 */

public class ImageButton extends ImageView, TooltipCreator {
        
    public var toolTip:String;
    public var tooltipManager: TooltipManager;
    override public function createTooltipNode (sourceNode : Node) : Node {
       var  tooltip = ColoredTextTooltip{
               content:toolTip

            }
         }
    


    public var normalImage: Image on replace {
        if(normalImage != null) { image = normalImage; }
    }
    public var selectImage: Image;

    public override var onMouseEntered = function(e:MouseEvent) {
        if(selectImage != null) { image = selectImage; }
    }

    public override var onMouseExited = function(e:MouseEvent) {
        if(normalImage != null) { image = normalImage; }
    }

}
