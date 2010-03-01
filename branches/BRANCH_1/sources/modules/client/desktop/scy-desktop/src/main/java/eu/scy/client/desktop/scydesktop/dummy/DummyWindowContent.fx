/*
 * DummyWindowContent.fx
 *
 * Created on 30-jun-2009, 15:53:23
 */

package eu.scy.client.desktop.scydesktop.dummy;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * @author sikkenj
 */

public class DummyWindowContent  extends CustomNode {
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