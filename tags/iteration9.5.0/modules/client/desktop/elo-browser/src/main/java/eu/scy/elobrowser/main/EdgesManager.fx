/*
 * EdgesManager.fx
 *
 * Created on 27.03.2009, 13:55:24
 */

package eu.scy.elobrowser.main;

import eu.scy.elobrowser.awareness.contact.ContactWindow;
import eu.scy.scywindows.ScyEdgeLayer;
import eu.scy.scywindows.ScyWindow;
import java.lang.System;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;

/**
 * @author pg
 */

public class EdgesManager extends CustomNode {
    var nodes: Node[];
    public var contactWindow:ContactWindow; 
    public var edgeLayers: ScyEdgeLayer[];

    override function create():Node {
        return Group {
            content: bind nodes;
        }
    }

    public function createEdge(window1:ScyWindow, window2:ScyWindow,text:String):Void{
        var newEdge:ScyEdgeLayer = ScyEdgeLayer {
            caption: text;
            node1: window1;
            node2: window2;
            visible: bind (newEdge.node1.visible and newEdge.node2.visible and newEdge.node1.opacity == 1.0 and newEdge.node2.opacity == 1.0);
//            visible: bind (contactWindow.visible and contactWindow.opacity == 1.0);
        }
        System.out.println("newEdge.node1.visible: {newEdge.node1.visible}");
        System.out.println("newEdge.node2.visible: {newEdge.node2.visible}");
        window1.addEdge(newEdge);
        window2.addEdge(newEdge);
        insert newEdge into edgeLayers;
        insert newEdge into nodes;
        newEdge.repaint();
    }

    public function deleteEdge(edgeLayer:ScyEdgeLayer):Void {
        delete edgeLayer from edgeLayers;
        delete edgeLayer from nodes;
    }

}
