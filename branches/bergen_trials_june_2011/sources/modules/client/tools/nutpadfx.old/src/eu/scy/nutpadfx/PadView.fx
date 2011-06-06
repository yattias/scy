
package eu.scy.nutpadfx;

import javafx.scene.control.ScrollBar;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.KeyCode;
import javafx.util.Math;

/**
 * @author Rakesh Menon
 */

public class PadView extends CustomNode {
    
    public var listItem : PadItem[];    
    public var height = 100;
    public var width;
    
    
    public var selectedListItem : PadItem;
    
    override var focusTraversable = true;
    
    
   public var scrollBar : ScrollBar = ScrollBar {
        translateX: bind (width - 9 - scrollBar.width)
       //translateX: 400
        //translateY: bind (height - scrollBar.height)
        translateY: 2
        min: 0
        max: bind Math.max((padView.boundsInLocal.height - height), 0)
        vertical: true
        height: bind height
        blockIncrement: PadItem.height
        focusTraversable: false
        blocksMouse: true
    };

    var padView = VBox {
        spacing: 2
        translateX: 2
        translateY: bind -(scrollBar.value) + 2
        content: bind listItem
        focusTraversable: false
    };

    override var onKeyPressed = function(e) {
        if(e.code == KeyCode.VK_UP) {
            scrollBar.adjustValue(-1);
        } else if(e.code == KeyCode.VK_DOWN) {
            scrollBar.adjustValue(1);
        }
    }

    override function create() : Node {
        
        Group {
            content: [
                Group {
                    content: [ padView ]
                },
                
                scrollBar
            ]
            
            
        }
    }
}
