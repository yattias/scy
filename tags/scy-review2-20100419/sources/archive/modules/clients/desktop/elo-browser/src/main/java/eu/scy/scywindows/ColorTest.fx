/*
 * ColorTest.fx
 *
 * Created on 1-apr-2009, 12:04:30
 */

package eu.scy.scywindows;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

/**
 * @author sikkenj
 */

Stage {
    title : "Color test"
    scene: Scene {
        width: 200
        height: 200
        content: [
                Rectangle {
            x: 10, y: 10
            width: 100, height: 90
            fill: Color.web("#8db800")
        }

        ]
    }
}
