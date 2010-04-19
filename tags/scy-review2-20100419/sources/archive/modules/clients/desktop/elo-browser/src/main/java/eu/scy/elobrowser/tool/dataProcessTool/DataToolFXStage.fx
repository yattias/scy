/*
 * DataToolFXStage.fx
 *
 * Created on 25 juin 2009, 14:20:56
 */

package eu.scy.elobrowser.tool.dataProcessTool;

import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * run data processing tool without scy-lab
 * @author Marjolaine
 */

var dataViewer: DataToolNode = DataToolNode{
    dataToolPanel: new DataToolPanel();
     };

 var scyWind = dataViewer.createDataToolWindow(dataViewer);


Stage {
    title: "Data Processing Tool"
    width: 600
    height: 600
    scene: Scene {
        content: [
            scyWind
        ]

    }
}