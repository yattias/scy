/*
 * Edge.fx
 *
 * Created on 08.01.2010, 11:53:28
 */

package eu.scy.client.desktop.scydesktop.edges;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.Group;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;
import javafx.scene.shape.Line;
import java.util.Random;

/**
 * @author pg
 */

public class Edge extends CustomNode {

    public-init var manager: EdgesManager;
    
    public-init var start:ScyWindow;
    public-init var end:ScyWindow;
    public-init var text:String;
    var rand = Random{};
    public-read var line:Line = Line {
            startX: bind (start as StandardScyWindow).layoutX + (start.width/2);
            startY: bind (start as StandardScyWindow).layoutY + (start.height/2);

            endX: bind (end as StandardScyWindow).layoutX + (end.width/2);
            endY: bind (end as StandardScyWindow).layoutY + (end.height/2);
            strokeWidth: 4.0;
            opacity: 0.3;
            stroke: start.color;
    }

    var label:EdgeLabel = EdgeLabel {
            edge: this;
            labelText: text;
    }


    override protected function create () : Node {
        var g:Group = Group {
            content: bind [
                    line,
                    label,
                    ]
            ;
        }
    }

}