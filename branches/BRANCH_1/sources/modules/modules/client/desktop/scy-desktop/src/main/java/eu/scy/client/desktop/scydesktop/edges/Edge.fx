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
import java.lang.System;
import java.util.Random;

/**
 * @author pg
 */

public class Edge extends CustomNode {

    public-init var manager: EdgesManager;
    
    public-init var start:ScyWindow;
    public-init var end:ScyWindow;
    var rand = Random{};
    public-read var line:Line = Line {
            startX: bind (start as StandardScyWindow).layoutX + (start.width/2);
            startY: bind (start as StandardScyWindow).layoutY + (start.height/2);

            endX: bind (end as StandardScyWindow).layoutX + (end.width/2);
            endY: bind (end as StandardScyWindow).layoutY + (end.height/2);
    }

    var label:EdgeLabel = EdgeLabel {
            edge: this;
            labelText: "i am a label! {rand.nextInt(1337)}"
    }


    var length:Number;
    public function paintEdge():Void {
            /*
            var startX:Number = (start as StandardScyWindow).layoutX;
            var startY:Number = (start as StandardScyWindow).layoutY;
            var startHeight:Number = start.height;
            var startWidth:Number = start.width;

            var endX:Number = (end as StandardScyWindow).layoutX;
            var endY:Number = (end as StandardScyWindow).layoutY;
            var endHeight:Number = end.height;
            var endWidth:Number = end.width;

            edge = Line {
                startX: startX + (startWidth/2),
                startY: startY + (startHeight/2),

                endX: endX + (endWidth/2),
                endY: endY + (endHeight/2)
            }

            length = javafx.util.Math.sqrt((endX-startX)*(endX-startX) + (endY-startY)*(endY-startY));

            label.x = (edge.startX+edge.endX) / 2;
            label.y = (edge.startY+edge.endY) / 2;
            */
            //System.out.println(length);
    }

    public public function deleteMe () : Void {
            manager.removeEdge(start, end, this);
            start = null;
            end = null;
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