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
					x: 3.42
					y: 0.14
					width: 37.0
					height: 37.0
				},
				Polygon {
					points: [40.42,0.14,40.30,40.14,0.30,40.14,0.30,0.14]
					fill: null
					stroke: null
				},
				Polygon {
					points: [40.42,28.14,13.42,0.14,40.42,0.14]
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
