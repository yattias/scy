package eu.scy.client.desktop.scydesktop.art.eloicons;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
/**
 * @author lars
 */
public class Save_as_datasetIcon extends AbstractEloIcon {

public override function clone(): Save_as_datasetIcon {
Save_as_datasetIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				Rectangle {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.secondColor
					x: 0.54
					y: 0.14
					width: 40.0
					height: 40.0
				},
				Rectangle {
					fill: bind windowColorScheme.mainColorLight
					stroke: bind windowColorScheme.secondColor
					x: 3.55
					y: 3.14
					width: 34.0
					height: 34.0
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 3.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 12.42
					startY: 25.64
					endX: 40.42
					endY: 25.64
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 3.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 12.42
					startY: 1.64
					endX: 40.42
					endY: 1.64
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 3.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 12.42
					startY: 9.64
					endX: 40.42
					endY: 9.64
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 3.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 12.42
					startY: 17.64
					endX: 40.42
					endY: 17.64
},
]
}
}
}
function run(){
   def windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   Stage {
	title: 'MyApp'
	onClose: function () {  }
	scene: Scene {
		width: 200
		height: 200
      fill: Color.YELLOW
		content: [
         Save_as_datasetIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Save_as_datasetIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
