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
public class Save_asIcon extends AbstractEloIcon {

public override function clone(): Save_asIcon {
Save_asIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				Rectangle {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					x: 0.42
					y: 0.14
					width: 40.0
					height: 40.0
				},
				Rectangle {
					fill: bind windowColorScheme.mainColor
					stroke: null
					x: 3.42
					y: 3.14
					width: 34.0
					height: 34.0
				},
				Rectangle {
					fill: null
					stroke: null
					x: 0.3
					y: 0.14
					width: 40.0
					height: 40.0
				},
				Polygon {
					points: [37.23,3.33,31.46,13.74,37.22,24.16,26.80,18.40,16.39,24.14,22.14,13.74,16.40,3.31,26.81,9.08]
					fill: bind windowColorScheme.thirdColor
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 2.39
					strokeLineCap: StrokeLineCap.BUTT
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
         Save_asIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Save_asIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
