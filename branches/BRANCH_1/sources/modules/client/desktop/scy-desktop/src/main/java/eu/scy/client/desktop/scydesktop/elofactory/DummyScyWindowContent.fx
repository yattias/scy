/*
 * DummyScyWindowContent.fx
 *
 * Created on 23-mrt-2009, 10:48:24
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

/**
 * @author sikkenj
 */

public class DummyScyWindowContent  extends CustomNode {
    public var label = "???";

    public override function create(): Node {
        return Group {
            content: [
                Text {
                    font: Font {
                        size: 12
                    }
                    x: 10,
                    y: 30
                    content: bind label
                }
            ]
        };
    }
}