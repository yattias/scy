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
public class Import_2Icon extends AbstractEloIcon {

public override function clone(): Import_2Icon {
Import_2Icon {
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
					x: 0.42
					y: 0.14
					width: 40.0
					height: 40.0
				},
				Rectangle {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					x: 3.65
					y: 2.72
					width: 33.77
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
					points: [3.67,3.85,19.48,20.14,3.67,35.85]
					fill: bind windowColorScheme.secondColor
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
         Import_2Icon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Import_2Icon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
