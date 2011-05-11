package eu.scy.client.desktop.desktoputils.art.eloicons;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
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
					stroke: null
					x: 0.44
					y: 0.12
					width: 40.05
					height: 40.0
				},
				Rectangle {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					x: 3.57
					y: 3.11
					width: 34.25
					height: 34.0
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 3.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 12.51
					startY: 25.62
					endX: 40.56
					endY: 25.62
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 3.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 12.51
					startY: 1.62
					endX: 40.56
					endY: 1.62
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 3.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 12.51
					startY: 9.62
					endX: 40.56
					endY: 9.62
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 3.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 12.51
					startY: 17.62
					endX: 40.56
					endY: 17.62
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
