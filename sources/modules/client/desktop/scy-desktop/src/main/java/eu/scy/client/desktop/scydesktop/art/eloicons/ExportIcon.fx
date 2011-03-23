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
public class ExportIcon extends AbstractEloIcon {

public override function clone(): ExportIcon {
ExportIcon {
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
					x: 0.3
					y: 0.14
					width: 40.0
					height: 40.0
				},
				Rectangle {
					fill: bind windowColorScheme.mainColor
					stroke: null
					x: 3.3
					y: 3.55
					width: 33.77
					height: 34.0
				},
				Rectangle {
					fill: null
					stroke: null
					x: 0.42
					y: 0.14
					width: 40.0
					height: 40.0
				},
				Polygon {
					points: [21.28,3.79,37.04,20.19,21.28,35.79]
					fill: bind windowColorScheme.thirdColor
					stroke: null
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
         ExportIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         ExportIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
