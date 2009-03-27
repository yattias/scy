/*
 * EdgesManager.fx
 *
 * Created on 27.03.2009, 13:55:24
 */

package eu.scy.elobrowser.main;

import eu.scy.scywindows.ScyEdgeLayer;
import eu.scy.scywindows.ScyWindow;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;

/**
 * @author pg
 */

public class EdgesManager extends CustomNode {
    var nodes: Node[];


    override function create():Node {
        return Group {
            content: bind nodes;
        }

    }

    public function createEdge(node1:ScyWindow, node2:ScyWindow,text:String) {
        var newEdge = ScyEdgeLayer {
            caption: text;
            node1: node1;
            node2: node2;
        }
        node1.addEdge(newEdge);
        node2.addEdge(newEdge);
        insert newEdge into nodes;

    }


}
