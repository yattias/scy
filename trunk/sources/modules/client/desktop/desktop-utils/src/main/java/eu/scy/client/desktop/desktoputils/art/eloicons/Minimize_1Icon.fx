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
public class Minimize_1Icon extends AbstractEloIcon {

public override function clone(): Minimize_1Icon {
Minimize_1Icon {
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
					x: 0.42
					y: 0.11
					width: 40.0
					height: 35.0
				},
				Rectangle {
					fill: null
					stroke: null
					x: 0.42
					y: 0.14
					width: 40.0
					height: 40.0
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
         Minimize_1Icon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Minimize_1Icon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
