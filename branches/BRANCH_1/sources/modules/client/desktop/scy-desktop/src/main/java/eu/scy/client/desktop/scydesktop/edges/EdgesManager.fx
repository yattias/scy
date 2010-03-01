/*
 * EdgesManager.fx
 *
 * Created on 08.01.2010, 11:35:55
 */

package eu.scy.client.desktop.scydesktop.edges;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.Group;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.lang.System;


public class EdgesManager extends CustomNode {

    var nodes:Node[];

    public function addEdge(window1:ScyWindow, window2:ScyWindow):Void {
            //System.out.println("manager: adding edges.");
            var edge:Edge = Edge {
                start: window1
                end: window2
                manager: this;
            }
            window1.addEdge(edge);
            window2.addEdge(edge);
            insert edge into nodes;
            edge.paintEdge();

    }


    public function removeEdge(window1:ScyWindow, window2:ScyWindow, edge:Edge):Void {
            //System.out.println("manager.removeEdge()");
            window1.removeEdge(edge);
            window2.removeEdge(edge);
            delete edge from nodes;
    }


    override protected function create () : Node {
        var g:Group = Group {
                content: bind nodes;
        }

    }


}
