/*
 * SuperTestStage.fx
 *
 * Created on 16.06.2009, 13:57:59
 */

package eu.scy.elobrowser.tool.pictureviewer;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

/**
 * @author pg
 */

 var picViewer: PictureViewerNode = PictureViewerNode{
        title: "a great smile";
        description: "a wonderfull smile i found on the interwebs";
        author: "I";
        dateCreatedString: "yesterday";
     };

 var scyWind = picViewer.createPictureViewerWindow(picViewer);

Stage {
    title: "Application title"
    width: 600
    height: 600
    scene: Scene {
        content: [
            scyWind
        ]
    }
}