/*
 * Main.fx
 *
 * Created on 8-jul-2009, 14:51:44
 */

package fxdrawingtool;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

/**
 * @author sikkenj
 */

Stage {
    title: "Application title"
    width: 250
    height: 80
    scene: Scene {
        content: [
            Text {
                font : Font {
                    size : 16
                }
                x: 10
                y: 30
                content: "Application content"
            }
        ]
    }
}