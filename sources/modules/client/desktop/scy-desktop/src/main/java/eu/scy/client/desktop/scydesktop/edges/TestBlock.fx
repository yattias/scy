

package eu.scy.client.desktop.scydesktop.edges;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
/*
 * TestBlock.fx
 *
 * Created on 25.03.2010, 12:52:58
 */



public class TestBlock extends CustomNode {
        public var height:Number;
        public var width:Number;


    override protected function create () : Node {
            var g:Group = Group {
                content: [
                        Rectangle {
                            height: bind height;
                            width: bind width;
                        }

                        ]
            }

            return g;

    }
}


